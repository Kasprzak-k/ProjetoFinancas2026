package com.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuração do cliente HTTP utilizado para integrar com APIs externas,
 * em especial a API pública (keyless) do CoinGecko.
 */
@Configuration
public class RestClientConfig {

    /**
     * Bean de RestTemplate configurado com timeouts adequados para chamadas à API do CoinGecko.
     * - connectTimeout: tempo máximo para estabelecer a conexão TCP.
     * - readTimeout: tempo máximo aguardando a resposta após a conexão.
     */
    @Bean
    public RestTemplate restTemplate(
            RestTemplateBuilder builder,
            @Value("${coingecko.api.connect-timeout-ms:3000}") int connectTimeoutMs,
            @Value("${coingecko.api.read-timeout-ms:5000}") int readTimeoutMs) {

        return builder
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .readTimeout(Duration.ofMillis(readTimeoutMs))
                .build();
    }
}
