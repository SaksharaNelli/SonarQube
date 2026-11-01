package com.example.bank.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A bank account that keeps its balance and a history of transactions.
 */
public class Account {

    private final String accountNumber;
    private final String holderName;
    private final List<Transaction> transactions = new ArrayList<>();
    private BigDecimal balance;

    public Account(String accountNumber, String holderName, BigDecimal openingBalance) {
        this.accountNumber = Objects.requireNonNull(accountNumber, "accountNumber");
        this.holderName = Objects.requireNonNull(holderName, "holderName");
        this.balance = Objects.requireNonNull(openingBalance, "openingBalance");
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount, balance));
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
        transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount, balance));
    }
}
