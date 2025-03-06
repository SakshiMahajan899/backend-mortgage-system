package com.bank.mortgage.service;

import com.bank.mortgage.exception.InterestRateNotFoundException;
import com.bank.mortgage.model.InterestRate;
import com.bank.mortgage.model.InterestRateResponse;
import com.bank.mortgage.repository.InterestRateRepository;
import com.bank.mortgage.util.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestRateService {
    private static final Logger logger = LoggerFactory.getLogger(InterestRateService.class);

    private final InterestRateRepository interestRateRepository;

    /**
     * Finds the interest rate for a given maturity period.
     *
     * @param maturityPeriod The maturity period of the mortgage.
     * @return The interest rate entity.
     * @throws InterestRateNotFoundException if no interest rate is found.
     */
    public InterestRate getInterestRate(int maturityPeriod) {
        InterestRate interestRateEntity = interestRateRepository.findByMaturityPeriod(maturityPeriod);
        if (interestRateEntity == null) {
            logger.info("No interest rate found for maturity period: {}", maturityPeriod);
            throw new InterestRateNotFoundException(ExceptionMessage.INTEREST_RATE_FETCH_ERROR.getMessage());
        }
        return interestRateEntity;
    }

    /**
     * Fetches a list of all interest rates.
     *
     * @return List of interest rate responses.
     */
    @Cacheable("interestRates")
    public List<InterestRateResponse> getAllInterestRates() {
        try {
            return interestRateRepository.findAll().stream()
                    .map(rate -> new InterestRateResponse(rate.getMaturityPeriod(), rate.getInterestRate(), rate.getLastUpdate()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching interest rates", e);
            throw new InterestRateNotFoundException(ExceptionMessage.INTEREST_RATE_FETCH_ERROR.getMessage());
        }
    }
}
