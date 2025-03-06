package com.bank.mortgage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterestRateResponse {
    private int maturityPeriod;
    private double interestRate;
    private Timestamp lastUpdate;
}
