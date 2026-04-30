package com.portfolio.controller;

import com.portfolio.dto.TransactionRequest;
import com.portfolio.dto.TransactionResponse;
import com.portfolio.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        return new ResponseEntity<>(transactionService.registerTransaction(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam Long userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }
}
