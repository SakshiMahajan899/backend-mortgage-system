package com.bank.mortgage.controller;

import com.bank.mortgage.model.InterestRateResponse;
import com.bank.mortgage.model.MortgageCheckRequest;
import com.bank.mortgage.model.MortgageCheckResponse;
import com.bank.mortgage.service.MortgageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller for handling mortgage-related operations.
 */
@RestController
@RequiredArgsConstructor
public class MortgageController {
    private static final Logger logger = LoggerFactory.getLogger(MortgageController.class);

    private final MortgageService mortgageService;

    /**
     * Endpoint to fetch interest rates.
     *
     * @return ResponseEntity containing the list of interest rate responses.
     */
    @GetMapping("/api/v1/interest-rates")
    public ResponseEntity<List<InterestRateResponse>> getInterestRates() {
        logger.info("Fetching interest rates");
        List<InterestRateResponse> interestRates = mortgageService.getInterestRates();
        return ResponseEntity.ok(interestRates);
    }

    /**
     * Endpoint to check the feasibility of a mortgage.
     *
     * @param request Mortgage check request payload.
     * @return ResponseEntity containing the mortgage check response.
     */
    @PostMapping("/api/v1/mortgage-check")
    public ResponseEntity<MortgageCheckResponse> mortgageCheck(@RequestBody MortgageCheckRequest request) {
        logger.info("Processing mortgage check request: {}", request);
        MortgageCheckResponse response = mortgageService.calculateMortgage(request);
        return ResponseEntity.ok(response);
    }
}
