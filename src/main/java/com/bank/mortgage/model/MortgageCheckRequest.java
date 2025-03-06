package com.bank.mortgage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MortgageCheckRequest {
    @NotNull(message = "Income is required")
    @Min(value = 1, message = "Income must be greater than 0")
    private Double income;

    @NotNull(message = "Loan value is required")
    @Min(value = 1, message = "Loan value must be greater than 0")
    private Double loanValue;

    @NotNull(message = "Home value is required")
    @Min(value = 1, message = "Home value must be greater than 0")
    private Double homeValue;

    @NotNull(message = "Maturity period is required")
    @Min(value = 1, message = "Maturity period must be greater than 0")
    private Integer maturityPeriod;
}
