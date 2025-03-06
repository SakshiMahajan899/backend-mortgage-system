package com.bank.mortgage.exception;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static com.bank.mortgage.util.ExceptionMessage.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    ;
    @Mock
    private WebRequest request;

//    @BeforeEach
//    void setUp() {
//        globalExceptionHandler = new GlobalExceptionHandler();
//
//            // Implement methods if required
//        };
//    }

    @Test
    void whenMaxLoanExceededException_thenReturnsBadRequest() {
        MaxLoanExceededException exception = new MaxLoanExceededException();
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleMaxLoanExceededException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MAX_LOAN_EXCEEDED.getMessage(), responseEntity.getBody().getMessage());
        assertEquals("MAX_LOAN_EXCEEDED", responseEntity.getBody().getCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getBody().getStatus());
    }

    @Test
    void whenHomeValueExceededException_thenReturnsBadRequest() {
        HomeValueExceededException exception = new HomeValueExceededException();
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleHomeValueExceededException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HOME_VALUE_EXCEEDED.getMessage(), responseEntity.getBody().getMessage());
        assertEquals("HOME_VALUE_EXCEEDED", responseEntity.getBody().getCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getBody().getStatus());
    }

    @Test
    void whenInterestRateNotFoundException_thenReturnsNotFound() {
        InterestRateNotFoundException exception = new InterestRateNotFoundException("Interest rate not found");
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleInterestRateNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Interest rate not found", responseEntity.getBody().getMessage());
        assertEquals("INTEREST_RATE_NOT_FOUND", responseEntity.getBody().getCode());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getBody().getStatus());
    }

    @Test
    void whenMortgageCalculationException_thenReturnsInternalServerError() {
        MortgageCalculationException exception = new MortgageCalculationException();
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleMortgageCalculationException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(MORTGAGE_CALCULATION_ERROR.getMessage(), responseEntity.getBody().getMessage());
        assertEquals("MORTGAGE_CALCULATION_ERROR", responseEntity.getBody().getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getBody().getStatus());
    }

    @Test
    void whenMortgageException_thenReturnsInternalServerError() {
        MortgageException exception = new MortgageException("Mortgage error");
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleMortgageException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Mortgage error", responseEntity.getBody().getMessage());
        assertEquals("MORTGAGE_ERROR", responseEntity.getBody().getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getBody().getStatus());
    }

    @Test
    void whenGenericException_thenReturnsInternalServerError() {
        Exception exception = new Exception("An unexpected error occurred.");
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An unexpected error occurred.", responseEntity.getBody().getMessage());
        assertEquals("GENERIC_ERROR", responseEntity.getBody().getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getBody().getStatus());
    }
}
