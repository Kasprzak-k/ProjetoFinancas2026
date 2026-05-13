package com.portfolio.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import java.io.Serializable;

@Data
@Builder
public class AssetPositionDto implements Serializable {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal avgPrice;
    private BigDecimal currentPrice;
    private BigDecimal currentValue;
    private BigDecimal pnl;
}
