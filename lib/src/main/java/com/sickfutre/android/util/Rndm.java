package com.sickfutre.android.util;

import java.util.Random;

/**
 * Created by Alex Dzeshko on 08-Feb-16.
 */
public class Rndm {
    private static Rndm sInstance;
    private Random random;

    private static Rndm getInstance() {
        if (sInstance == null) {
            sInstance = new Rndm();
        }
        return sInstance;
    }

    private Rndm() {
        random = new Random();
    }

    public static int inRange(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) end - (long) start + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * getRandomStatic().nextDouble());
        return (int) (fraction + start);
    }

    public static double gaussian(double mean, double variance) {
        return mean + getRandomStatic().nextGaussian() * variance;
    }

    private static Random getRandomStatic() {
        return getInstance().getRandom();
    }

    public static <T> T getRandomFrom(T[] objects) {
        return objects[inRange(0, objects.length - 1)];
    }

    private Random getRandom() {
        return random;
    }
}
