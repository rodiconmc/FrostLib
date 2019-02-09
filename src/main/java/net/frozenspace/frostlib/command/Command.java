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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used for defining a method to be a
 * PluginCommand, the corp of the method will be executed
 * when someone will perform a command with the selected name
 *
 * @author Frozen
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Command {

    /**
     * The name of the command is what the player need to type
     * for executing this command
     *
     * @return The name of this command
     */
    String name();

    /**
     * The description of the command is what is show to players
     * when using /help
     *
     * @return The description of this command
     */
    String description();

    /**
     * The permission is the permission needed by the command sender
     * to perform this command by default there is no permission needed
     *
     * @return The permission needed to perform this command
     */
    String permission() default "";

    /**
     * Usage is the message sent to the player when the command
     * is badly formatted the default value is /commandname
     *
     * @return The usage message of this command
     */
    String usage() default "/<command>";

    /**
     * The command method will be executed only if the sender
     * of the command is the sender needed
     * by default all sender are allowed
     *
     * @return The sender who should use the command
     */
    Sender sender() default Sender.ALL;

    /**
     * The command aliases are used for perform a command using aliases
     *
     * @return The aliases of the command
     */
    String[] aliases() default { };

    /**
     * Define if the command method have a {@link Completer} method
     * used for tab completion somewhere in the class by default it's set to false
     *
     * @return Yes if tab completion is enabled false if not
     */
    boolean completion() default false;

    /**
     * Define the max args allowed for perform this command
     * If player enter more args than this value the command will not be
     * executed by default the value is {@link Integer#MAX_VALUE}
     *
     * @return The max args allowed for this command
     */
    int maxArgs() default Integer.MAX_VALUE;

    /**
     * Define the min args allowed for perform this command
     * If player enter less args than this value the command will not be
     * executed by default the value is 0
     *
     * @return The min args allowed for this command
     */
    int minArgs() default 0;

    /**
     * The message send to player who perform this command without
     * the permission by default it's set to
     * "You don't have the permission to perform this command"
     *
     * @return The no permission message to send
     */
    String permissionMessage() default "You don't have the permission to perform this command";

}
