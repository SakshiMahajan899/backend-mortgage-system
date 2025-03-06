package com.bank.mortgage.exception;


import com.bank.mortgage.util.ExceptionMessage;

public class MaxLoanExceededException extends RuntimeException {
    public MaxLoanExceededException() {
        super(ExceptionMessage.MAX_LOAN_EXCEEDED.getMessage());
    }
}
