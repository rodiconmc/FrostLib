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
package net.frozenspace.frostlib.command;

import net.frozenspace.frostlib.data.Loader;
import net.frozenspace.frostlib.util.ReflectUtil;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * CommandLoader is used for creating bukkit standard commands
 * by loading class who contains methods annotated with {@link Command}
 * Each methods annotated with {@link Command} can have a method
 * annotated with {@link Completer} who will be set as TabCompleter of the command
 *
 * @author Frozen
 */
public final class CommandLoader extends Loader {

    private final Plugin plugin;

    private CommandMap commandMap;

    /**
     * Constructor used for initialization and also getting the {@link CommandMap}
     * instance in {@link Bukkit#getPluginManager()}
     *
     * @param plugin Plugin instance of your plugin
     */
    public CommandLoader( @NotNull Plugin plugin ) {
        Validate.notNull( plugin, "[CommandLoader] Plugin instance of CommandLoader is null." );
        this.plugin = plugin;
        if ( Bukkit.getPluginManager() instanceof SimplePluginManager ) {
            SimplePluginManager spm = (SimplePluginManager) Bukkit.getPluginManager();
            try {
                Field field = FieldUtils.getDeclaredField( spm.getClass(), "commandMap", true );
                commandMap = (CommandMap) field.get( spm );
            } catch ( IllegalAccessException e ) {
                throw new RuntimeException( "Can't get the Bukkit CommandMap instance." );
            }
        }
        plugin.getLogger().info( "[CommandLoader] Successfully initialized." );
    }

    /**
     * Create a new PluginCommand for all the methods annotated with {@link Command}
     * and register this command into Bukkit CommandMap
     *
     * @param obj Instance of the object to load
     * @param <T> Type of the object to load
     * @return The object passed as parameter
     */
    @NotNull
    public <T> T load( @NotNull T obj ) {
        Validate.notNull( obj, "[CommandLoader] Trying to load a null object." );
        final Map<String, Method> completers = new HashMap<>();
        final List<Method> allMethods = ReflectUtil.getAllMethods( obj.getClass() );
        allMethods.stream()
                .filter( method -> method.isAnnotationPresent( Completer.class ) )
                .forEach( method -> completers.put( method.getAnnotation( Completer.class ).value(), method ) );
        allMethods.stream()
                .filter( method -> method.isAnnotationPresent( Command.class ) )
                .forEach( method -> {
                    Command command = method.getAnnotation( Command.class );
                    Class<?>[] parameters = method.getParameterTypes();
                    if ( command != null ) {
                        if ( parameters.length >= 1
                                && parameters[0].equals( command.sender().get() ) ) {
                            final PluginCommand pluginCommand = createPluginCommand( command.name() );
                            if ( pluginCommand != null ) {
                                pluginCommand.setAliases( Arrays.asList( command.aliases() ) );
                                pluginCommand.setPermission( command.permission() );
                                pluginCommand.setUsage( command.usage() );
                                pluginCommand.setDescription( command.description() );
                                pluginCommand.setPermissionMessage( command.permissionMessage() );
                                if ( command.completion() ) {
                                    Method completerMethod = completers.get( command.name() );
                                    if ( completerMethod != null ) {
                                        Class<?>[] completerParameters = completerMethod.getParameterTypes();
                                        if ( completerParameters.length == 2
                                                && completerParameters[0].equals( int.class )
                                                && completerParameters[1].equals( String[].class ) ) {
                                            pluginCommand.setTabCompleter( ( sender, cmd, alias, args ) -> {
                                                try {
                                                    @SuppressWarnings( "unchecked" )
                                                    List<String> completions = (List<String>) completerMethod.invoke( obj, args.length - 1, args );
                                                    return completions;
                                                } catch ( IllegalAccessException | InvocationTargetException e ) {
                                                    e.printStackTrace();
                                                }
                                                return new ArrayList<>();
                                            } );
                                        } else {
                                            plugin.getLogger().severe( "[CommandLoader] Wrong parameter for Completer method used by " + command.name() + "." );
                                        }
                                    } else {
                                        plugin.getLogger().severe( "[CommandLoader] Completion field of Command annotation set to true but no Completer found for " + command.name() + "." );
                                    }
                                }
                                pluginCommand.setExecutor( ( sender, cmd, label, args ) -> {
                                    if ( command.sender().get().isInstance( sender ) && args.length >= command.minArgs() ) {
                                        try {
                                            if ( method.getParameterCount() == 2 ) {
                                                method.invoke( obj, command.sender().get().cast( sender ), args );
                                            } else {
                                                method.invoke( obj, command.sender().get().cast( sender ) );
                                            }
                                        } catch ( IllegalAccessException | InvocationTargetException e ) {
                                            sender.sendMessage( ChatColor.RED + "There is an error when performing this command." );
                                            plugin.getLogger().severe( "[CommandLoader] Can't execute the command method of " + command.name() + "." );
                                            e.printStackTrace();
                                        }
                                    }
                                    return true;
                                } );
                                commandMap.register( plugin.getDescription().getName(), pluginCommand );
                                plugin.getLogger().info( "[CommandLoader] Command " + command.name() + " created and registered." );
                            } else {
                                plugin.getLogger().severe( "[CommandLoader] Error when creating a new instance of PluginCommand for command: " + command.name() + "." );
                            }
                        }
                    }
                } );
        return obj;
    }

    /**
     * Create a new instance of {@link PluginCommand} using reflection
     *
     * @param name Name of the command
     * @return The command instantiated or null if failed
     */
    @Nullable
    private PluginCommand createPluginCommand( @NotNull String name ) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor( String.class, Plugin.class );
            constructor.setAccessible( true );
            return constructor.newInstance( name, plugin );
        } catch ( NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e ) {
            e.printStackTrace();
        }
        return null;
    }

}
