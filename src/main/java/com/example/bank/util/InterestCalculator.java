package com.example.bank.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Simple monthly interest: balance x annual rate (%) / 100 / 12.
 */
public final class InterestCalculator {

    private static final BigDecimal PERCENT_TIMES_MONTHS = BigDecimal.valueOf(1200);

    private InterestCalculator() {
        // utility class
    }

    public static BigDecimal monthlyInterest(BigDecimal balance, BigDecimal annualRatePercent) {
        Validator.requireNonNegative(balance, "Balance");
        Validator.requireNonNegative(annualRatePercent, "Interest rate");
        return balance.multiply(annualRatePercent)
                .divide(PERCENT_TIMES_MONTHS, 2, RoundingMode.HALF_UP);
    }
}
