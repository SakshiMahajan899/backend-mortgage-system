package com.bank.mortgage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MaxLoanExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxLoanExceededException(MaxLoanExceededException ex, WebRequest request) {
        logger.error("MaxLoanExceededException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "MAX_LOAN_EXCEEDED", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HomeValueExceededException.class)
    public ResponseEntity<ErrorResponse> handleHomeValueExceededException(HomeValueExceededException ex, WebRequest request) {
        logger.error("HomeValueExceededException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "HOME_VALUE_EXCEEDED", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InterestRateNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInterestRateNotFoundException(InterestRateNotFoundException ex, WebRequest request) {
        logger.error("InterestRateNotFoundException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "INTEREST_RATE_NOT_FOUND", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MortgageCalculationException.class)
    public ResponseEntity<ErrorResponse> handleMortgageCalculationException(MortgageCalculationException ex, WebRequest request) {
        logger.error("MortgageCalculationException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "MORTGAGE_CALCULATION_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MortgageException.class)
    public ResponseEntity<ErrorResponse> handleMortgageException(MortgageException ex, WebRequest request) {
        logger.error("MortgageException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "MORTGAGE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred.", "GENERIC_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
