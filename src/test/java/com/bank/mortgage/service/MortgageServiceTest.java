package com.bank.mortgage.service;

import com.bank.mortgage.exception.*;
import com.bank.mortgage.model.InterestRate;
import com.bank.mortgage.model.InterestRateResponse;
import com.bank.mortgage.model.MortgageCheckRequest;
import com.bank.mortgage.model.MortgageCheckResponse;
import com.bank.mortgage.repository.InterestRateRepository;
import com.bank.mortgage.util.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageServiceTest {

    @Mock
    private InterestRateRepository interestRateRepository;

    @InjectMocks
    private MortgageService mortgageService;

    private MortgageCheckRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new MortgageCheckRequest();
        validRequest.setIncome(75000d);
        validRequest.setMaturityPeriod(30);
        validRequest.setLoanValue(250000d);
        validRequest.setHomeValue(300000d);
    }

    @Test
    void calculateMortgage_ShouldReturnFeasibleResponse() {
        InterestRate interestRate = new InterestRate();
        interestRate.setMaturityPeriod(30);
        interestRate.setInterestRate(5.0);

        when(interestRateRepository.findByMaturityPeriod(30)).thenReturn(interestRate);

        MortgageCheckResponse response = mortgageService.calculateMortgage(validRequest);

        assertTrue(response.isFeasible());
        assertEquals(1342.05, response.getMonthlyCost(), 0.01);
    }

    @Test
    void calculateMortgage_ShouldThrowMaxLoanExceededException() {
        validRequest.setLoanValue(350000d);

        MaxLoanExceededException exception = assertThrows(MaxLoanExceededException.class,
                () -> mortgageService.calculateMortgage(validRequest));

        assertEquals("Loan value exceeds maximum loan limit.", exception.getMessage());
    }

    @Test
    void calculateMortgage_ShouldThrowInterestRateNotFoundException() {
        when(interestRateRepository.findByMaturityPeriod(30)).thenReturn(null);

        InterestRateNotFoundException exception = assertThrows(InterestRateNotFoundException.class,
                () -> mortgageService.calculateMortgage(validRequest));

        assertEquals("No interest rate found for the given maturity period.", exception.getMessage());
    }

    @Test
    void getInterestRates_ShouldReturnListOfInterestRates() {
        InterestRate interestRate = new InterestRate();
        interestRate.setMaturityPeriod(10);
        interestRate.setInterestRate(5.0);

        when(interestRateRepository.findAll()).thenReturn(Collections.singletonList(interestRate));

        List<InterestRateResponse> rates = mortgageService.getInterestRates();

        assertEquals(1, rates.size());
        assertEquals(10, rates.get(0).getMaturityPeriod());
        assertEquals(5.0, rates.get(0).getInterestRate());
    }

    @Test
    void getInterestRates_ShouldThrowMortgageCalculationException() {
        when(interestRateRepository.findAll()).thenThrow(new RuntimeException());

        MortgageCalculationException exception = assertThrows(MortgageCalculationException.class,
                () -> mortgageService.getInterestRates());

        assertEquals(ExceptionMessage.INTEREST_RATE_FETCH_ERROR.getMessage(), exception.getMessage());
    }
}

