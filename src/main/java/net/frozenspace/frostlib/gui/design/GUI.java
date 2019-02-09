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

import net.frozenspace.frostlib.gui.exception.PageNotFoundException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class is the representation of a GUI
 *
 * @author Frozen
 */
public abstract class GUI {

    private Plugin plugin;

    private String name;
    private Set<Page> pages = new LinkedHashSet<>();

    /**
     * Default constructor
     *
     * @param name   Name of the GUI
     * @param plugin Your plugin instance
     */
    public GUI( @NotNull String name, @NotNull Plugin plugin ) {
        this.plugin = plugin;
        this.name = name;
    }

    /**
     * Get the selected page using name
     *
     * @param name Name of the page
     * @return The page found or null
     */
    @Nullable
    public Page getPage( @NotNull String name ) {
        return pages.stream()
                .filter( p -> p.getName().equalsIgnoreCase( name ) )
                .findFirst()
                .orElse( null );
    }

    /**
     * Create a page and add it to page list
     *
     * @param name Name of the page
     * @param size Size of the page
     * @return The page created
     */
    @NotNull
    public Page createPage( @NotNull String name, @NotNull Size size ) {
        final Page page = new Page( name, size, plugin );
        pages.add( page );
        return page;
    }

    /**
     * Open the GUI to the selected player (first page)
     *
     * @param player Player wanted
     * @return The page opened
     */
    @NotNull
    protected Page openGUI( @NotNull Player player ) {
        if ( pages.size() == 0 ) throw new PageNotFoundException( "There is no page in GUI pages" );
        return showPage( pages.iterator().next(), player );
    }

    /**
     * Show the selected page to the selected player
     *
     * @param page   Page to show
     * @param player Player wanted
     * @return The page show
     */
    @NotNull
    protected Page showPage( @NotNull Page page, @NotNull Player player ) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
                player.openInventory( page.getInventory() );
            }
        }.runTask( plugin );
        return page;
    }

    /**
     * Show the next page according to the current page
     *
     * @param currentPage The current page
     * @param player      The player wanted
     * @return The page next page or the current if there is no next page
     */
    @NotNull
    protected Page nextPage( @NotNull Page currentPage, @NotNull Player player ) {
        while ( pages.iterator().hasNext()
                && pages.iterator().next() != currentPage ) {
            pages.iterator().next();
        }
        if ( pages.iterator().hasNext() ) {
            return showPage( pages.iterator().next(), player );
        }
        return currentPage;
    }

    /**
     * Get the name of the GUI
     *
     * @return Name of the GUI
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the pages of the GUI
     *
     * @return Pages of the GUI
     */
    public Set<Page> getPages() {
        return pages;
    }

    /**
     * Determine if an object is equals to this object
     *
     * @param obj The object to check
     * @return true if equals false if not
     */
    @Override
    public boolean equals( Object obj ) {
        if ( obj instanceof GUI ) {
            GUI gui = (GUI) obj;
            return gui.getName().equalsIgnoreCase( name );
        }
        return false;
    }

}
