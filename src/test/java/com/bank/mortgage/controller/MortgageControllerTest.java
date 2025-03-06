package com.bank.mortgage.controller;

import com.bank.mortgage.exception.ErrorResponse;
import com.bank.mortgage.exception.HomeValueExceededException;
import com.bank.mortgage.exception.InterestRateNotFoundException;
import com.bank.mortgage.exception.MaxLoanExceededException;
import com.bank.mortgage.model.InterestRateResponse;
import com.bank.mortgage.model.MortgageCheckRequest;
import com.bank.mortgage.model.MortgageCheckResponse;
import com.bank.mortgage.service.InterestRateService;
import com.bank.mortgage.service.MortgageService;
import com.bank.mortgage.util.ExceptionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static com.bank.mortgage.util.ExceptionMessage.HOME_VALUE_EXCEEDED;
import static com.bank.mortgage.util.ExceptionMessage.MAX_LOAN_EXCEEDED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(MortgageController.class)
class MortgageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MortgageService mortgageService;

    @MockBean
    private InterestRateService interestRateService;

    @Autowired
    private ObjectMapper objectMapper;

    private MortgageCheckRequest request;
    private MortgageCheckResponse response;
    private InterestRateResponse interestRateResponse;

    @BeforeEach
    void setUp() {
        request = new MortgageCheckRequest();
        request.setIncome(75000d);
        request.setMaturityPeriod(30);
        request.setLoanValue(250000d);
        request.setHomeValue(300000d);

        response = new MortgageCheckResponse();
        response.setFeasible(true);
        response.setMonthlyCost(1342.05);

        interestRateResponse = new InterestRateResponse();
        interestRateResponse.setMaturityPeriod(10);
        interestRateResponse.setInterestRate(5.0);
        interestRateResponse.setLastUpdate(Timestamp.from(Instant.parse("2025-03-03T12:00:00.000Z")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getInterestRates_whenValidRequest_shouldReturnInterestRates() throws Exception {
        List<InterestRateResponse> interestRates = Collections.singletonList(interestRateResponse);

        when(interestRateService.getAllInterestRates()).thenReturn(interestRates);

        mockMvc.perform(get("/api/v1/interest-rates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].maturityPeriod").value(10))
                .andExpect(jsonPath("$[0].interestRate").value(5.0));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void mortgageCheck_whenValidRequest_shouldReturnFeasibleResponse() throws Exception {
        when(mortgageService.calculateMortgage(any(MortgageCheckRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.feasible").value(true))
                .andExpect(jsonPath("$.monthlyCost").value(1342.05));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void mortgageCheck_whenMaxLoanExceeded_shouldReturnBadRequest() throws Exception {
        when(mortgageService.calculateMortgage(any(MortgageCheckRequest.class)))
                .thenThrow(new MaxLoanExceededException());

        ErrorResponse errorResponse = new ErrorResponse("Max loan exceeded", "MAX_LOAN_EXCEEDED", HttpStatus.BAD_REQUEST);

        mockMvc.perform(post("/api/v1/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(MAX_LOAN_EXCEEDED.getMessage()))
                .andExpect(jsonPath("$.code").value(errorResponse.getCode()))
                .andExpect(jsonPath("$.status").value(errorResponse.getStatus().name()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void mortgageCheck_whenHomeValueExceeded_shouldReturnBadRequest() throws Exception {
        when(mortgageService.calculateMortgage(any(MortgageCheckRequest.class)))
                .thenThrow(new HomeValueExceededException());

        ErrorResponse errorResponse = new ErrorResponse("Home value exceeded", "HOME_VALUE_EXCEEDED", HttpStatus.BAD_REQUEST);

        mockMvc.perform(post("/api/v1/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(HOME_VALUE_EXCEEDED.getMessage()))
                .andExpect(jsonPath("$.code").value(errorResponse.getCode()))
                .andExpect(jsonPath("$.status").value(errorResponse.getStatus().name()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void mortgageCheck_whenInterestRateNotFound_shouldReturnNotFound() throws Exception {
        when(mortgageService.calculateMortgage(any(MortgageCheckRequest.class)))
                .thenThrow(new InterestRateNotFoundException(ExceptionMessage.INTEREST_RATE_FETCH_ERROR.getMessage()));

        ErrorResponse errorResponse = new ErrorResponse(ExceptionMessage.INTEREST_RATE_FETCH_ERROR.getMessage(), "INTEREST_RATE_NOT_FOUND", HttpStatus.NOT_FOUND);

        mockMvc.perform(post("/api/v1/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorResponse.getMessage()))
                .andExpect(jsonPath("$.code").value(errorResponse.getCode()))
                .andExpect(jsonPath("$.status").value(errorResponse.getStatus().name()));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getInterestRates_whenServiceThrowsException_shouldReturnInternalServerError() throws Exception {
        when(interestRateService.getAllInterestRates()).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/v1/interest-rates")
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

}
