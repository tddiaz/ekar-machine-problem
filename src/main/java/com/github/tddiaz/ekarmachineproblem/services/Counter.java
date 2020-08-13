package com.github.tddiaz.ekarmachineproblem.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;


@Component
public class Counter {

    private final ReentrantLock lock = new ReentrantLock();

    @Value("${counter.initialValue}")
    private Integer value;

    @Value("${counter.minValue}")
    private Integer minValue;

    @Value("${counter.maxValue}")
    private Integer maxValue;

    public void decrement() {
        this.value--;
    }

    public void increment() {
        this.value++;
    }

    public boolean isValueReachedMax() {
        return Objects.equals(value, maxValue);
    }

    public boolean isValueReachedMin() {
        return Objects.equals(value, minValue);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        if (value < minValue || value > maxValue) {
            throw new InvalidCounterValueException(String.format("counter value cannot be less than %d or more than %d", minValue, maxValue));
        }
        this.value = value;
    }

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }
}
