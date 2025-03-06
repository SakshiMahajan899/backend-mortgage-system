package com.bank.mortgage.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardMortgageCalculatorTest {

    private StandardMortgageCalculator standardMortgageCalculator;

    @BeforeEach
    void setUp() {
        standardMortgageCalculator = new StandardMortgageCalculator();
    }

    @Test
    void whenCalculateMonthlyCostWithValidInputs_thenReturnsExpectedMonthlyCost() {
        double loanValue = 250000;
        double interestRate = 5;
        int maturityPeriod = 30;

        double monthlyCost = standardMortgageCalculator.calculateMonthlyCost(loanValue, interestRate, maturityPeriod);

        assertEquals(1342.05, monthlyCost, 0.01);
    }

    @Test
    void whenCalculateMonthlyCostWithZeroLoanValue_thenReturnsZero() {
        double loanValue = 0;
        double interestRate = 5;
        int maturityPeriod = 30;

        double monthlyCost = standardMortgageCalculator.calculateMonthlyCost(loanValue, interestRate, maturityPeriod);

        assertEquals(0, monthlyCost, 0.01);
    }



}
