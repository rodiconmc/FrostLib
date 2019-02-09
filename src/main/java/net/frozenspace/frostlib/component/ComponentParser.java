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
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MiniDigger (https://www.spigotmc.org/members/minidigger.6039)
 */
public class ComponentParser {

    private static Pattern pattern = Pattern.compile( "((?<start><)(?<token>([^<>]+)|([^<>]+\"(?<inner>[^\"]+)\"))(?<end>>))+?" );

    @NotNull
    public static String escapeTokens( @NotNull String richMessage ) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern.matcher( richMessage );
        int lastEnd = 0;
        while ( matcher.find() ) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            if ( startIndex > lastEnd ) {
                sb.append( richMessage, lastEnd, startIndex );
            }
            lastEnd = endIndex;

            String start = matcher.group( "start" );
            String token = matcher.group( "token" );
            String inner = matcher.group( "inner" );
            String end = matcher.group( "end" );

            // also escape inner
            if ( inner != null ) {
                token = token.replace( inner, escapeTokens( inner ) );
            }

            sb.append( "\\" ).append( start ).append( token ).append( "\\" ).append( end );
        }

        if ( richMessage.length() > lastEnd ) {
            sb.append( richMessage.substring( lastEnd ) );
        }

        return sb.toString();
    }

    @NotNull
    public static String stripTokens( @NotNull String richMessage ) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern.matcher( richMessage );
        int lastEnd = 0;
        while ( matcher.find() ) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            if ( startIndex > lastEnd ) {
                sb.append( richMessage, lastEnd, startIndex );
            }
            lastEnd = endIndex;
        }

        if ( richMessage.length() > lastEnd ) {
            sb.append( richMessage.substring( lastEnd ) );
        }

        return sb.toString();
    }

    @NotNull
    public static String handlePlaceholders( @NotNull String richMessage, @NotNull String... placeholders ) {
        if ( placeholders.length % 2 != 0 ) {
            throw new RuntimeException(
                    "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)" );
        }
        for ( int i = 0; i < placeholders.length; i += 2 ) {
            richMessage = richMessage.replace( "<" + placeholders[i] + ">", placeholders[i + 1] );
        }
        return richMessage;
    }

    @NotNull
    public static String handlePlaceholders( @NotNull String richMessage, @NotNull Map<String, String> placeholders ) {
        for ( Map.Entry<String, String> entry : placeholders.entrySet() ) {
            richMessage = richMessage.replace( "<" + entry.getKey() + ">", entry.getValue() );
        }
        return richMessage;
    }

    @NotNull
    public static BaseComponent[] parseFormat( @NotNull String richMessage, @NotNull String... placeholders ) {
        return parseFormat( handlePlaceholders( richMessage, placeholders ) );
    }

    @NotNull
    public static BaseComponent[] parseFormat( @NotNull String richMessage, @NotNull Map<String, String> placeholders ) {
        return parseFormat( handlePlaceholders( richMessage, placeholders ) );
    }

    @NotNull
    public static BaseComponent[] parseFormat( @NotNull String richMessage ) {
        ComponentBuilder builder = null;

        Stack<ClickEvent> clickEvents = new Stack<>();
        Stack<HoverEvent> hoverEvents = new Stack<>();
        Stack<ChatColor> colors = new Stack<>();
        EnumSet<TextDecoration> decorations = EnumSet.noneOf( TextDecoration.class );

        Matcher matcher = pattern.matcher( richMessage );
        int lastEnd = 0;
        while ( matcher.find() ) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            String msg = null;
            if ( startIndex > lastEnd ) {
                msg = richMessage.substring( lastEnd, startIndex );
            }
            lastEnd = endIndex;

            // handle component
            if ( msg != null && msg.length() != 0 ) {
                // append component
                if ( builder == null ) {
                    builder = new ComponentBuilder( msg );
                } else {
                    builder.append( msg, ComponentBuilder.FormatRetention.NONE );
                }

                // set everything that is not closed yet
                if ( clickEvents.size() > 0 ) {
                    builder.event( clickEvents.peek() );
                }
                if ( hoverEvents.size() > 0 ) {
                    builder.event( hoverEvents.peek() );
                }
                if ( colors.size() > 0 ) {
                    builder.color( colors.peek() );
                }
                if ( decorations.size() > 0 ) {
                    // no lambda because builder isn't effective final :/
                    for ( TextDecoration decor : decorations ) {
                        decor.apply( builder );
                    }
                }
            }

//            String group = matcher.group();
//            String start = matcher.group("start");
            String token = matcher.group( "token" );
            String inner = matcher.group( "inner" );
//            String end = matcher.group("end");

            Optional<TextDecoration> deco;
            Optional<ChatColor> color;

            // click
            if ( token.startsWith( "click:" ) ) {
                clickEvents.push( handleClick( token, inner ) );
            } else if ( token.equals( "/click" ) ) {
                clickEvents.pop();
            }
            // hover
            else if ( token.startsWith( "hover:" ) ) {
                hoverEvents.push( handleHover( token, inner ) );
            } else if ( token.equals( "/hover" ) ) {
                hoverEvents.pop();
            }
            // decoration
            else if ( ( deco = resolveDecoration( token ) ).isPresent() ) {
                decorations.add( deco.get() );
            } else if ( token.startsWith( "/" ) && ( deco = resolveDecoration( token.replace( "/", "" ) ) ).isPresent() ) {
                decorations.remove( deco.get() );
            }
            // color
            else if ( ( color = resolveColor( token ) ).isPresent() ) {
                colors.push( color.get() );
            } else if ( token.startsWith( "/" ) && resolveColor( token.replace( "/", "" ) ).isPresent() ) {
                colors.pop();
            }
        }

        // handle last component part
        if ( richMessage.length() > lastEnd ) {
            String msg = richMessage.substring( lastEnd );
            // append component
            if ( builder == null ) {
                builder = new ComponentBuilder( msg );
            } else {
                builder.append( msg, ComponentBuilder.FormatRetention.NONE );
            }

            // set everything that is not closed yet
            if ( clickEvents.size() > 0 ) {
                builder.event( clickEvents.peek() );
            }
            if ( hoverEvents.size() > 0 ) {
                builder.event( hoverEvents.peek() );
            }
            if ( colors.size() > 0 ) {
                builder.color( colors.peek() );
            }
            if ( decorations.size() > 0 ) {
                // no lambda because builder isn't effective final :/
                for ( TextDecoration decor : decorations ) {
                    decor.apply( builder );
                }
            }
        }

        if ( builder == null ) {
            // lets just return an empty component
            builder = new ComponentBuilder( "" );
        }

        return builder.create();
    }

    @NotNull
    private static ClickEvent handleClick( @NotNull String token, @NotNull String inner ) {
        String[] args = token.split( ":" );
        ClickEvent clickEvent;
        if ( args.length < 2 ) {
            throw new RuntimeException( "Can't parse click action (too few args) " + token );
        }
        switch ( args[1] ) {
            case "run_command":
                clickEvent = new ClickEvent( ClickEvent.Action.RUN_COMMAND, token.replace( "click:run_command:", "" ) );
                break;
            case "suggest_command":
                clickEvent = new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, token.replace( "click:suggest_command:", "" ) );
                break;
            case "open_url":
                clickEvent = new ClickEvent( ClickEvent.Action.OPEN_URL, token.replace( "click:open_url:", "" ) );
                break;
            case "change_page":
                clickEvent = new ClickEvent( ClickEvent.Action.CHANGE_PAGE, token.replace( "click:change_page:", "" ) );
                break;
            default:
                throw new RuntimeException( "Can't parse click action (invalid type " + args[1] + ") " + token );
        }
        return clickEvent;
    }

    @NotNull
    private static HoverEvent handleHover( @NotNull String token, @NotNull String inner ) {
        String[] args = token.split( ":" );
        HoverEvent hoverEvent;
        if ( args.length < 2 ) {
            throw new RuntimeException( "Can't parse hover action (too few args) " + token );
        }
        switch ( args[1] ) {
            case "show_text":
                hoverEvent = new HoverEvent( HoverEvent.Action.SHOW_TEXT, parseFormat( inner ) );
                break;
            case "show_item":
                hoverEvent = new HoverEvent( HoverEvent.Action.SHOW_ITEM, parseFormat( inner ) );
                break;
            case "show_entity":
                hoverEvent = new HoverEvent( HoverEvent.Action.SHOW_ENTITY, parseFormat( inner ) );
                break;
            default:
                throw new RuntimeException( "Can't parse hover action (invalid type " + args[1] + ") " + token );
        }
        return hoverEvent;
    }

    @NotNull
    private static Optional<ChatColor> resolveColor( @NotNull String token ) {
        try {
            return Optional.of( ChatColor.valueOf( token.toUpperCase() ) );
        } catch ( IllegalArgumentException ex ) {
            return Optional.empty();
        }
    }

    @NotNull
    private static Optional<TextDecoration> resolveDecoration( @NotNull String token ) {
        try {
            return Optional.of( TextDecoration.valueOf( token.toUpperCase() ) );
        } catch ( IllegalArgumentException ex ) {
            return Optional.empty();
        }
    }

    enum TextDecoration {

        BOLD( builder -> builder.bold( true ) ),
        ITALIC( builder -> builder.italic( true ) ),
        UNDERLINED( builder -> builder.underlined( true ) ),
        STRIKETHROUGH( builder -> builder.strikethrough( true ) ),
        OBFUSCATED( builder -> builder.obfuscated( true ) );

        private Consumer<ComponentBuilder> builder;

        TextDecoration( Consumer<ComponentBuilder> builder ) {
            this.builder = builder;
        }

        public void apply( ComponentBuilder comp ) {
            builder.accept( comp );
        }

    }
}