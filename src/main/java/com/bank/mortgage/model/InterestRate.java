package com.bank.mortgage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Represents the mortgage interest rate for database operations.
 */

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class InterestRate {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private int maturityPeriod;
    private double interestRate;
    private Timestamp lastUpdate;
}
