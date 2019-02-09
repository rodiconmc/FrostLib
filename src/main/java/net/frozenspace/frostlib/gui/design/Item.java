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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;

/**
 * This class represent an item in the GUI
 * This item is not reactive to click event if you want
 * to execute something when clicking on an item use {@link ActionItem} instead
 *
 * @author Frozen
 */
public class Item {

    private ItemStack stack;

    /**
     * Defaut constructor taking ItemStack as parameter
     *
     * @param stack ItemStack of this item
     */
    Item( @NotNull ItemStack stack ) {
        this.stack = stack;
    }

    /**
     * Defaut constructor taking ItemStack as parameter
     *
     * @param material Material of this item
     */
    Item( @NotNull Material material ) {
        stack = new ItemStack( material );
    }

    /**
     * Create a new Item instance
     *
     * @param material Material of this item
     * @return Instance created
     */
    @NotNull
    public static Item create( @NotNull Material material ) {
        return new Item( material );
    }

    /**
     * Create a new Item instance
     *
     * @param stack ItemStack of this item
     * @return Instance created
     */
    @NotNull
    public static Item create( @NotNull ItemStack stack ) {
        return new Item( stack );
    }

    /**
     * Set the material used by this item
     *
     * @param material Material of this item
     * @return Current instance
     */
    @NotNull
    public Item material( @NotNull Material material ) {
        stack.setType( material );
        return this;
    }

    /**
     * Set the amount of this item
     *
     * @param amount Amount of the item
     * @return Current instance
     */
    @NotNull
    public Item amount( int amount ) {
        stack.setAmount( amount );
        return this;
    }

    /**
     * Set the name of this item
     *
     * @param name Name of this item
     * @return Current instance
     */
    @NotNull
    public Item name( @NotNull String name ) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName( name );
        stack.setItemMeta( meta );
        return this;
    }

    /**
     * Set the lore of this item
     *
     * @param lore Lore of this item
     * @return Current instance
     */
    @NotNull
    public Item lore( @NotNull String... lore ) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore( Arrays.asList( lore ) );
        stack.setItemMeta( meta );
        return this;
    }
    
    /**
     * Set the flags of this item
     *
     * @param flags Flags to add
     * @return Current instance
     */
    @NotNull
    public Item flag(@NotNull ItemFlag... flags) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(flags);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Set the ItemStack of this item
     *
     * @param stack ItemStack of this item
     * @return Current instance
     */
    @NotNull
    public Item stack( @NotNull ItemStack stack ) {
        this.stack = stack;
        return this;
    }

    /**
     * Get the ItemStack of this item
     *
     * @return ItemStack of this item
     */
    @NotNull
    ItemStack getStack() {
        return stack;
    }

}
