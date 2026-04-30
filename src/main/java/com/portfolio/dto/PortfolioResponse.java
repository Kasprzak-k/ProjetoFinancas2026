package com.portfolio.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PortfolioResponse {
    private BigDecimal totalValue;
    private BigDecimal totalUnrealizedPnl;
    private List<AssetPositionDto> assets;
}
