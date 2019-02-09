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
package net.frozenspace.frostlib.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains some util method for reflections
 *
 * @author Frozen
 */
public class ReflectUtil {

    /**
     * Private constructor for preventing instantiation
     */
    private ReflectUtil() {
    }

    /**
     * Get all the field of the class and his superclass
     *
     * @param type Class to check
     * @return All the fields
     */
    public static List<Field> getAllFields( Class<?> type ) {
        List<Field> fields = new ArrayList<Field>();
        for ( Class<?> c = type; c != null; c = c.getSuperclass() ) {
            fields.addAll( Arrays.asList( c.getDeclaredFields() ) );
        }
        return fields;
    }

    /**
     * Get all the methods of the class and his superclass
     *
     * @param type Class to check
     * @return All the methods
     */
    public static List<Method> getAllMethods( Class<?> type ) {
        List<Method> methods = new ArrayList<>();
        for ( Class<?> c = type; c != null; c = c.getSuperclass() ) {
            methods.addAll( Arrays.asList( c.getDeclaredMethods() ) );
        }
        return methods;
    }

}
