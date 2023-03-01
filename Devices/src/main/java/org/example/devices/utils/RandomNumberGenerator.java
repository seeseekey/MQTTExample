package org.example.devices.utils;

import java.util.Random;

public final class RandomNumberGenerator {

    private static Random RNG = new Random();

    private RandomNumberGenerator() {
    }

    public static boolean getRandomBoolean() {
        return RNG.nextBoolean();
    }

    public static int getRandomNumberFromRange(int from, int to) {
        return RNG.nextInt(from, to + 1);
    }

    public static double getRandomNumberFromRange(double from, double to) {
        return from + (to - from) * RNG.nextDouble();
    }
}
