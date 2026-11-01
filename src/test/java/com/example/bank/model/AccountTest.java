package com.example.bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("ACC1", "Alice", new BigDecimal("100.00"));
    }

    @Test
    void gettersReturnConstructorValues() {
        assertEquals("ACC1", account.getAccountNumber());
        assertEquals("Alice", account.getHolderName());
        assertEquals(0, new BigDecimal("100.00").compareTo(account.getBalance()));
    }

    @Test
    void creditIncreasesBalanceAndRecordsDeposit() {
        account.credit(new BigDecimal("50.00"));

        assertEquals(0, new BigDecimal("150.00").compareTo(account.getBalance()));
        Transaction last = account.getTransactions().get(0);
        assertEquals(TransactionType.DEPOSIT, last.type());
        assertEquals(0, new BigDecimal("50.00").compareTo(last.amount()));
        assertEquals(0, new BigDecimal("150.00").compareTo(last.balanceAfter()));
    }

    @Test
    void debitDecreasesBalanceAndRecordsWithdrawal() {
        account.debit(new BigDecimal("40.00"));

        assertEquals(0, new BigDecimal("60.00").compareTo(account.getBalance()));
        assertEquals(TransactionType.WITHDRAWAL, account.getTransactions().get(0).type());
    }

    @Test
    void transactionListCannotBeModifiedFromOutside() {
        assertThrows(UnsupportedOperationException.class, () -> account.getTransactions().clear());
    }

    @Test
    void nullArgumentsAreRejected() {
        BigDecimal zero = BigDecimal.ZERO;
        assertThrows(NullPointerException.class, () -> new Account(null, "Alice", zero));
        assertThrows(NullPointerException.class, () -> new Account("ACC1", null, zero));
        assertThrows(NullPointerException.class, () -> new Account("ACC1", "Alice", null));
    }
}
