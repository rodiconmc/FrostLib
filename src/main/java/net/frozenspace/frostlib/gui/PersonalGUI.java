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
package net.frozenspace.frostlib.gui;

import net.frozenspace.frostlib.gui.design.GUI;
import net.frozenspace.frostlib.gui.design.Page;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * This class represent a GUI used by one and only one player
 * If you need a GUI who can be accessed by different player use {@link SharedGUI}
 *
 * @author Frozen
 */
public class PersonalGUI extends GUI {

    private Player player;

    private Page currentPage;
    private Page previousPage;

    /**
     * Default constructor
     *
     * @param name   Name of the GUI
     * @param player Player attached to this GUI
     * @param plugin Your plugin instance
     */
    PersonalGUI( @NotNull String name, @NotNull Player player, @NotNull Plugin plugin ) {
        super( name, plugin );
        this.player = player;
    }

    /**
     * Open the first page of the GUI to the owner
     */
    public void open() {
        super.openGUI( player );
    }

    /**
     * Show the selected page to the owner
     *
     * @param page Page to show
     */
    public void show( @NotNull Page page ) {
        if ( currentPage != page ) {
            previousPage = currentPage;
            currentPage = super.showPage( page, player );
        }
    }

    /**
     * Show the next page to the player (if exist)
     */
    public void next() {
        currentPage = super.nextPage( currentPage, player );
    }

    /**
     * Show the previous page to the player (if exist)
     */
    public void previous() {
        if ( previousPage != null ) {
            previousPage = currentPage;
            currentPage = super.showPage( previousPage, player );
        }
    }

    /**
     * Get the owner of this GUI
     *
     * @return The player instance
     */
    public Player getPlayer() {
        return player;
    }

}

