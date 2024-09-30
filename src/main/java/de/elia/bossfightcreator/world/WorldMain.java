package de.elia.bossfightcreator.world;

import de.elia.api.logging.PluginLogger;

import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

import java.io.File;

//This class set a logger for the world creation and load the path for the arena files.
public class WorldMain {

  private final Plugin plugin;
  private File file;
  private final PluginLogger logger = new PluginLogger("SoulBossSystem -> World");

  public WorldMain(Plugin plugin) {
    this.plugin = plugin;
    this.loadFolder();
  }

  //log a message in the console.
  public void logInfo(@NotNull String message) {
    this.logger.logInfo(message);
  }

  //Load the path for the arena
  private void loadFolder() {
    this.file = new File(this.plugin.getDataFolder() + "/arenas/");
    if (!this.file.exists()) {
      this.file.mkdir();
    }
  }

  //Gets the arena path
  public File arenaFolder() {
    return this.file;
  }
}
