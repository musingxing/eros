package com.eros.datagen.generator.impl;

import com.eros.datagen.generator.DataGenerator;

import java.util.Random;

/**
 * Data generator to generate random integer.
 *
 * @author Eros
 * @since   2020-01-03 10:42
 */
public class RandomIntGenerator extends DataGenerator {

    private final int upper;
    private final ThreadLocal<Random> random;

    /**
     * Constructor
     *
     * @param upper    integer upper
     */
    public RandomIntGenerator(int upper) {
        this.upper = upper;
        this.random = new ThreadLocal<Random>(){
            @Override
            protected Random initialValue() {
                return new Random();
            }
        };
    }

    /**
     * Default Constructor
     */
    public RandomIntGenerator() {
       this(Integer.MAX_VALUE);
    }

    @Override
    public String generate(Object... args) {
        Integer value = random.get().nextInt(upper);
        return value.toString();
    }
}
