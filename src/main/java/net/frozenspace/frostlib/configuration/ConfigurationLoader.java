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
package net.frozenspace.frostlib.configuration;

import net.frozenspace.frostlib.data.Loader;
import net.frozenspace.frostlib.util.ReflectUtil;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CommandManager is used for creating/loading configuration file
 * by loading class annotated with {@link Configuration}
 * Each field in the class will be loaded from the file created/loaded
 *
 * @author Frozen
 */
public class ConfigurationLoader extends Loader {

    private final Plugin plugin;

    private boolean excludeFieldsWithoutManage = false;

    /**
     * Default constructor
     *
     * @param plugin Instance of your plugin
     */
    public ConfigurationLoader( @NotNull Plugin plugin ) {
        this.plugin = plugin;
    }

    /**
     * This method is used for excluding all fields in the classes who
     * don't have the {@link Manage} annotation
     *
     * @return The current instance
     */
    @NotNull
    public ConfigurationLoader excludeFieldsWithoutManageAnnotation() {
        excludeFieldsWithoutManage = true;
        return this;
    }

    /**
     * This method will load all the field in the object in parameter
     * If {@link #excludeFieldsWithoutManageAnnotation()} has been called before
     * All the fields without this annotation will be ignored
     *
     * @param obj Object to load
     * @param <T> Type of the object to load
     * @return The object loaded
     */
    @NotNull
    public <T> T load( @NotNull T obj ) {
        Validate.notNull( obj, "[ConfigurationLoader] Trying to load a null object." );
        final Configuration configuration = obj.getClass().getAnnotation( Configuration.class );
        if ( configuration != null ) {
            final File file = create( configuration );
            final FileConfiguration fileConfiguration = new YamlConfiguration();
            try {
                fileConfiguration.load( file );
            } catch ( IOException | InvalidConfigurationException e ) {
                plugin.getLogger().severe( "[ConfigurationLoader] Can't load " + configuration.value() + " file configuration." );
            }
            final List<Field> fields;
            if ( excludeFieldsWithoutManage ) {
                fields = ReflectUtil.getAllFields( obj.getClass() ).stream()
                        .filter( f -> f.isAnnotationPresent( Manage.class ) )
                        .collect( Collectors.toList() );
            } else {
                fields = ReflectUtil.getAllFields( obj.getClass() ).stream()
                        .filter( f -> !f.isAnnotationPresent( Manage.class )
                                || ( f.isAnnotationPresent( Manage.class )
                                && f.getAnnotation( Manage.class ).load() )
                        ).collect( Collectors.toList() );
            }
            List<Field> otherFile = fields.stream()
                    .filter( f -> f.getType().isAnnotationPresent( Configuration.class ) )
                    .collect( Collectors.toList() );
            fields.removeAll( otherFile );
            fields.forEach( field -> {
                final Key key = field.getAnnotation( Key.class );
                final String name = key != null ? key.value() : field.getName();
                final Object value = fileConfiguration.get( name );
                if ( value != null ) {
                    try {
                        FieldUtils.writeField( field, obj, value, true );
                    } catch ( IllegalAccessException e ) {
                        plugin.getLogger().warning( "[ConfigurationLoader] Can't set the value in field " + field.getName() + "." );
                    }
                } else {
                    plugin.getLogger().warning( "[ConfigurationLoader] Object get with key " + name + " in file " + configuration.value() + " is null." );
                }
            } );
            otherFile.forEach( field -> {
                try {
                    FieldUtils.writeField( field, obj, load( field.getType() ), true );
                } catch ( IllegalAccessException e ) {
                    plugin.getLogger().warning( "[ConfigurationLoader] Can't set the value in field " + field.getName() + "." );
                }
            } );
        }
        return obj;
    }

    /**
     * This method will save all the field of the object in the file
     * If {@link #excludeFieldsWithoutManageAnnotation()} has been called before
     * All the fields without this annotation will be ignored
     *
     * @param obj Object to save
     */
    public void save( @NotNull Object obj ) {
        Validate.notNull( obj, "[ConfigurationLoader] Trying to save a null object." );
        final Configuration configuration = obj.getClass().getAnnotation( Configuration.class );
        if ( configuration != null ) {
            final File file = create( configuration );
            final FileConfiguration fileConfiguration = new YamlConfiguration();
            try {
                fileConfiguration.load( file );
            } catch ( IOException | InvalidConfigurationException e ) {
                plugin.getLogger().severe( "[ConfigurationLoader] Can't load " + configuration.value() + " file configuration." );
            }
            final List<Field> fields;
            if ( excludeFieldsWithoutManage ) {
                fields = ReflectUtil.getAllFields( obj.getClass() ).stream()
                        .filter( f -> f.isAnnotationPresent( Manage.class ) )
                        .collect( Collectors.toList() );
            } else {
                fields = ReflectUtil.getAllFields( obj.getClass() ).stream()
                        .filter( f -> !f.isAnnotationPresent( Manage.class )
                                || ( f.isAnnotationPresent( Manage.class )
                                && f.getAnnotation( Manage.class ).save() )
                        ).collect( Collectors.toList() );
            }
            List<Field> otherFile = fields.stream()
                    .filter( f -> f.getType().isAnnotationPresent( Configuration.class ) )
                    .collect( Collectors.toList() );
            fields.removeAll( otherFile );
            fields.forEach( field -> {
                final Key key = field.getAnnotation( Key.class );
                final String name = key != null ? key.value() : field.getName();
                try {
                    fileConfiguration.set( name, FieldUtils.readField( field, obj, true ) );
                } catch ( IllegalAccessException e ) {
                    plugin.getLogger().warning( "[ConfigurationLoader] Can't save the value of " + field.getName() + " in " + configuration.value() );
                }

            } );
            otherFile.forEach( field -> {
                try {
                    save( FieldUtils.readField( field, obj, true ) );
                } catch ( IllegalAccessException e ) {
                    plugin.getLogger().warning( "[ConfigurationLoader] Can't save the value of " + field.getName() + " in " + configuration.value() );
                }
            } );
            try {
                fileConfiguration.save( file );
            } catch ( IOException e ) {
                plugin.getLogger().warning( "Can't save the FileConfiguration in the file" );
            }
        }
    }

    /**
     * This method create a {@link File} instance depending of the {@link Configuration}
     * If the file doesn't exists he will be created (blank or from resource) according to
     * the Configuration parameter values
     *
     * @param configuration The configuration annotation
     * @return The file created
     */
    @NotNull
    private File create( @NotNull Configuration configuration ) {
        String name = configuration.value().endsWith( ".yml" ) ? configuration.value() : configuration.value() + ".yml";
        final File file = new File( plugin.getDataFolder() + "/" + configuration.path(), name );
        boolean created = file.getParentFile().exists();
        if ( !created || !file.exists() )
            created = file.getParentFile().mkdirs() && file.exists();
        if ( !created ) {
            if ( configuration.resource() ) {
                try ( final InputStream stream = plugin.getResource( file.getName() ) ) {
                    if ( stream != null ) {
                        Files.copy( stream, file.toPath() );
                        plugin.getLogger().info( "[ConfigurationLoader] Created new resource configuration file: " + file.getName() );
                    } else {
                        throw new IOException( "" );
                    }
                } catch ( IOException e ) {
                    plugin.getLogger().severe( "[ConfigurationLoader] Can't create " + configuration.value() + " file from resources." );
                }
            } else {
                try {
                    created = file.createNewFile();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
                if ( created ) {
                    plugin.getLogger().info( "[ConfigurationLoader] Created new blank configuration file: " + configuration.value() );
                }
            }
        }
        return file;
    }

}
