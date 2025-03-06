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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class MortgageServiceTest {

    @Mock
    private InterestRateService interestRateService;

    @Mock
    private MortgageCalculatorStrategy mortgageCalculator;

    @InjectMocks
    private MortgageService mortgageService;

    private MortgageCheckRequest request;
    private InterestRate interestRate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MortgageCheckRequest();
        request.setIncome(750000d);
        request.setMaturityPeriod(30);
        request.setLoanValue(25000d);
        request.setHomeValue(300000d);

        interestRate = new InterestRate();
        interestRate.setMaturityPeriod(30);
        interestRate.setInterestRate(5.0);
    }

    @Test
    void whenCalculateMortgageWithValidRequest_thenReturnsMortgageCheckResponse() {
        when(interestRateService.getInterestRate(anyInt())).thenReturn(interestRate);
        when(mortgageCalculator.calculateMonthlyCost(anyDouble(), anyDouble(), anyInt())).thenReturn(1342.05);

        MortgageCheckResponse response = mortgageService.calculateMortgage(request);

        assertNotNull(response);
        assertTrue(response.isFeasible());
        assertEquals(1342.05, response.getMonthlyCost());
    }

    @Test
    void whenCalculateMortgageWithMaxLoanExceeded_thenThrowsMaxLoanExceededException() {

        request.setLoanValue(350000000d);
        MaxLoanExceededException exception = assertThrows(
                MaxLoanExceededException.class,
                () -> mortgageService.calculateMortgage(request)
        );

        assertNotNull(exception);
    }

    @Test
    void whenCalculateMortgageWithHomeValueExceeded_thenThrowsHomeValueExceededException() {
        request.setLoanValue(800000d);

        HomeValueExceededException exception = assertThrows(
                HomeValueExceededException.class,
                () -> mortgageService.calculateMortgage(request)
        );

        assertNotNull(exception);
    }

    @Test
    void whenCalculateMortgageWithInterestRateNotFound_thenThrowsInterestRateNotFoundException() {
        when(interestRateService.getInterestRate(anyInt())).thenThrow(new InterestRateNotFoundException("Interest rate not found"));

        InterestRateNotFoundException exception = assertThrows(
                InterestRateNotFoundException.class,
                () -> mortgageService.calculateMortgage(request)
        );

        assertEquals("Interest rate not found", exception.getMessage());
    }

    @Test
    void whenCalculateMortgageWithUnexpectedException_thenThrowsMortgageCalculationException() {
        when(interestRateService.getInterestRate(anyInt())).thenReturn(interestRate);
        when(mortgageCalculator.calculateMonthlyCost(anyDouble(), anyDouble(), anyInt())).thenThrow(new RuntimeException("Unexpected error"));

        MortgageCalculationException exception = assertThrows(
                MortgageCalculationException.class,
                () -> mortgageService.calculateMortgage(request)
        );

        assertNotNull(exception);
        assertEquals("java.lang.RuntimeException: Unexpected error", exception.getCause().toString());
    }
}
