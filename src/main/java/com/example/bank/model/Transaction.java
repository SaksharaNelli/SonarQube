package com.example.bank.model;

import java.math.BigDecimal;

/**
 * Immutable record of a single balance change.
 */
public record Transaction(TransactionType type, BigDecimal amount, BigDecimal balanceAfter) {
}
