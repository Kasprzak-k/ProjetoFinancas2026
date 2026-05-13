package com.portfolio.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import java.io.Serializable;

@Data
@Builder
public class PortfolioResponse implements Serializable {
    private BigDecimal totalValue;
    private BigDecimal totalUnrealizedPnl;
    private List<AssetPositionDto> assets;
}
