package com.portfolio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Serviço de preços de ativos.
 *
 * Pendência 1.A resolvida: integração com a API pública (keyless) do CoinGecko
 * para ativos do tipo CRYPTO, substituindo os valores mockados aleatórios.
 *
 * Fluxo para ativos CRYPTO:
 *   1. Converte o símbolo (ex: "BTC") para o ID do CoinGecko (ex: "bitcoin")
 *      via {@link CoinGeckoSymbolMapper}.
 *   2. Chama o endpoint público:
 *      GET https://api.coingecko.com/api/v3/simple/price?ids={id}&vs_currencies=usd
 *   3. O resultado é armazenado no cache Redis com TTL de 5 minutos
 *      (configurado em {@link com.portfolio.config.RedisConfig}).
 *   4. Em caso de erro na API ou símbolo não mapeado, retorna BigDecimal.ZERO
 *      e registra um aviso no log para facilitar diagnóstico.
 *
 * Ativos STOCK/ETF ainda não possuem integração com API de mercado de ações
 * (pendência 2 do documento de pendências) — retornam BigDecimal.ZERO.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService {

    private final CoinGeckoClient coinGeckoClient;

    /**
     * Retorna o preço atual em USD do ativo identificado pelo símbolo.
     * O resultado é cacheado no Redis (cache "assetPrices", TTL 5 min).
     *
     * @param symbol símbolo do ativo (ex: "BTC", "ETH", "AAPL")
     * @return preço atual em USD, ou {@link BigDecimal#ZERO} se não disponível
     */
    @Cacheable(value = "assetPrices", key = "#symbol", unless = "#result == null || #result == T(java.math.BigDecimal).ZERO")
    public BigDecimal getCurrentPrice(String symbol) {
        String upperSymbol = symbol.toUpperCase();

        if (!CoinGeckoSymbolMapper.isMapped(upperSymbol)) {
            log.warn("Símbolo '{}' não possui mapeamento para o CoinGecko. " +
                     "Preço retornado: 0 (adicione o mapeamento em CoinGeckoSymbolMapper).", upperSymbol);
            return BigDecimal.ZERO;
        }

        String coinGeckoId = CoinGeckoSymbolMapper.toCoingeckoId(upperSymbol);
        Optional<BigDecimal> price = coinGeckoClient.getPriceInUsd(coinGeckoId);

        if (price.isEmpty()) {
            log.error("Não foi possível obter o preço de '{}' (CoinGecko ID: '{}') via API. " +
                      "Verifique a conectividade ou os rate limits da API pública.", upperSymbol, coinGeckoId);
            return BigDecimal.ZERO;
        }

        log.info("Preço obtido via CoinGecko API — {}: USD {}", upperSymbol, price.get());
        return price.get();
    }
}
