# FrostLib

Library not made by us - Original author is FrozenLegend, who appears to have disappeared from github.

FrostLib contains several classes very useful for the development of minecraft plugin.  
This project was created to facilitate the plugin development and avoid to recode several times the same thing

## Packages available

- **net.frozenspace.frostlib.configuration** : Create/load/save configuration file using Annotation
- **net.frozenspace.frostlib.command** : Simply create commands using Annotation
- **net.frozenspace.frostlib.gui** : Create custom interactive GUI easily
- **net.frozenspace.frostlib.achievement** : Easily create custom achievement
- **net.frozenspace.frostlib.component** : Create message with format/placeholder support

### Prerequisites

- Spigot 1.13 or higher

### Examples

###### Create commands using net.frozenspace.frostlib.command
``` java
public class FrostLib extends JavaPlugin {

      private final CommandLoader commandLoader = new CommandLoader( this );

      @Override
      public void onEnable() {
          commandLoader.load( this );
      }

      @Command( name = "example", sender = Sender.PLAYER, description = "My example command description")
      public void onSampleCommand( Player player ) {
          player.sendMessage( "You performed the command example !" );
      }
      
}
```

###### Create/load/save configuration file using net.frozenspace.frostlib.configuration
``` java
public class FrostLib extends JavaPlugin {

    private final ConfigurationLoader configurationLoader = new ConfigurationLoader( this );
    private Messages messages;
    
    @Override
    public void onEnable() {
        messages = configurationLoader.load( Messages.class );
    }
    
    @Override
    public void onDisable() {
        configurationLoader.save( messages );
    }

}
```
```java
@Configuration( "messages" )
public class Messages {
    
    public String join;
    
    public String quit;
    
    @Key( "kills.creeper" )
    public String killByCreeper;
    
}
```
```yml
# messages.yml file
join: "My join message"
quit: "My quit message"
kills:
  creeper: "Killed by creeper"
```

###### Create GUI using net.frozenspace.frostlib.gui
```java
public class FrostLib extends JavaPlugin {

    private GUIManager manager;

    @Override
    public void onEnable() {
        manager = new GUIManager( this );
        SharedGUI shared = manager.getShared( "my_shared_gui" );
        Page page = shared.createPage( "My first page", Size.ONE_ROW );
        Pattern pattern =  page.createPattern()
                .setRow( Row.FIRST, "XXXXOXXXX" )
                .setKey( 'O', ActionItem.create( Material.CLOCK )
                        .name( ChatColor.AQUA + "Teleport to spawn")
                        .click( (player) -> player.teleport( player.getWorld().getSpawnLocation() ) )
                );
        page.applyPattern( pattern );
        shared.open( /* your player instance */ );
    }

}
```
###### Create achievements using net.frozenspace.frostlib.achievement
```java
public class FrostLib extends JavaPlugin {

    private final AchievementManager manager = new AchievementManager( this );

    @Override
    public void onEnable() {
        Achievement root = manager.build( "root", "I'm the root achievement" )
                .withBackground( Background.ADVENTURE )
                .withIcon( Material.DIAMOND )
                .withFrame( Frame.CHALLENGE )
                .withDescription( "I'm the root achievement description" )
                .addTrigger( new Trigger( "auto", TriggerType.LOCATION ) )
                .create();
        Achievement firstRootChild = manager.build( "root/1", "I'm the first root child" )
                .withIcon( Material.EMERALD )
                .withFrame( Frame.GOAL )
                .withDescription( "I'm the first root child description" )
                .withParent( root )
                .create();
    }

}
```
###### Create formatted message using net.frozenspace.frostlib.component
```java
player.spigot().sendMessage( ComponentParser.parseFormat( 
    "<red>Hi <dark_red><player></dark_red> what's up ?", 
    "user,", 
    player.getName() ) 
);
```

## Maven / Gradle
Maven:
Check here for the maven/gradle import instructions: https://bintray.com/rodiconmc/RodiconRepo/FrostLib/1.0.0

## Javadoc

https://frozenlegend.github.io/docs/FrostLib/

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
