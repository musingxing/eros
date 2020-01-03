package com.eros.common.string;

import java.util.Iterator;
import java.util.Map;
/**
 * Two-dimension table.
 *
 * @author Eros
 * @since   2020-01-02 17:07
 */
public class DoubleColumnTable {

    private static final char HEAD_SEP = '—';
    private static final char SEP = '|';
    private static final int LENGTH = 192;
    private static final int MIN_LINE_LENGTH = 64;
    private static final int MAX_LINE_LENGTH = 1024;

    private final char headSep;
    private final char midSep;
    private final int length;
    private final String header;
    private final String itemKey;
    private final String itemValue;
    private final String key_;
    private final String value_;
    /**
     * Constructor
     *
     * @param headSep      header separator
     * @param midSep       middle separator
     * @param length       max length of table
     * @param itemKey      item key name
     * @param itemValue    item value name
     */
    DoubleColumnTable(char headSep, char midSep, int length, String itemKey, String itemValue) {
        this.headSep = headSep;
        if(length < MIN_LINE_LENGTH)
            throw new IllegalArgumentException("length can't be less than " + MIN_LINE_LENGTH);
        if(length > MAX_LINE_LENGTH)
            throw new IllegalArgumentException("length can't be greater than " + MAX_LINE_LENGTH);
        StringBuilder headBuilder = new StringBuilder();
        for(int i = 0; i < length; i++){
            headBuilder.append(headSep);
        }
        this.header = headBuilder.toString();
        this.midSep = midSep;
        this.length = length;
        this.key_ = "%" + length/2 + "s\t";
        this.value_ ="\t%-" + length/2 + "s";
        this.itemKey = String.format(key_, itemKey);
        this.itemValue = String.format(value_, itemValue);;
    }

    public String format(Map<? extends Object, Object> kvs){
        StringBuilder format = new StringBuilder();
        format.append("\n").append(header).append("\n");
        format.append(itemKey).append(midSep).append(itemValue).append("\n");
        format.append(header).append("\n");
        for(Iterator<? extends Map.Entry<?, Object>> it = kvs.entrySet().iterator(); it.hasNext();){
            Map.Entry<? extends Object, Object> entry = it.next();
            String key = String.format(key_, entry.getKey());
            String value = String.format(value_, entry.getValue());
            format.append(key).append(midSep).append(value).append("\n");
        }
        format.append(header).append("\n");
        return format.toString();
    }

    /**
     * Construct table formatter
     *
     * @param headSep      header separator
     * @param midSep       middle separator
     * @param length       max length of table
     * @param itemKey      item key name
     * @param itemValue    item value name
     * @return  {@link DoubleColumnTable}
     */
    public static DoubleColumnTable newTable(char headSep, char midSep, int length, String itemKey, String itemValue){
        return new DoubleColumnTable(headSep, midSep,  length, itemKey, itemValue);
    }

    /**
     * Construct table formatter
     *
     * @param itemKey      item key name
     * @param itemValue    item value name
     * @return  {@link DoubleColumnTable}
     */
    public static DoubleColumnTable newTable(String itemKey, String itemValue){
        return new DoubleColumnTable(HEAD_SEP, SEP, LENGTH, itemKey, itemValue);
    }
}
