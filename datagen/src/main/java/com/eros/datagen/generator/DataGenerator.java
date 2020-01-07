package com.eros.datagen.generator;

/**
 * Base class for data generator.
 *
 * @author Eros
 * @since   2020-01-03 10:42
 */
public abstract class DataGenerator {

    /**
     * Generate data with specified parameters
     *
     * @param args  parameters
     * @return      data
     */
    public abstract String generate(Object... args);

    @Override
    public String toString() {
        return getClass().getCanonicalName();
    }

    /**
     * Build data generator from class string
     *
     * @param clazz  class string
     * @return   {@link DataGenerator}
     */
    public static DataGenerator parse(String clazz){
        try{
            Class<?> c = Class.forName(clazz);
            return (DataGenerator)c.newInstance();
        }catch (Throwable e){
            throw new RuntimeException("Unknown exception to build data generator", e);
        }
    }
}
