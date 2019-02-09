/*
    MIT License
    
    Copyright (c) 2018 FrozenLegend
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/
package net.frozenspace.frostlib.gui.design;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represent a GUI page
 *
 * @author Frozen
 */
public class Page {

    private final Inventory inventory;

    private final Plugin plugin;

    private final String name;
    private final Size size;

    private final Map<Integer, Item> items = new HashMap<>();

    /**
     * Default constructor
     *
     * @param name   Name of the page (title of the inventory)
     * @param size   Size of the page (size of the inventory)
     * @param plugin Your plugin instance
     */
    Page( @NotNull String name, @NotNull Size size, @NotNull Plugin plugin ) {
        this.plugin = plugin;
        this.name = ChatColor.stripColor( name );
        this.size = size;
        this.inventory = Bukkit.createInventory( null, size.getSlots(), name );
    }

    /**
     * Method used for calculate the slot using row/column
     *
     * @param row    Row wanted
     * @param column Column wanted
     * @return The slot
     */
    public static int calculateSlot( Row row, Column column ) {
        return calculateSlot( row.getValue(), column.getValue() );
    }

    /**
     * Method used for calculate the slot using row/column
     *
     * @param row    Row wanted
     * @param column Column wanted
     * @return The slot
     */
    public static int calculateSlot( int row, int column ) {
        return ( row * 9 ) - ( 9 - column ) - 1;
    }

    /**
     * Create a new pattern instance of the correct size
     *
     * @return The pattern instance created
     */
    public Pattern createPattern() {
        return new Pattern( size );
    }

    /**
     * Apply a pattern to this page
     *
     * @param pattern Pattern to apply
     */
    public void applyPattern( Pattern pattern ) {
        final String[] rows = pattern.getPattern();
        for ( int i = 0; i < rows.length; i++ ) {
            final char[] column = rows[i].toCharArray();
            for ( int j = 0; j < column.length; j++ ) {
                final Item item = pattern.getItems().get( column[j] );
                if ( item != null ) {
                    if ( item instanceof ActionItem ) {
                        addActionItem( (ActionItem) item, i + 1, j + 1 );
                    } else {
                        addItem( item, i + 1, j + 1 );
                    }
                }
            }
        }
    }

    /**
     * Add an action item to the page
     *
     * @param item   Item to add
     * @param row    Row where the item need to be
     * @param column Column where the item need to be
     * @return Current page
     */
    public Page addActionItem( ActionItem item, Row row, Column column ) {
        return addActionItem( item, calculateSlot( row, column ) );
    }

    /**
     * Add an action item to the page
     *
     * @param item   Item to add
     * @param row    Row where the item need to be
     * @param column Column where the item need to be
     * @return Current page
     */
    public Page addActionItem( ActionItem item, int row, int column ) {
        return addActionItem( item, calculateSlot( row, column ) );
    }

    /**
     * Add an action item to the page
     *
     * @param item Item to add
     * @param slot Slot where the item is
     * @return Current page
     */
    public Page addActionItem( ActionItem item, int slot ) {
        items.put( slot, item );
        inventory.setItem( slot, item.getStack() );
        return this;
    }

    /**
     * Add an item to the page
     *
     * @param item   Item to add
     * @param row    Row where the item need to be
     * @param column Column where the item need to be
     * @return Current page
     */
    public Page addItem( Item item, int row, int column ) {
        return addItem( item, calculateSlot( row, column ) );
    }

    /**
     * Add an action item to the page
     *
     * @param item   Item to add
     * @param row    Row where the item need to be
     * @param column Column where the item need to be
     * @return Current page
     */
    public Page addItem( Item item, Row row, Column column ) {
        return addItem( item, calculateSlot( row.getValue(), column.getValue() ) );
    }

    /**
     * Add an action item to the page
     *
     * @param item Item to add
     * @param slot Slot where the item is
     * @return Current page
     */
    public Page addItem( Item item, int slot ) {
        items.put( slot, item );
        inventory.setItem( slot, item.getStack() );
        return this;
    }

    /**
     * Update the inventory with correct items and call
     * #update() for all players viewer
     */
    public void update() {
        items.forEach( ( K, V ) -> inventory.setItem( K, V.getStack() ) );
        new BukkitRunnable() {
            @Override
            public void run() {
                inventory.getViewers().stream()
                        .filter( v -> v instanceof Player )
                        .forEach( v -> ( (Player) v ).updateInventory() );
            }
        }.runTask( plugin );
    }

    /**
     * This method is used for firing a click
     * by a the selected player in the selected slot
     *
     * @param player Player who clicked
     * @param slot   Slot where he clicked
     */
    public void click( Player player, int slot ) {
        Item item = items.get( slot );
        if ( item instanceof ActionItem ) {
            ( (ActionItem) item ).click( player );
        }
    }

    /**
     * Get the name of the page
     *
     * @return The name of the page
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the inventory of the page
     *
     * @return The inventory of the page
     */
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Define if an object is equals to this page
     *
     * @param o Object to check
     * @return True if equals false if not
     */
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Page page = (Page) o;
        return Objects.equals( plugin, page.plugin ) &&
                Objects.equals( name, page.name ) &&
                Objects.equals( inventory, page.inventory );
    }

}
