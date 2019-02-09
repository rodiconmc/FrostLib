/*
 * Copyright (c) 2018 Frozencraft
 */
package net.frozenspace.frostlib.gui.design;


import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for creating a Pattern for filling the GUI
 *
 * @author Frozen
 */
public class Pattern {

    private final String[] pattern;
    private final Map<Character, Item> items = new HashMap<>();

    /**
     * Default constructor
     *
     * @param size Size of the pattern (need to be the same as the GUI)
     */
    Pattern( Size size ) {
        pattern = new String[size.getSlots() / 9];
        for ( int i = 0; i < pattern.length; i++ ) {
            pattern[i] = "         ";
        }
    }

    /**
     * Set the select string pattern to the row
     *
     * @param row   The row selected
     * @param value The pattern of this row
     * @return The current instance
     */
    public Pattern setRow( Row row, String value ) {
        if ( row.getValue() - 1 < pattern.length ) {
            if ( value.length() == 9 ) {
                pattern[row.getValue() - 1] = value;
            }
        }
        return this;
    }

    /**
     * Set the a value to the key used in the pattern
     *
     * @param key   Key linked with value
     * @param value Value to assign
     * @return Current instance
     */
    public Pattern setKey( Character key, Item value ) {
        items.put( key, value );
        return this;
    }

    /**
     * Get the pattern
     *
     * @return The pattern array
     */
    String[] getPattern() {
        return pattern;
    }

    /**
     * Get the items of this pattern
     *
     * @return Items of this pattern
     */
    Map<Character, Item> getItems() {
        return items;
    }

}
