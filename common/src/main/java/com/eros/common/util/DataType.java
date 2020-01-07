package com.eros.common.util;

/**
 * Basic data type for java
 *
 * @author Eros
 * @since 2020-01-02 15:58
 */
public enum DataType {
    STRING("java.lang.String"),
    INTEGER("java.lang.Integer"),
    LONG("java.lang.Long"),
    DOUBLE("java.lang.Double"),
    SHORT("java.lang.Short"),
    FLOAT("java.lang.Float"),
    BYTE("java.lang.Byte"),
    BOOLEAN("java.lang.Boolean"),
    CHAR("java.lang.Character");

    private final String clazzName;
    /**
     * Constructor
     *
     * @param clazzName  class  name
     */
    DataType(String clazzName) {
        this.clazzName = clazzName;
    }

    /**
     * Parse data type from {@code type}
     *
     * @param type type
     * @return  {@link DataType}
     */
    public static DataType parse(String type){
        if(type == null || type.isEmpty())
            throw new IllegalArgumentException("type is null or empty");
        switch (type.toUpperCase()){
            case "STRING":
                return STRING;
            case "INTEGER":
                return INTEGER;
            case "LONG":
                return LONG;
            case "DOUBLE":
                return DOUBLE;
            case "SHORT":
                return SHORT;
            case "FLOAT":
                return FLOAT;
            case "BYTE":
                return BYTE;
            case "BOOLEAN":
                return BOOLEAN;
            case "CHAR":
                return CHAR;
            default:
                throw new IllegalArgumentException("unknown data type: " + type);
        }
    }

    /**
     * Convert data from {@code value}
     *
     * @param type data type
     * @param value real value
     * @return  Basic data object
     */
    public static Object convert(String value, DataType type){
        if(value == null || value.isEmpty())
            throw new IllegalArgumentException("value is null or empty");
        switch (type){
            case STRING:
                return value;
            case INTEGER:
                return Integer.parseInt(value.trim());
            case LONG:
                return Long.parseLong(value.trim());
            case DOUBLE:
                return Double.parseDouble(value.trim());
            case SHORT:
                return Short.parseShort(value.trim());
            case FLOAT:
                return Float.parseFloat(value.trim());
            case BYTE:
                return Byte.parseByte(value.trim());
            case BOOLEAN:
                return Boolean.parseBoolean(value.trim());
            case CHAR:
                return value.charAt(0);
            default:
                throw new IllegalArgumentException("unknown data type: " + type);
        }
    }

    public static Object parseAndConvert(String value, String type){
        DataType dataType = parse(type);
        return convert(value, dataType);
    }
}
