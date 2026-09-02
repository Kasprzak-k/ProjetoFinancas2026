package com.portfolio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Cliente HTTP para a API pública (keyless) do CoinGecko.
 * Endpoint utilizado: GET /api/v3/simple/price
 * Exemplo: https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd
 * Resposta: {"bitcoin":{"usd":78759},"ethereum":{"usd":2495.98}}
 */
@Slf4j
@Component
public class CoinGeckoClient {

    private static final String VS_CURRENCY = "usd";

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CoinGeckoClient(
            RestTemplate restTemplate,
            @Value("${coingecko.api.base-url:https://api.coingecko.com/api/v3}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * Busca o preço em USD de um único ativo pelo seu ID no CoinGecko (ex: "bitcoin", "ethereum").
     *
     * @param coinGeckoId ID do ativo na API do CoinGecko
     * @return Optional com o preço em USD, ou empty() em caso de erro / ativo não encontrado
     */
    public Optional<BigDecimal> getPriceInUsd(String coinGeckoId) {
        Map<String, Map<String, BigDecimal>> prices = fetchPrices(coinGeckoId);
        return Optional.ofNullable(prices.get(coinGeckoId))
                .map(currencyMap -> currencyMap.get(VS_CURRENCY));
    }

    /**
     * Busca os preços em USD de múltiplos ativos de uma só vez (batch).
     *
     * @param coinGeckoIds IDs separados por vírgula (ex: "bitcoin,ethereum,solana")
     * @return Mapa com estrutura: { "bitcoin": { "usd": 78759.0 }, ... }
     */
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, BigDecimal>> fetchPrices(String coinGeckoIds) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/simple/price")
                .queryParam("ids", coinGeckoIds)
                .queryParam("vs_currencies", VS_CURRENCY)
                .build()
                .toUriString();

        log.debug("Chamando CoinGecko API: {}", url);

        try {
            Map<?, ?> rawResponse = restTemplate.getForObject(url, Map.class);
            if (rawResponse == null || rawResponse.isEmpty()) {
                log.warn("CoinGecko retornou resposta vazia para IDs: {}", coinGeckoIds);
                return Collections.emptyMap();
            }
            return (Map<String, Map<String, BigDecimal>>) rawResponse;
        } catch (RestClientException e) {
            log.error("Erro ao chamar a API do CoinGecko para IDs '{}': {}", coinGeckoIds, e.getMessage());
            return Collections.emptyMap();
        }
    }
}
