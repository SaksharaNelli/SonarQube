package com.example.bank.repository;

import com.example.bank.model.Account;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> store = new LinkedHashMap<>();

    @Override
    public void save(Account account) {
        store.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<Account> findByNumber(String accountNumber) {
        return Optional.ofNullable(store.get(accountNumber));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }
}
