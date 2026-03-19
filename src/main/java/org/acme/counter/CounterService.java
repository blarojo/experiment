package org.acme.counter;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class CounterService {

    private final AtomicInteger counter = new AtomicInteger(0);

    public int get() {
        return counter.get();
    }

    public int increment() {
        return counter.incrementAndGet();
    }

    public int decrement() {
        return counter.decrementAndGet();
    }

    public int reset() {
        counter.set(0);
        return 0;
    }
}
