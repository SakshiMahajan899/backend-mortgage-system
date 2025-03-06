package com.bank.mortgage.service;

import com.bank.mortgage.exception.InterestRateNotFoundException;
import com.bank.mortgage.model.InterestRate;
import com.bank.mortgage.model.InterestRateResponse;
import com.bank.mortgage.repository.InterestRateRepository;
import com.bank.mortgage.util.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestRateServiceTest {

    @Mock
    private InterestRateRepository interestRateRepository;

    @InjectMocks
    private InterestRateService interestRateService;

    @Mock
    private CacheManager cacheManager;

    private InterestRate interestRate;
    private InterestRateResponse interestRateResponse;

    @BeforeEach
    void setUp() {
        interestRate = new InterestRate();
        interestRate.setMaturityPeriod(10);
        interestRate.setInterestRate(5.0);
        interestRate.setLastUpdate(Timestamp.from(Instant.parse("2025-03-03T12:00:00.000Z")));

        interestRateResponse = new InterestRateResponse();
        interestRateResponse.setMaturityPeriod(10);
        interestRateResponse.setInterestRate(5.0);
        interestRateResponse.setLastUpdate(Timestamp.from(Instant.parse("2025-03-03T12:00:00.000Z")));
    }

    @Test
    void whenGetInterestRateWithValidMaturityPeriod_thenReturnsInterestRate() {
        when(interestRateRepository.findByMaturityPeriod(anyInt())).thenReturn(interestRate);

        InterestRate result = interestRateService.getInterestRate(10);

        assertNotNull(result);
        assertEquals(10, result.getMaturityPeriod());
        assertEquals(5.0, result.getInterestRate());
        assertEquals(Timestamp.from(Instant.parse("2025-03-03T12:00:00.000Z")), result.getLastUpdate());
    }

    @Test
    void whenGetInterestRateWithInvalidMaturityPeriod_thenThrowsInterestRateNotFoundException() {
        when(interestRateRepository.findByMaturityPeriod(anyInt())).thenReturn(null);

        InterestRateNotFoundException exception = assertThrows(
                InterestRateNotFoundException.class,
                () -> interestRateService.getInterestRate(10)
        );

        assertEquals(ExceptionMessage.INTEREST_RATE_FETCH_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    void whenGetAllInterestRates_thenReturnsListOfInterestRates() {
        when(interestRateRepository.findAll()).thenReturn(Collections.singletonList(interestRate));

        List<InterestRateResponse> result = interestRateService.getAllInterestRates();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getMaturityPeriod());
        assertEquals(5.0, result.get(0).getInterestRate());
        assertEquals(Timestamp.from(Instant.parse("2025-03-03T12:00:00.000Z")), result.get(0).getLastUpdate());
    }

    @Test
    void whenGetAllInterestRatesAndRepositoryThrowsException_thenThrowsInterestRateNotFoundException() {
        when(interestRateRepository.findAll()).thenThrow(new RuntimeException("Repository error"));

        InterestRateNotFoundException exception = assertThrows(
                InterestRateNotFoundException.class,
                () -> interestRateService.getAllInterestRates()
        );

        assertEquals(ExceptionMessage.INTEREST_RATE_FETCH_ERROR.getMessage(), exception.getMessage());
    }
}
