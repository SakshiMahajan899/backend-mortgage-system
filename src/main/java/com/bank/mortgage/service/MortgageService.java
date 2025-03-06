package com.bank.mortgage.service;

import com.bank.mortgage.exception.HomeValueExceededException;
import com.bank.mortgage.exception.InterestRateNotFoundException;
import com.bank.mortgage.exception.MaxLoanExceededException;
import com.bank.mortgage.exception.MortgageCalculationException;
import com.bank.mortgage.model.InterestRate;
import com.bank.mortgage.model.MortgageCheckRequest;
import com.bank.mortgage.model.MortgageCheckResponse;
import com.bank.mortgage.strategy.MortgageCalculatorStrategy;
import com.bank.mortgage.util.MortgageConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MortgageService {
    private static final Logger logger = LoggerFactory.getLogger(MortgageService.class);

    private final InterestRateService interestRateService;
    private final MortgageCalculatorStrategy mortgageCalculator;

    public MortgageCheckResponse calculateMortgage(MortgageCheckRequest request) {
        try {
            logger.info("Calculating mortgage for request: {}", request);

            validateLoanValue(request);

            InterestRate interestRate = interestRateService.getInterestRate(request.getMaturityPeriod());
            double monthlyCost = mortgageCalculator.calculateMonthlyCost(
                    request.getLoanValue(),
                    interestRate.getInterestRate(),
                    request.getMaturityPeriod()
            );
            MortgageCheckResponse response = new MortgageCheckResponse();
            response.setFeasible(true);
            response.setMonthlyCost(monthlyCost);

            logger.info("Mortgage calculated successfully for request: {}", request);
            return response;
        } catch (MaxLoanExceededException | HomeValueExceededException | InterestRateNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error calculating mortgage for request: {}", request, e);
            throw new MortgageCalculationException(e);
        }
    }

    private void validateLoanValue(MortgageCheckRequest request) {
        double maxLoan = request.getIncome() * MortgageConstants.MAX_LOAN_MULTIPLIER;
        if (request.getLoanValue() > maxLoan) {
            logger.info("Loan value exceeds maximum loan limit for request: {}", request);
            throw new MaxLoanExceededException();
        }
        if (request.getLoanValue() > request.getHomeValue()) {
            logger.info("Loan value exceeds home value for request: {}", request);
            throw new HomeValueExceededException();
        }
    }
}
