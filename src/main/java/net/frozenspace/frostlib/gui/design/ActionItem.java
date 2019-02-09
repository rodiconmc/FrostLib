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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.bukkit.inventory.ItemFlag;

/**
 * This class represent an Item who do something when clicked in inventory
 *
 * @author Frozen
 */
public class ActionItem extends Item {

    private Click onClick = ( player ) -> {
    };

    /**
     * Constructor using ItemStack
     *
     * @param stack The ItemStack of this item
     */
    private ActionItem( @NotNull ItemStack stack ) {
        super( stack );
    }

    /**
     * Constructor using Material
     *
     * @param material The material of this item
     */
    private ActionItem( @NotNull Material material ) {
        super( material );
    }

    /**
     * Static method for create a new item (better readability)
     * This method is just calling {@link #ActionItem(Material)}
     *
     * @param material The material of this item
     * @return The ActionItem instance created
     */
    @NotNull
    public static ActionItem create( @NotNull Material material ) {
        return new ActionItem( material );
    }

    /**
     * Static method for create a new item (better readability)
     * This method is just calling {@link #ActionItem(ItemStack)}
     *
     * @param stack The ItemStack of this item
     * @return The ActionItem instance created
     */
    @NotNull
    public static ActionItem create( @NotNull ItemStack stack ) {
        return new ActionItem( stack );
    }

    /**
     * Set the material of this item
     *
     * @param material Material of this item
     * @return Current instance
     */
    @Override
    public @NotNull ActionItem material( @NotNull Material material ) {
        return (ActionItem) super.material( material );
    }

    /**
     * Set the amount in the ItemStack
     *
     * @param amount Amount of this item
     * @return Current instance
     */
    @Override
    public @NotNull ActionItem amount( int amount ) {
        return (ActionItem) super.amount( amount );
    }

    /**
     * Set the name of the item
     *
     * @param name Name of the item
     * @return Current instance
     */
    @Override
    public @NotNull ActionItem name( @NotNull String name ) {
        return (ActionItem) super.name( name );
    }

    /**
     * Set the lore of the item
     *
     * @param lores Lore of the item
     * @return Current instance
     */
    @Override
    public @NotNull ActionItem lore( @NotNull String... lores ) {
        return (ActionItem) super.lore( lores );
    }
    
    /**
     * Set the flags of this item
     *
     * @param flags Flags to add
     * @return Current instance
     */
    @Override
    public @NotNull ActionItem flag( @NotNull ItemFlag... flags ) {
        return (ActionItem) super.flag( flags );
    }

    /**
     * Set the itemstack of the item
     *
     * @param stack Itemstack wanted
     * @return Current instance
     */
    @Override
    public @NotNull ActionItem stack( @NotNull ItemStack stack ) {
        return (ActionItem) super.stack( stack );
    }

    /**
     * Get the itemstack
     *
     * @return The itemstack
     */
    @Override
    @NotNull ItemStack getStack() {
        return super.getStack();
    }

    /**
     * Set the action performed when clicking on this item
     *
     * @param click Action to perform
     * @return Current instance
     */
    public ActionItem click( Click click ) {
        this.onClick = click;
        return this;
    }

    /**
     * Perform a click action
     *
     * @param player Player who clicked
     */
    void click( Player player ) {
        onClick.run( player );
    }

}
