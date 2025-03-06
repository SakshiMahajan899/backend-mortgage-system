package com.bank.mortgage.exception;

import com.bank.mortgage.util.ExceptionMessage;

public class HomeValueExceededException extends RuntimeException {
    public HomeValueExceededException() {
        super(ExceptionMessage.HOME_VALUE_EXCEEDED.getMessage());
    }
}
