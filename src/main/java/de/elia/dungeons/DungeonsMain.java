package de.elia.dungeons;

import de.elia.Main;
import de.elia.api.loader.SoulPlugin;
import de.elia.api.loader.exceptions.SoulPluginLoadException;
import de.elia.api.logging.PluginLogger;
import de.elia.systemclasses.Instances;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DungeonsMain implements SoulPlugin {

  private static DungeonsMain dungeonsMain;

  @Override
  public void enable(@NotNull JavaPlugin javaPlugin) throws SoulPluginLoadException {
    dungeonsMain = this;
  }

  @Override
  public void disable(@NotNull JavaPlugin javaPlugin) throws SoulPluginLoadException {
    //...
  }

  @NotNull
  public static DungeonsMain dungeonsMain() {
    return dungeonsMain;
  }

  @NotNull
  public Main main() {
    return Main.main();
  }

  @NotNull
  public PluginLogger dungeonLogger() {
    return Instances.DUNGEONS_LOGGER;
  }

  @NotNull
  public PluginLogger dungeonWorldLogger(){
    return Instances.DUNGEONS_LOGGER_WORLD;
  }
}
