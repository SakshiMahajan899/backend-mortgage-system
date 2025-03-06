package com.bank.mortgage.repository;

import com.bank.mortgage.model.InterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InterestRateRepository extends JpaRepository<InterestRate, UUID> {
    InterestRate findByMaturityPeriod(int maturityPeriod);
}
