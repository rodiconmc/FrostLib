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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used for create/saving GUI
 *
 * @author Frozen
 */
public class GUIManager implements Listener {

    private final Plugin plugin;
    private final Set<GUI> guis = new HashSet<>();

    /**
     * Default constructor
     *
     * @param plugin Your plugin instance
     */
    public GUIManager( @NotNull Plugin plugin ) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents( this, plugin );
    }

    /**
     * Get the player personal GUI using gui name and player instance
     *
     * @param name   Name of the GUI
     * @param player Instance of the player
     * @return The GUI found or a new one
     */
    @NotNull
    public PersonalGUI getPersonal( @NotNull String name, @NotNull Player player ) {
        PersonalGUI personalGUI = (PersonalGUI) guis.stream()
                .filter( GUI -> GUI instanceof PersonalGUI )
                .filter( GUI -> ( (PersonalGUI) GUI ).getPlayer().equals( player ) && GUI.getName().equalsIgnoreCase( ChatColor.stripColor( name ) ) )
                .findFirst().orElse( new PersonalGUI( name, player, plugin ) );
        guis.add( personalGUI );
        return personalGUI;
    }

    /**
     * Get the shared GUI using gui name
     *
     * @param name Name of the GUI
     * @return The GUI found or a new one
     */
    @NotNull
    public SharedGUI getShared( @NotNull String name ) {
        SharedGUI sharedGUI = (SharedGUI) guis.stream()
                .filter( GUI -> GUI instanceof SharedGUI )
                .filter( GUI -> GUI.getName().equalsIgnoreCase( ChatColor.stripColor( name ) ) )
                .findFirst().orElse( new SharedGUI( name, plugin ) );
        guis.add( sharedGUI );
        return sharedGUI;
    }

    @EventHandler
    public void onInventoryClick( InventoryClickEvent e ) {
        if ( e.getClickedInventory() != null
                && e.getCurrentItem() != null ) {
            guis.forEach( gui -> gui.getPages()
                    .stream()
                    .filter( page -> page.getInventory().equals( e.getClickedInventory() ) )
                    .findFirst()
                    .ifPresent( page -> {
                        page.click( (Player) e.getWhoClicked(), e.getSlot() );
                        e.setCancelled( true );
                    } ) );
        }
    }

}
