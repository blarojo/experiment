package org.acme.counter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CounterServiceTest {

    private CounterService service;

    @BeforeEach
    void setUp() {
        service = new CounterService();
    }

    @Test
    void testInitialValueIsZero() {
        assertEquals(0, service.get());
    }

    @Test
    void testIncrement() {
        assertEquals(1, service.increment());
        assertEquals(2, service.increment());
    }

    @Test
    void testDecrement() {
        assertEquals(-1, service.decrement());
    }

    @Test
    void testReset() {
        service.increment();
        service.increment();
        assertEquals(0, service.reset());
        assertEquals(0, service.get());
    }
}
