package de.elia.bossfightcreator.world;

import de.elia.api.logging.PluginLogger;

import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * This class set a logger for the world creation and load the path for the arena files.
 * @author Elia
 * @since 2.0.0
 */
public class WorldMain {

  private final Plugin plugin;
  private File file;
  private final PluginLogger logger = new PluginLogger("SoulBossSystem -> World");

  /**
   * The main to for loading the world
   * @param plugin
   */
  public WorldMain(Plugin plugin) {
    this.plugin = plugin;
    //Load the folder for the arena schematics
    this.loadFolder();
  }

  /**
   * log a message in the console.
   * @param message the log message
   */
  public void logInfo(@NotNull String message) {
    this.logger.logInfo(message);
  }

  /**
   * Load the path for the arena
   */
  private void loadFolder() {
    this.file = new File(this.plugin.getDataFolder() + "/arenas/");
    if (!this.file.exists()) {
      this.file.mkdir();
    }
  }

  /**
   * Gets the arena path
   * @return the folder of the arena schematics
   */
  public File arenaFolder() {
    return this.file;
  }
}
