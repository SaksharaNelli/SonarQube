package com.example.bank.legacy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * INTENTIONALLY BAD CODE, used to show what SonarQube finds.
 *
 * Step 1 of the demo: scan with this class present -> Quality Gate FAILS.
 * Step 2 of the demo: delete this package (or fix it) -> Quality Gate PASSES.
 */
public class LegacyReportExporter {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin123";

    // SECURITY: SQL built by string concatenation, credentials hard-coded above.
    // RELIABILITY: connection, statement and result set are never closed.
    public String findHolder(String accountNumber) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT holder FROM accounts WHERE number = '" + accountNumber + "'");
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    // RELIABILITY: the reader is never closed (resource leak).
    public int countLines(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }
        return count;
    }

    // BUG: 'value' can still be null when it is dereferenced.
    public int safeLength(String text) {
        String value = null;
        if (text != null && text.length() > 3) {
            value = text;
        }
        return value.length();
    }

    // BUG: the denominator is always zero.
    public int averageFee(int totalFee) {
        int accounts = 0;
        return totalFee / accounts;
    }

    // CODE SMELLS: unused variable, empty catch block, printStackTrace, System.out.
    public void tryParse(String value) {
        int unused = 42;
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt(value + "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("parsed " + value);
    }

    // CODE SMELL: far too many nested branches (cognitive complexity).
    public String classifyCustomer(int age, double balance, boolean active, boolean vip, int years) {
        if (active) {
            if (vip) {
                if (balance > 100000) {
                    if (years > 10) {
                        return "PLATINUM";
                    } else if (years > 5) {
                        return "GOLD";
                    } else {
                        return "SILVER";
                    }
                } else if (balance > 50000) {
                    if (years > 10) {
                        return "GOLD";
                    } else if (age > 60) {
                        return "SENIOR-GOLD";
                    } else {
                        return "SILVER";
                    }
                } else {
                    for (int i = 0; i < years; i++) {
                        if (i > 3 && age > 40) {
                            if (balance > 10000) {
                                return "LOYAL";
                            }
                        }
                    }
                    return "BASIC-VIP";
                }
            } else {
                if (balance > 20000) {
                    if (age < 25) {
                        return "YOUTH-PLUS";
                    } else if (years > 3) {
                        return "REGULAR-PLUS";
                    }
                    return "REGULAR";
                }
                return "BASIC";
            }
        }
        return "INACTIVE";
    }

    // CODE SMELL: duplicated block (identical to formatCurrentStatement below).
    public String formatSavingsStatement(String name, double balance, double rate) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ACCOUNT STATEMENT ===\n");
        sb.append("Customer : ").append(name).append("\n");
        sb.append("Balance  : ").append(balance).append("\n");
        sb.append("Rate     : ").append(rate).append("\n");
        double interest = balance * rate / 100;
        sb.append("Interest : ").append(interest).append("\n");
        double total = balance + interest;
        sb.append("Total    : ").append(total).append("\n");
        double tax = interest * 0.1;
        sb.append("Tax      : ").append(tax).append("\n");
        double net = total - tax;
        sb.append("Net      : ").append(net).append("\n");
        sb.append("Thank you for banking with us\n");
        sb.append("=========================\n");
        return sb.toString();
    }

    public String formatCurrentStatement(String name, double balance, double rate) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ACCOUNT STATEMENT ===\n");
        sb.append("Customer : ").append(name).append("\n");
        sb.append("Balance  : ").append(balance).append("\n");
        sb.append("Rate     : ").append(rate).append("\n");
        double interest = balance * rate / 100;
        sb.append("Interest : ").append(interest).append("\n");
        double total = balance + interest;
        sb.append("Total    : ").append(total).append("\n");
        double tax = interest * 0.1;
        sb.append("Tax      : ").append(tax).append("\n");
        double net = total - tax;
        sb.append("Net      : ").append(net).append("\n");
        sb.append("Thank you for banking with us\n");
        sb.append("=========================\n");
        return sb.toString();
    }
}
