package de.elia.systemclasses.plugin;

import de.elia.api.loader.SoulPlugin;
import de.elia.api.loader.exceptions.SoulPluginLoadException;
import de.elia.api.logging.PluginLogger;

import de.elia.systemclasses.plugin.configuation.SoulBossSystemConfigurationLoader;
import de.elia.systemclasses.Instances;

import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

//This class is the main class of the soulbosssystem plugin
public class SoulBossSystemMain implements SoulPlugin {

  private static SoulBossSystemMain soulBossSystemMain;

  //This methode loads this plugin.
  public void enable(@NotNull JavaPlugin javaPlugin) throws SoulPluginLoadException {
    soulBossSystemMain = this;
    this.soulBossSystemLogger().logInfo("Load SoulBossSystem plugin...");
    this.soulBossSystemLogger().logInfo("Load files...");
    SoulBossSystemConfigurationLoader.loadFiles(javaPlugin);
    this.soulBossSystemLogger().logInfo("files loaded!");
    this.soulBossSystemLogger().logInfo("SoulBossSystem plugin loaded!");
  }

  //Gets the logger for this plugin.
  public void disable(@NotNull JavaPlugin javaPlugin) throws SoulPluginLoadException {
    SoulBossSystemConfigurationLoader.save(javaPlugin);
  }

  //Gets this class.
  @NotNull
  public PluginLogger soulBossSystemLogger() {
    return Instances.SOUL_BOSS_SYSTEM_LOGGER;
  }

  //Gets this class.
  @NotNull
  public static SoulBossSystemMain soulBossSystemMain() {
    return soulBossSystemMain;
  }

  //Gets the instance, of the main class, of this system.
  @NotNull
  public Main main() {
    return Main.main();
  }
}
