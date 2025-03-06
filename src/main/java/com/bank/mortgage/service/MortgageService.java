package com.bank.mortgage.service;

import com.bank.mortgage.exception.*;
import com.bank.mortgage.model.InterestRate;
import com.bank.mortgage.model.InterestRateResponse;
import com.bank.mortgage.model.MortgageCheckRequest;
import com.bank.mortgage.model.MortgageCheckResponse;
import com.bank.mortgage.repository.InterestRateRepository;
import com.bank.mortgage.util.ExceptionMessage;
import com.bank.mortgage.util.MortgageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for handling mortgage-related operations.
 */
@Service
@RequiredArgsConstructor
public class MortgageService {
    private static final Logger logger = LoggerFactory.getLogger(MortgageService.class);

    private final InterestRateRepository interestRateRepository;

    /**
     * Calculates the feasibility and monthly cost of a mortgage.
     *
     * @param request The mortgage check request containing income, loan value, home value, and maturity period.
     * @return The mortgage check response containing feasibility status and monthly cost.
     * @throws MaxLoanExceededException Thrown when the loan value exceeds the maximum loan limit.
     * @throws HomeValueExceededException Thrown when the loan value exceeds the home value.
     * @throws InterestRateNotFoundException Thrown when no interest rate is found for the given maturity period.
     * @throws MortgageCalculationException Thrown when an error occurs during mortgage calculation.
     */
    public MortgageCheckResponse calculateMortgage(MortgageCheckRequest request) {
        try {
            logger.info("Calculating mortgage for request: {}", request);

            validateLoanValue(request);

            InterestRate interestRateEntity = findInterestRate(request.getMaturityPeriod());
            double monthlyCost = calculateMonthlyCost(request.getLoanValue(), interestRateEntity.getInterestRate(), request.getMaturityPeriod());

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

    /**
     * Validates the loan value against the maximum loan limit and home value.
     *
     * @param request The mortgage check request containing loan value and home value.
     * @throws MaxLoanExceededException Thrown when the loan value exceeds the maximum loan limit.
     * @throws HomeValueExceededException Thrown when the loan value exceeds the home value.
     */
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

    /**
     * Finds the interest rate for a given maturity period.
     *
     * @param maturityPeriod The maturity period of the mortgage.
     * @return The interest rate entity for the given maturity period.
     * @throws InterestRateNotFoundException Thrown when no interest rate is found for the given maturity period.
     */
    private InterestRate findInterestRate(int maturityPeriod) {
        InterestRate interestRateEntity = interestRateRepository.findByMaturityPeriod(maturityPeriod);
        if (interestRateEntity == null) {
            logger.info("No interest rate found for maturity period: {}", maturityPeriod);
            throw new InterestRateNotFoundException();
        }
        return interestRateEntity;
    }

    /**
     * Calculates the monthly mortgage cost.
     *
     * @param loanValue The value of the loan.
     * @param interestRate The interest rate of the loan.
     * @param maturityPeriod The maturity period of the loan in years.
     * @return The monthly mortgage cost.
     */
    private double calculateMonthlyCost(double loanValue, double interestRate, int maturityPeriod) {
        double monthlyRate = interestRate / 100 / MortgageConstants.MONTHS_IN_YEAR;
        double numberOfPayments = maturityPeriod * MortgageConstants.MONTHS_IN_YEAR;
        return loanValue * monthlyRate / (1 - Math.pow(1 + monthlyRate, -numberOfPayments));
    }

    /**
     * Fetches the list of interest rates.
     *
     * @return The list of interest rate responses.
     * @throws MortgageCalculationException Thrown when an error occurs while fetching interest rates.
     */
    public List<InterestRateResponse> getInterestRates() {
        try {
            return interestRateRepository.findAll().stream()
                    .map(rate -> new InterestRateResponse(rate.getMaturityPeriod(), rate.getInterestRate(), rate.getLastUpdate()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching interest rates", e);
            throw new MortgageCalculationException(ExceptionMessage.INTEREST_RATE_FETCH_ERROR.getMessage(), e);
        }
    }
}
