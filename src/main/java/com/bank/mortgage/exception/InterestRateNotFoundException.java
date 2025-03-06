package com.bank.mortgage.exception;

public class InterestRateNotFoundException extends RuntimeException {
    public InterestRateNotFoundException(String message) {
        super(message);
    }
}
