package com.portfolio.controller;

import com.portfolio.dto.PortfolioResponse;
import com.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<PortfolioResponse> getPortfolio(@RequestParam Long userId) {
        return ResponseEntity.ok(portfolioService.calculatePortfolio(userId));
    }
}
