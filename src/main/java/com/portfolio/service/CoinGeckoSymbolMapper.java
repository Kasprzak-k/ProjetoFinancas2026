package com.portfolio.service;

import java.util.Map;

/**
 * Mapeamento entre os símbolos internos do sistema e os IDs da API do CoinGecko.
 * A API pública (keyless) do CoinGecko utiliza IDs em minúsculo por extenso,
 * diferente dos tickers/símbolos convencionais (ex: BTC vs "bitcoin").
 *
 * Referência: https://api.coingecko.com/api/v3/coins/list
 */
public final class CoinGeckoSymbolMapper {

    private CoinGeckoSymbolMapper() {
        // Classe utilitária — não instanciável
    }

    /**
     * Mapa de símbolo interno → ID do CoinGecko.
     * Adicione novos ativos cripto conforme necessário.
     */
    private static final Map<String, String> SYMBOL_TO_COINGECKO_ID = Map.ofEntries(
            Map.entry("BTC",  "bitcoin"),
            Map.entry("ETH",  "ethereum"),
            Map.entry("SOL",  "solana"),
            Map.entry("BNB",  "binancecoin"),
            Map.entry("ADA",  "cardano"),
            Map.entry("XRP",  "ripple"),
            Map.entry("DOT",  "polkadot"),
            Map.entry("DOGE", "dogecoin"),
            Map.entry("AVAX", "avalanche-2"),
            Map.entry("MATIC","matic-network"),
            Map.entry("LINK", "chainlink"),
            Map.entry("UNI",  "uniswap"),
            Map.entry("LTC",  "litecoin"),
            Map.entry("ATOM", "cosmos"),
            Map.entry("XLM",  "stellar")
    );

    /**
     * Converte um símbolo interno (ex: "BTC") para o ID do CoinGecko (ex: "bitcoin").
     *
     * @param symbol símbolo do ativo em maiúsculo
     * @return ID do CoinGecko, ou null se o símbolo não for mapeado
     */
    public static String toCoingeckoId(String symbol) {
        return SYMBOL_TO_COINGECKO_ID.get(symbol.toUpperCase());
    }

    /**
     * Verifica se um símbolo possui mapeamento para o CoinGecko.
     */
    public static boolean isMapped(String symbol) {
        return SYMBOL_TO_COINGECKO_ID.containsKey(symbol.toUpperCase());
    }
}
