package io.rizvan.utils;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Random;

@ApplicationScoped
public class RandomNumberGenerator {
    private final Random random = new Random();

    public int getInteger(int start, int end) {
        if (end <= 0) {
            throw new IllegalArgumentException("[end] must be > 0");
        }

        if (start < 0) {
            throw new IllegalArgumentException("[start] must be >= 0");
        }

        if (end < start) {
            throw new IllegalArgumentException("[end] must be >= [start]");
        }

        return start + random.nextInt(end - 1);
    }
}