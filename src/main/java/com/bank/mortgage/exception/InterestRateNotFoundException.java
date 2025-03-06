package com.bank.mortgage.exception;

import com.bank.mortgage.util.ExceptionMessage;

public class InterestRateNotFoundException extends RuntimeException {
    public InterestRateNotFoundException() {
        super(ExceptionMessage.INTEREST_RATE_NOT_FOUND.getMessage());
    }
}
