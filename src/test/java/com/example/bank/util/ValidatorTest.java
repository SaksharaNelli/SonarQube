package com.example.bank.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    @Test
    void requireTextRejectsNullAndBlank() {
        assertThrows(IllegalArgumentException.class, () -> Validator.requireText(null, "Name"));
        assertThrows(IllegalArgumentException.class, () -> Validator.requireText("   ", "Name"));
        assertDoesNotThrow(() -> Validator.requireText("Alice", "Name"));
    }

    @Test
    void requirePositiveRejectsNullZeroAndNegative() {
        assertThrows(IllegalArgumentException.class, () -> Validator.requirePositive(null, "Amount"));
        assertThrows(IllegalArgumentException.class, () -> Validator.requirePositive(BigDecimal.ZERO, "Amount"));
        assertThrows(IllegalArgumentException.class, () -> Validator.requirePositive(new BigDecimal("-1"), "Amount"));
        assertDoesNotThrow(() -> Validator.requirePositive(BigDecimal.ONE, "Amount"));
    }

    @Test
    void requireNonNegativeAllowsZeroButNotNegativeOrNull() {
        assertThrows(IllegalArgumentException.class, () -> Validator.requireNonNegative(null, "Amount"));
        assertThrows(IllegalArgumentException.class, () -> Validator.requireNonNegative(new BigDecimal("-0.01"), "Amount"));
        assertDoesNotThrow(() -> Validator.requireNonNegative(BigDecimal.ZERO, "Amount"));
    }
}
