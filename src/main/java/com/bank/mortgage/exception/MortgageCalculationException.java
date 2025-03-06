package com.bank.mortgage.exception;

import com.bank.mortgage.util.ExceptionMessage;

public class MortgageCalculationException extends RuntimeException {
    public MortgageCalculationException() {
        super(ExceptionMessage.MORTGAGE_CALCULATION_ERROR.getMessage());
    }

    public MortgageCalculationException(Throwable cause) {
        super(ExceptionMessage.MORTGAGE_CALCULATION_ERROR.getMessage(), cause);
    }

    public MortgageCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
