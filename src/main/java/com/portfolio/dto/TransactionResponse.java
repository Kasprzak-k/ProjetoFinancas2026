package com.portfolio.dto;

import com.portfolio.domain.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private Long userId;
    private String assetSymbol;
    private TransactionType type;
    private BigDecimal quantity;
    private BigDecimal price;
    private LocalDateTime timestamp;
}
