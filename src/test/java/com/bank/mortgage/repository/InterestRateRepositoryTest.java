package com.bank.mortgage.repository;


import com.bank.mortgage.model.InterestRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class InterestRateRepositoryTest {

    @Autowired
    private InterestRateRepository interestRateRepository;

    private InterestRate interestRate1;
    private InterestRate interestRate2;

    @BeforeEach
    void setUp() {
        interestRate1 = new InterestRate();
        interestRate1.setId(UUID.randomUUID());
        interestRate1.setMaturityPeriod(10);
        interestRate1.setInterestRate(5.0);

        interestRate2 = new InterestRate();
        interestRate2.setId(UUID.randomUUID());
        interestRate2.setMaturityPeriod(20);
        interestRate2.setInterestRate(6.0);

        interestRateRepository.save(interestRate1);
        interestRateRepository.save(interestRate2);
    }

    @Test
    void findByMaturityPeriod_ShouldReturnNullForNonExistingMaturityPeriod() {
        InterestRate foundRate = interestRateRepository.findByMaturityPeriod(30);
        assertNull(foundRate);
    }

    @Test
    void save_ShouldSaveInterestRate() {
        InterestRate newRate = new InterestRate();
        newRate.setId(UUID.randomUUID());
        newRate.setMaturityPeriod(30);
        newRate.setInterestRate(7.0);

        InterestRate savedRate = interestRateRepository.save(newRate);

        assertNotNull(savedRate);
        assertEquals(30, savedRate.getMaturityPeriod());
        assertEquals(7.0, savedRate.getInterestRate());
    }

    @Test
    void delete_ShouldDeleteInterestRate() {
        interestRateRepository.delete(interestRate1);
        InterestRate foundRate = interestRateRepository.findById(interestRate1.getId()).orElse(null);
        assertNull(foundRate);
    }
}

