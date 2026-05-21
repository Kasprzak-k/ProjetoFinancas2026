package com.portfolio.service;

import com.portfolio.domain.AssetType;
import com.portfolio.domain.Transaction;
import com.portfolio.domain.TransactionType;
import com.portfolio.dto.AssetPositionDto;
import com.portfolio.dto.PortfolioResponse;
import com.portfolio.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final TransactionRepository transactionRepository;
    private final PriceService priceService;

    @Transactional(readOnly = true)
    @Cacheable(value = "portfolio", key = "#userId")
    public PortfolioResponse calculatePortfolio(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByTimestampAsc(userId);
        
        // asset symbol -> Position
        Map<String, PositionAccumulator> positions = new HashMap<>();

        for (Transaction t : transactions) {
            String symbol = t.getAsset().getSymbol();
            AssetType type = t.getAsset().getType();
            positions.putIfAbsent(symbol, new PositionAccumulator(symbol, type));
            
            PositionAccumulator pos = positions.get(symbol);
            if (t.getType() == TransactionType.BUY) {
                // Calculate new average price: (old_qty * old_avg + new_qty * new_price) / (old_qty + new_qty)
                BigDecimal totalCost = pos.quantity.multiply(pos.avgPrice)
                        .add(t.getQuantity().multiply(t.getPrice()));
                pos.quantity = pos.quantity.add(t.getQuantity());
                if (pos.quantity.compareTo(BigDecimal.ZERO) > 0) {
                    pos.avgPrice = totalCost.divide(pos.quantity, 8, RoundingMode.HALF_UP);
                }
            } else if (t.getType() == TransactionType.SELL) {
                pos.quantity = pos.quantity.subtract(t.getQuantity());
                // Avg price doesn't change on sell
            }
        }

        BigDecimal totalPortfolioValue = BigDecimal.ZERO;
        BigDecimal totalUnrealizedPnl = BigDecimal.ZERO;
        List<AssetPositionDto> assetDtos = new ArrayList<>();

        for (PositionAccumulator pos : positions.values()) {
            if (pos.quantity.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal currentPrice = priceService.getCurrentPrice(pos.symbol);
                BigDecimal currentValue = pos.quantity.multiply(currentPrice);
                BigDecimal totalCost = pos.quantity.multiply(pos.avgPrice);
                BigDecimal pnl = currentValue.subtract(totalCost);

                totalPortfolioValue = totalPortfolioValue.add(currentValue);
                totalUnrealizedPnl = totalUnrealizedPnl.add(pnl);

                assetDtos.add(AssetPositionDto.builder()
                        .symbol(pos.symbol)
                        .type(pos.type)
                        .quantity(pos.quantity)
                        .avgPrice(pos.avgPrice)
                        .currentPrice(currentPrice)
                        .currentValue(currentValue)
                        .pnl(pnl)
                        .build());
            }
        }

        return PortfolioResponse.builder()
                .totalValue(totalPortfolioValue)
                .totalUnrealizedPnl(totalUnrealizedPnl)
                .assets(assetDtos)
                .build();
    }

    private static class PositionAccumulator {
        String symbol;
        AssetType type;
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal avgPrice = BigDecimal.ZERO;

        PositionAccumulator(String symbol, AssetType type) {
            this.symbol = symbol;
            this.type = type;
        }
    }
}
