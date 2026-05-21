package com.portfolio.service;

import com.portfolio.domain.Asset;
import com.portfolio.domain.Transaction;
import com.portfolio.domain.TransactionType;
import com.portfolio.domain.User;
import com.portfolio.dto.TransactionRequest;
import com.portfolio.dto.TransactionResponse;
import com.portfolio.exception.InsufficientPositionException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.AssetRepository;
import com.portfolio.repository.TransactionRepository;
import com.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    @Transactional
    @CacheEvict(value = "portfolio", key = "#request.userId")
    public TransactionResponse registerTransaction(TransactionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Asset asset = assetRepository.findBySymbol(request.getAssetSymbol())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        if (request.getType() == TransactionType.SELL) {
            validateSellPosition(user.getId(), asset.getId(), request.getQuantity());
        }

        Transaction transaction = Transaction.builder()
                .user(user)
                .asset(asset)
                .type(request.getType())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .timestamp(request.getTimestamp())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToResponse(savedTransaction);
    }

    private void validateSellPosition(Long userId, Long assetId, BigDecimal sellQuantity) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByTimestampAsc(userId);
        
        BigDecimal currentQuantity = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getAsset().getId().equals(assetId)) {
                if (t.getType() == TransactionType.BUY) {
                    currentQuantity = currentQuantity.add(t.getQuantity());
                } else {
                    currentQuantity = currentQuantity.subtract(t.getQuantity());
                }
            }
        }

        if (currentQuantity.compareTo(sellQuantity) < 0) {
            throw new InsufficientPositionException(
                    "Cannot sell more than current position. Current: " + currentQuantity);
        }
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByUser(Long userId) {
        return transactionRepository.findByUserIdOrderByTimestampAsc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .assetSymbol(transaction.getAsset().getSymbol())
                .type(transaction.getType())
                .quantity(transaction.getQuantity())
                .price(transaction.getPrice())
                .timestamp(transaction.getTimestamp())
                .build();
    }
}
