/*
 * Copyright (c) 2018 Frozencraft
 */
package net.frozenspace.frostlib.gui.design;

/**
 * This enum is used for representing a row in a page
 *
 * @author Frozen
 */
public enum Row {

    FIRST( 1 ),
    SECOND( 2 ),
    THIRD( 3 ),
    FOURTH( 4 ),
    FIFTH( 5 ),
    SIXTH( 6 );

    private final int value;

    /**
     * Default constructor
     *
     * @param value Value as int
     */
    Row( int value ) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
