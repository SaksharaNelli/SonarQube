package com.example.bank;

import com.example.bank.model.Account;
import com.example.bank.repository.InMemoryAccountRepository;
import com.example.bank.service.BankService;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs a short, non-interactive demo of the banking service.
 */
public final class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private Main() {
        // entry-point class
    }

    public static void main(String[] args) {
        BankService bank = new BankService(new InMemoryAccountRepository());

        Account alice = bank.createAccount("Alice", new BigDecimal("10000.00"));
        Account bob = bank.createAccount("Bob", new BigDecimal("2500.00"));

        bank.deposit(alice.getAccountNumber(), new BigDecimal("500.00"));
        bank.withdraw(bob.getAccountNumber(), new BigDecimal("300.00"));
        bank.transfer(alice.getAccountNumber(), bob.getAccountNumber(), new BigDecimal("1000.00"));

        BigDecimal interest = bank.applyMonthlyInterest(alice.getAccountNumber(), new BigDecimal("6.0"));
        LOGGER.log(Level.INFO, "Interest credited to {0}: {1}",
                new Object[] {alice.getAccountNumber(), interest});

        for (Account account : bank.getAllAccounts()) {
            LOGGER.log(Level.INFO, "{0} | {1} | balance = {2}",
                    new Object[] {account.getAccountNumber(), account.getHolderName(), account.getBalance()});
        }
    }
}
