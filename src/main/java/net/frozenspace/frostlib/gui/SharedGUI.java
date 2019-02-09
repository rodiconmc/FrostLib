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

import java.util.HashMap;
import java.util.Map;

/**
 * This class represent a GUI used by multiple player
 * If you need a GUI who can be by only one player use {@link PersonalGUI}
 *
 * @author Frozen
 */
public class SharedGUI extends GUI {

    private Map<Player, Page> currentPages = new HashMap<>();
    private Map<Player, Page> previousPages = new HashMap<>();

    /**
     * Default constructor
     *
     * @param name   Name of the GUI
     * @param plugin Your plugin instance
     */
    SharedGUI( @NotNull String name, @NotNull Plugin plugin ) {
        super( name, plugin );
    }

    /**
     * Open the first page to the selected player
     *
     * @param player Player which will see the page
     */
    public void open( @NotNull Player player ) {
        currentPages.put( player, super.openGUI( player ) );
    }

    /**
     * Show the selected page to the selected player
     *
     * @param page   Page to show
     * @param player Player which will see the page
     */
    public void show( @NotNull Page page, @NotNull Player player ) {
        currentPages.put( player, super.showPage( page, player ) );
    }

    /**
     * Show the next page to the selected player (if exist)
     *
     * @param player Player which will see the page
     */
    public void next( @NotNull Player player ) {
        currentPages.put( player, super.nextPage( currentPages.get( player ), player ) );
    }

    /**
     * Show the previous page to the selected player (if exist)
     *
     * @param player Player which will see the page
     */
    public void previous( @NotNull Player player ) {
        if ( previousPages.containsKey( player ) ) {
            previousPages.put( player, currentPages.get( player ) );
            currentPages.put( player, super.showPage( previousPages.get( player ), player ) );
        }
    }

}
