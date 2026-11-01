package com.example.bank.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InsufficientFundsException(String accountNumber, BigDecimal requested, BigDecimal available) {
        super("Insufficient funds in " + accountNumber
                + ": requested " + requested + ", available " + available);
    }
}
