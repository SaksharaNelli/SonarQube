package com.example.bank.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class InterestCalculatorTest {

    @Test
    void calculatesOneMonthOfInterest() {
        BigDecimal interest = InterestCalculator.monthlyInterest(new BigDecimal("10000.00"), new BigDecimal("6.0"));
        assertEquals(new BigDecimal("50.00"), interest);
    }

    @Test
    void roundsToTwoDecimalPlaces() {
        BigDecimal interest = InterestCalculator.monthlyInterest(new BigDecimal("1234.56"), new BigDecimal("3.5"));
        assertEquals(new BigDecimal("3.60"), interest);
    }

    @Test
    void zeroRateGivesZeroInterest() {
        BigDecimal interest = InterestCalculator.monthlyInterest(new BigDecimal("500.00"), BigDecimal.ZERO);
        assertEquals(0, BigDecimal.ZERO.compareTo(interest));
    }

    @Test
    void negativeInputsAreRejected() {
        BigDecimal ten = BigDecimal.TEN;
        BigDecimal negative = new BigDecimal("-1");
        assertThrows(IllegalArgumentException.class, () -> InterestCalculator.monthlyInterest(negative, ten));
        assertThrows(IllegalArgumentException.class, () -> InterestCalculator.monthlyInterest(ten, negative));
    }
}
