package com.bank.mortgage.strategy;

import com.bank.mortgage.util.MortgageConstants;
import org.springframework.stereotype.Component;

@Component
public class StandardMortgageCalculator implements MortgageCalculatorStrategy {

    @Override
    public double calculateMonthlyCost(double loanValue, double interestRate, int maturityPeriod) {
        double monthlyRate = interestRate / 100 / MortgageConstants.MONTHS_IN_YEAR;
        double numberOfPayments = maturityPeriod * MortgageConstants.MONTHS_IN_YEAR;
        return loanValue * monthlyRate / (1 - Math.pow(1 + monthlyRate, -numberOfPayments));
    }
}