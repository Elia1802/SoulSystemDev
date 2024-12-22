package de.elia;

import de.elia.events.PointEvents;
import de.elia.level.LevelUpListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
@Deprecated//USE THE MAIN OF THE SYSTEM
public class Main extends JavaPlugin {

    public final PluginManager pluginManager = this.getServer().getPluginManager();
    private static Main main;
    public static boolean isServerShuttingDown = false;

    @Override
    public void onEnable(){
        main = this;
        CommandRegister.registerCommands(main.getServer());
        pluginManager.registerEvents(new PointEvents(), main);
        pluginManager.registerEvents(new LevelUpListener(), main);
    }

    @Override
    public void onDisable(){
        isServerShuttingDown = true;
    }

    public static Main main() {
        return main;
    }
}
