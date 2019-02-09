/*
 * Copyright (c) 2018 Frozencraft
 */
package net.frozenspace.frostlib.gui.design;

/**
 * This enum is used for representing a column in a page
 *
 * @author Frozen
 */
public enum Column {

    FIRST( 1 ),
    SECOND( 2 ),
    THIRD( 3 ),
    FOURTH( 4 ),
    FIFTH( 5 ),
    SIXTH( 6 ),
    SEVENTH( 7 ),
    EIGHTH( 8 ),
    NINTH( 9 );

    private final int value;

    /**
     * Default constructpr
     *
     * @param value Value as int
     */
    Column( int value ) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
