package com.example.bank.service;

import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;
import com.example.bank.util.InterestCalculator;
import com.example.bank.util.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Core business logic: open accounts, deposit, withdraw, transfer, add interest.
 */
public class BankService {

    private final AccountRepository repository;
    private final AtomicInteger sequence = new AtomicInteger(1000);

    public BankService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(String holderName, BigDecimal openingBalance) {
        Validator.requireText(holderName, "Holder name");
        Validator.requireNonNegative(openingBalance, "Opening balance");
        Account account = new Account("ACC" + sequence.incrementAndGet(), holderName.trim(), openingBalance);
        repository.save(account);
        return account;
    }

    public void deposit(String accountNumber, BigDecimal amount) {
        Validator.requirePositive(amount, "Deposit amount");
        findOrThrow(accountNumber).credit(amount);
    }

    public void withdraw(String accountNumber, BigDecimal amount) {
        Validator.requirePositive(amount, "Withdrawal amount");
        Account account = findOrThrow(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountNumber, amount, account.getBalance());
        }
        account.debit(amount);
    }

    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        Validator.requirePositive(amount, "Transfer amount");
        Account target = findOrThrow(toAccount);
        withdraw(fromAccount, amount);
        target.credit(amount);
    }

    public BigDecimal applyMonthlyInterest(String accountNumber, BigDecimal annualRatePercent) {
        Account account = findOrThrow(accountNumber);
        BigDecimal interest = InterestCalculator.monthlyInterest(account.getBalance(), annualRatePercent);
        if (interest.signum() > 0) {
            account.credit(interest);
        }
        return interest;
    }

    public BigDecimal getBalance(String accountNumber) {
        return findOrThrow(accountNumber).getBalance();
    }

    public List<Transaction> getStatement(String accountNumber) {
        return findOrThrow(accountNumber).getTransactions();
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    private Account findOrThrow(String accountNumber) {
        return repository.findByNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
