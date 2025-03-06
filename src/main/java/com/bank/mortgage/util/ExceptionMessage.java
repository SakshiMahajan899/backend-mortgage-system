package com.bank.mortgage.util;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ExceptionMessage {
    MAX_LOAN_EXCEEDED("Loan value exceeds maximum loan limit."),
    HOME_VALUE_EXCEEDED("Loan value exceeds home value."),
    INTEREST_RATE_NOT_FOUND("No interest rate found for the given maturity period."),
    MORTGAGE_CALCULATION_ERROR("An error occurred while calculating the mortgage."),
    INTEREST_RATE_FETCH_ERROR("An error occurred while fetching interest rates.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

}