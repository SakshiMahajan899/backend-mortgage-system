package com.bank.mortgage.strategy;

public interface MortgageCalculatorStrategy {
    double calculateMonthlyCost(double loanValue, double interestRate, int maturityPeriod);
}