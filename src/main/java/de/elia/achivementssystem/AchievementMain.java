package de.elia.achivementssystem;

import de.elia.Main;
import de.elia.api.loader.SoulPlugin;
import de.elia.api.loader.exceptions.SoulPluginLoadException;
import de.elia.api.logging.PluginLogger;
import de.elia.systemclasses.Instances;

import de.elia.systemclasses.database.AchievementDataBase;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 * The Main of the Achievement Plugin
 */
public class AchievementMain implements SoulPlugin {

  private static AchievementMain achievementMain;
  private final AchievementDB achievementDataBase = new AchievementDataBase();

  /**
   * Load the Achievement Plugin
   * @param javaPlugin Requires the main class of the system
   * @throws SoulPluginLoadException If an error by loading the plugin, creates a stacktrace
   */
  @Override
  public void enable(@NotNull JavaPlugin javaPlugin) throws SoulPluginLoadException {
    this.achievementPluginLogger().logInfo("Load Achievement plugin...");
    achievementMain = this;
    try {
      achievementDataBase.loadDatabase();
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
    this.achievementPluginLogger().logInfo("Achievement plugin loaded!");
  }


  /**
   * Unload the Achievement Plugin
   * @param javaPlugin Requires the main class of the system
   * @throws SoulPluginLoadException If an error by unloading the plugin, creates a stacktrace
   */
  @Override
  public void disable(@NotNull JavaPlugin javaPlugin) throws SoulPluginLoadException {
    try {
      if (!achievementDataBase.connection().isClosed()) {
        achievementDataBase.connection().close();
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
    this.achievementPluginLogger().logInfo("Achievement disabled!");
  }

  /**
   * Gets the instance of this main class
   * @return this class
   */
  @NotNull
  public static AchievementMain achievementMain() {
    return achievementMain;
  }

  /**
   * Gets the Logger of the Achievement plugin.
   * @return the logger instance of this plugin
   */
  @NotNull
  public PluginLogger achievementPluginLogger() {
    return Instances.ACHIEVEMENT_LOGGER;
  }

  /**
   * Gets the Main class of this system
   * @return the instance
   */
  @NotNull
  public Main main() {
    return Main.main();
  }

  /**
   * Gets the instance of the achievement db
   * @return the database instance
   */
  @NotNull
  public AchievementDB getAchievementDataBase() {
    return achievementDataBase;
  }
}
