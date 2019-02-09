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
package net.frozenspace.frostlib.component;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author MiniDigger (https://www.spigotmc.org/members/minidigger.6039)
 */
public class ComponentSerializer {

    @NotNull
    public static String serialize( @NotNull BaseComponent... components ) {
        StringBuilder sb = new StringBuilder();

        for ( int i = 0; i < components.length; i++ ) {
            BaseComponent comp = components[i];

            // # start tags

            // ## get prev comp
            BaseComponent prevComp = null;
            if ( i > 0 ) {
                prevComp = components[i - 1];
            }

            // ## color
            // ### white is not important
            if ( !ChatColor.WHITE.equals( comp.getColor() ) ) {
                sb.append( startColor( comp.getColor() ) );
            }

            // ## decoration
            // ### only start if prevComp didn't start
            if ( comp.isBold() && ( prevComp == null || !prevComp.isBold() ) ) {
                sb.append( startTag( "bold" ) );
            }
            if ( comp.isItalic() && ( prevComp == null || !prevComp.isItalic() ) ) {
                sb.append( startTag( "italic" ) );
            }
            if ( comp.isObfuscated() && ( prevComp == null || !prevComp.isObfuscated() ) ) {
                sb.append( startTag( "obfuscated" ) );
            }
            if ( comp.isStrikethrough() && ( prevComp == null || !prevComp.isStrikethrough() ) ) {
                sb.append( startTag( "strikethrough" ) );
            }
            if ( comp.isUnderlined() && ( prevComp == null || !prevComp.isUnderlined() ) ) {
                sb.append( startTag( "underlined" ) );
            }

            // ## hover
            // ### only start if prevComp didn't start the same one
            HoverEvent hov = comp.getHoverEvent();
            if ( hov != null && ( prevComp == null || !equals( hov, prevComp.getHoverEvent() ) ) ) {
                sb.append( startTag(
                        "hover:" + hov.getAction().name().toLowerCase() + ":\"" + serialize( hov.getValue() ) + "\"" ) );
            }

            // ## click
            // ### only start if prevComp didn't start the same one
            ClickEvent click = comp.getClickEvent();
            if ( click != null && ( prevComp == null || !equals( click, prevComp.getClickEvent() ) ) ) {
                sb.append(
                        startTag( "click:" + click.getAction().name().toLowerCase() + ":\"" + click.getValue() + "\"" ) );
            }

            // # append text
            sb.append( comp.toPlainText() );

            // # end tags

            // ## get next comp
            BaseComponent nextComp = null;
            if ( i + 1 < components.length ) {
                nextComp = components[i + 1];
            }

            // ## color
            // ### only end color if next comp is white and curren't isn't
            if ( nextComp != null && comp.getColor() != ChatColor.WHITE ) {
                if ( nextComp.getColor() == ChatColor.WHITE || nextComp.getColor() == null ) {
                    sb.append( endColor( comp.getColor() ) );
                }
            }

            // ## decoration
            // ### only end decoration if next tag is different
            if ( nextComp != null ) {
                if ( comp.isBold() && !nextComp.isBold() ) {
                    sb.append( endTag( "bold" ) );
                }
                if ( comp.isItalic() && !nextComp.isItalic() ) {
                    sb.append( endTag( "italic" ) );
                }
                if ( comp.isObfuscated() && !nextComp.isObfuscated() ) {
                    sb.append( endTag( "obfuscated" ) );
                }
                if ( comp.isStrikethrough() && !nextComp.isStrikethrough() ) {
                    sb.append( endTag( "strikethrough" ) );
                }
                if ( comp.isUnderlined() && !nextComp.isUnderlined() ) {
                    sb.append( endTag( "underlined" ) );
                }
            }

            // ## hover
            // ### only end hover if next tag is different
            if ( nextComp != null && comp.getHoverEvent() != null ) {
                if ( !equals( comp.getHoverEvent(), nextComp.getHoverEvent() ) ) {
                    sb.append( endTag( "hover" ) );
                }
            }

            // ## click
            // ### only end click if next tag is different
            if ( nextComp != null && comp.getClickEvent() != null ) {
                if ( !equals( comp.getClickEvent(), nextComp.getClickEvent() ) ) {
                    sb.append( endTag( "click" ) );
                }
            }
        }

        return sb.toString();
    }

    private static boolean equals( ClickEvent c1, ClickEvent c2 ) {
        if ( c2 == null || c1 == null ) return false;
        return c1.equals( c2 ) || ( c1.getAction().equals( c2.getAction() ) && c1.getValue().equals( c2.getValue() ) );
    }

    private static boolean equals( HoverEvent h1, HoverEvent h2 ) {
        if ( h2 == null || h1 == null ) return false;
        return h1.equals( h2 ) || ( h1.getAction().equals( h2.getAction() ) );
    }

    private static String startColor( ChatColor color ) {
        return startTag( color.name().toLowerCase() );
    }

    private static String endColor( ChatColor color ) {
        return endTag( color.name().toLowerCase() );
    }

    private static String startTag( String content ) {
        return String.format( "<%s>", content );
    }

    private static String endTag( String content ) {
        return String.format( "</%s>", content );
    }
}
