package com.portfolio.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AssetPositionDto {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal avgPrice;
    private BigDecimal currentPrice;
    private BigDecimal currentValue;
    private BigDecimal pnl;
}
