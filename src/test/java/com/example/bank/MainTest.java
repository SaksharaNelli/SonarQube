package com.example.bank;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void demoRunsWithoutErrors() {
        assertDoesNotThrow(() -> Main.main(new String[0]));
    }
}
