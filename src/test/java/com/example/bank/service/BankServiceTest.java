package com.example.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.model.Account;
import com.example.bank.model.TransactionType;
import com.example.bank.repository.InMemoryAccountRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankServiceTest {

    private BankService bank;
    private Account alice;
    private Account bob;

    @BeforeEach
    void setUp() {
        bank = new BankService(new InMemoryAccountRepository());
        alice = bank.createAccount("Alice", new BigDecimal("1000.00"));
        bob = bank.createAccount("Bob", new BigDecimal("200.00"));
    }

    private static void assertAmount(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual), "expected " + expected + " but was " + actual);
    }

    @Test
    void createAccountGeneratesUniqueNumbersAndTrimsName() {
        Account carol = bank.createAccount("  Carol  ", BigDecimal.ZERO);

        assertEquals("Carol", carol.getHolderName());
        assertTrue(carol.getAccountNumber().startsWith("ACC"));
        assertNotEquals(alice.getAccountNumber(), carol.getAccountNumber());
        assertEquals(3, bank.getAllAccounts().size());
    }

    @Test
    void createAccountValidatesInput() {
        assertThrows(IllegalArgumentException.class, () -> bank.createAccount(" ", BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () -> bank.createAccount("Dave", new BigDecimal("-5")));
    }

    @Test
    void depositIncreasesBalance() {
        bank.deposit(alice.getAccountNumber(), new BigDecimal("250.00"));
        assertAmount("1250.00", bank.getBalance(alice.getAccountNumber()));
    }

    @Test
    void depositRejectsNonPositiveAmount() {
        String number = alice.getAccountNumber();
        assertThrows(IllegalArgumentException.class, () -> bank.deposit(number, BigDecimal.ZERO));
    }

    @Test
    void withdrawDecreasesBalance() {
        bank.withdraw(alice.getAccountNumber(), new BigDecimal("400.00"));
        assertAmount("600.00", bank.getBalance(alice.getAccountNumber()));
    }

    @Test
    void withdrawingExactBalanceIsAllowed() {
        bank.withdraw(bob.getAccountNumber(), new BigDecimal("200.00"));
        assertAmount("0.00", bank.getBalance(bob.getAccountNumber()));
    }

    @Test
    void withdrawMoreThanBalanceFails() {
        String number = bob.getAccountNumber();
        assertThrows(InsufficientFundsException.class, () -> bank.withdraw(number, new BigDecimal("200.01")));
        assertAmount("200.00", bank.getBalance(number));
    }

    @Test
    void transferMovesMoneyBetweenAccounts() {
        bank.transfer(alice.getAccountNumber(), bob.getAccountNumber(), new BigDecimal("300.00"));

        assertAmount("700.00", bank.getBalance(alice.getAccountNumber()));
        assertAmount("500.00", bank.getBalance(bob.getAccountNumber()));
    }

    @Test
    void transferToSameAccountIsRejected() {
        String number = alice.getAccountNumber();
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(number, number, BigDecimal.TEN));
    }

    @Test
    void transferToUnknownAccountLeavesSourceUntouched() {
        String number = alice.getAccountNumber();
        assertThrows(AccountNotFoundException.class, () -> bank.transfer(number, "ACC9999", BigDecimal.TEN));
        assertAmount("1000.00", bank.getBalance(number));
    }

    @Test
    void transferWithInsufficientFundsChangesNothing() {
        String from = bob.getAccountNumber();
        String to = alice.getAccountNumber();
        assertThrows(InsufficientFundsException.class, () -> bank.transfer(from, to, new BigDecimal("999.00")));
        assertAmount("200.00", bank.getBalance(from));
        assertAmount("1000.00", bank.getBalance(to));
    }

    @Test
    void transferRejectsNonPositiveAmount() {
        String from = alice.getAccountNumber();
        String to = bob.getAccountNumber();
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(from, to, BigDecimal.ZERO));
    }

    @Test
    void unknownAccountThrowsNotFound() {
        assertThrows(AccountNotFoundException.class, () -> bank.getBalance("NOPE"));
        assertThrows(AccountNotFoundException.class, () -> bank.deposit("NOPE", BigDecimal.ONE));
    }

    @Test
    void monthlyInterestIsCreditedToTheAccount() {
        BigDecimal interest = bank.applyMonthlyInterest(alice.getAccountNumber(), new BigDecimal("12"));

        assertAmount("10.00", interest);
        assertAmount("1010.00", bank.getBalance(alice.getAccountNumber()));
    }

    @Test
    void zeroInterestDoesNotCreateATransaction() {
        Account empty = bank.createAccount("Eve", BigDecimal.ZERO);

        BigDecimal interest = bank.applyMonthlyInterest(empty.getAccountNumber(), new BigDecimal("5"));

        assertAmount("0.00", interest);
        assertTrue(bank.getStatement(empty.getAccountNumber()).isEmpty());
    }

    @Test
    void statementListsTransactionsInOrder() {
        bank.deposit(alice.getAccountNumber(), new BigDecimal("100.00"));
        bank.withdraw(alice.getAccountNumber(), new BigDecimal("30.00"));

        var statement = bank.getStatement(alice.getAccountNumber());

        assertEquals(2, statement.size());
        assertEquals(TransactionType.DEPOSIT, statement.get(0).type());
        assertEquals(TransactionType.WITHDRAWAL, statement.get(1).type());
    }
}
