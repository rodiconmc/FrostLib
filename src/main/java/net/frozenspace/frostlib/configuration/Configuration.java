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

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used for define a class as
 * a configuration yaml file, all fields in this file
 * will be loaded using {@link ConfigurationLoader#load}
 *
 * @author Frozen
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface Configuration {

    /**
     * Name of the file to create/load without the extension
     *
     * @return The value of the file
     */
    @NotNull
    String value();

    /**
     * The path of the file to create/load
     * This value will be appended to {@link Plugin#getDataFolder()}
     *
     * @return The path of the file
     */
    @NotNull
    String path() default "";

    /**
     * Define if the file will be created from plugin resources
     * or if he will be a blank fill when created
     *
     * @return If the file is created from resource or not
     */
    boolean resource() default true;

}
