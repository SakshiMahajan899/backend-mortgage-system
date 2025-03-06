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
    public ResponseEntity<String> handleMaxLoanExceededException(MaxLoanExceededException ex, WebRequest request) {
        logger.error("MaxLoanExceededException: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HomeValueExceededException.class)
    public ResponseEntity<String> handleHomeValueExceededException(HomeValueExceededException ex, WebRequest request) {
        logger.error("HomeValueExceededException: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InterestRateNotFoundException.class)
    public ResponseEntity<String> handleInterestRateNotFoundException(InterestRateNotFoundException ex, WebRequest request) {
        logger.error("InterestRateNotFoundException: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MortgageCalculationException.class)
    public ResponseEntity<String> handleMortgageCalculationException(MortgageCalculationException ex, WebRequest request) {
        logger.error("MortgageCalculationException: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MortgageException.class)
    public ResponseEntity<String> handleMortgageException(MortgageException ex, WebRequest request) {
        logger.error("MortgageException: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Exception: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
