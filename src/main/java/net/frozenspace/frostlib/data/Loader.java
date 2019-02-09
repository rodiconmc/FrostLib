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
package net.frozenspace.frostlib.data;


import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * This class is used for defining if a class
 * is used for loading some value
 *
 * @author Frozen
 */
public abstract class Loader {

    /**
     * Create a new instance of the class and class {@link #load(Object)}
     * using the this new class instance as parameter
     *
     * @param clazz The class to load
     * @param <T>   The type of this class
     * @return The object loaded with {@link #load(Object)}
     */
    @NotNull
    public <T> T load( @NotNull Class<T> clazz ) {
        Validate.notNull( clazz, "[Loader] Trying to load a null class." );
        T obj;
        try {
            obj = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e ) {
            throw new RuntimeException( "[Loader] Can't instantiate " + clazz.getSimpleName() + "." );
        }
        return load( obj );
    }

    @NotNull
    public abstract <T> T load( @NotNull T obj );

}
