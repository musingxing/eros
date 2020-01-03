package com.eros.datagen.generator.impl;

import com.eros.datagen.generator.DataGenerator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Data generator to generate random hexadecimal char.
 *
 * @author Eros
 * @since   2020-01-03 10:42
 */
public class RandomHexCharsGenerator extends DataGenerator {

    private final int upperLen;
    private final int lowerLen;
    private final ThreadLocal<Random> random;
    private final ThreadLocalRandom lenRandom = ThreadLocalRandom.current();

    /**
     * Constructor
     *
     * @param upperLen    length upper
     * @param lowerLen    length lower
     */
    public RandomHexCharsGenerator(int lowerLen, int upperLen) {
        this.upperLen = upperLen;
        this.lowerLen = lowerLen;
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
    public RandomHexCharsGenerator() {
        this(1, 32);
    }

    @Override
    public String generate(Object... args) {

        int len = lenRandom.nextInt(lowerLen, upperLen);
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < len; i++){
            Integer data = random.get().nextInt();
            String hexStr = Integer.toHexString(data);
            if(hexStr.length() <= (len-str.length()))
                str.append(hexStr);
            else
                str.append(hexStr.toCharArray(), 0, len-str.length());

            if(str.length() >= len) break;;
        }

        return str.toString();
    }
}
