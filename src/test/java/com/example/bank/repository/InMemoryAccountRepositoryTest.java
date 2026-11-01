package com.example.bank.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bank.model.Account;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class InMemoryAccountRepositoryTest {

    @Test
    void savedAccountCanBeFoundByNumber() {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        Account account = new Account("ACC1", "Alice", BigDecimal.TEN);
        repository.save(account);

        assertSame(account, repository.findByNumber("ACC1").orElseThrow());
    }

    @Test
    void unknownNumberGivesEmptyResult() {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        assertFalse(repository.findByNumber("NOPE").isPresent());
    }

    @Test
    void findAllReturnsAccountsInInsertionOrder() {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        assertTrue(repository.findAll().isEmpty());

        repository.save(new Account("ACC1", "Alice", BigDecimal.TEN));
        repository.save(new Account("ACC2", "Bob", BigDecimal.ONE));

        List<Account> all = repository.findAll();
        assertEquals(2, all.size());
        assertEquals("ACC1", all.get(0).getAccountNumber());
        assertEquals("ACC2", all.get(1).getAccountNumber());
    }
}
