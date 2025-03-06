package com.bank.mortgage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MortgageCheckResponse {
    private boolean isFeasible;
    private double monthlyCost;

}
