package com.portfolio.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class PriceService {

    private final Random random = new Random();

    @Cacheable(value = "assetPrices", key = "#symbol", unless = "#result == null")
    public BigDecimal getCurrentPrice(String symbol) {
        // Mocking price between 10 and 1000
        double price = 10 + (990 * random.nextDouble());
        return BigDecimal.valueOf(price);
    }
}
