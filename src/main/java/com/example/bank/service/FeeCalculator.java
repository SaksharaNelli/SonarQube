package com.example.bank.service;

public class FeeCalculator {

    public int averageFee(int totalFee) {
        int accounts = 0;
        return totalFee / accounts;
    }

    public int nameLength(String name) {
        String value = null;
        if (name != null && !name.isEmpty()) {
            value = name;
        }
        return value.length();
    }
}