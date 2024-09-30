package de.elia.systemclasses.plugin.configuation;

import de.elia.api.configuration.SoulConfiguration;

import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

//This class manages all important files
public class SoulBossSystemConfigurationLoader {
  private static SoulConfiguration achievementStorage;
  private static SoulConfiguration ideasStorage;

  //Set the configs
  private static void setConfiguration(Plugin plugin) {
    achievementStorage = new SoulConfiguration(plugin, "achievements/", "Achievements.yml");
    ideasStorage = new SoulConfiguration(plugin, "ideas/", "Ideas.yml");
  }

  //Load all files
  public static void loadFiles(Plugin plugin) {
    SoulBossSystemConfigurationLoader.setConfiguration(plugin);
    achievementStorage.copyDefaults(true);
    achievementStorage.save();
    ideasStorage.copyDefaults(true);
    ideasStorage.save();
  }

  //Save all files
  public static void save(Plugin plugin) {
    achievementStorage.save();
    if (ideasStorage == null) {
      SoulBossSystemConfigurationLoader.setConfiguration(plugin);
    }
    ideasStorage.save();
  }

  //Save all files
  public static void reload(Plugin plugin) {
    SoulBossSystemConfigurationLoader.save(plugin);
    SoulBossSystemConfigurationLoader.loadFiles(plugin);
  }

  //Gets the achievement file
  @NotNull
  public static SoulConfiguration achievementStorage() {
    return achievementStorage;
  }

  //Gets the idea file
  @NotNull
  public static SoulConfiguration ideasStorage() {
    return ideasStorage;
  }

}
