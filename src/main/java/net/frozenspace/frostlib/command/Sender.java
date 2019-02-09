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

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This enum contains all available CommandSender
 *
 * @author Frozen
 */
public enum Sender {

    PLAYER( Player.class ),
    CONSOLE( ConsoleCommandSender.class ),
    RCON( RemoteConsoleCommandSender.class ),
    ALL( CommandSender.class );

    @NotNull
    private Class clazz;

    /**
     * Constructor used for initializing the clazz field
     *
     * @param clazz Class of the sender
     * @param <T>   Type extending CommandSender
     */
    <T extends CommandSender> Sender( @NotNull Class<T> clazz ) {
        this.clazz = clazz;
    }

    /**
     * Get the CommandSender class of the sender
     *
     * @return The CommandSender class
     */
    @NotNull
    public Class get() {
        return clazz;
    }

}
