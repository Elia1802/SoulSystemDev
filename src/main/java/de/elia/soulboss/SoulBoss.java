package de.elia.soulboss;

import de.elia.Main;
import de.elia.api.loader.SoulPlugin;
import de.elia.api.loader.exceptions.SoulPluginException;
import de.elia.api.logging.PluginLogger;

import de.elia.systemclasses.register.utils.UtilsLoader;
import de.elia.systemclasses.Instances;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

//This class is the main class of the soulboss plugin
public class SoulBoss implements SoulPlugin {

  private static SoulBoss soulboss;

  //This methode loads this plugin.
  public void enable(@NotNull JavaPlugin javaPlugin) {
    this.soulBossLogger().logInfo("Load SoulBoss...");
    soulboss = this;
    this.soulBossLogger().logInfo("Load utils...");
    UtilsLoader.loadUtils(javaPlugin);
    this.soulBossLogger().logInfo("Utils loaded!");
    this.soulBossLogger().logInfo("SoulBoss loaded!");
  }

  //This methode unloads this plugin.
  public void disable(@NotNull JavaPlugin javaPlugin) {
    Bukkit.getServer().getWorld("world_bossfight").getEntities().forEach(entity -> {
      if (entity.getType() == EntityType.ZOMBIE) {
        if (entity.getPersistentDataContainer().has(NameSpacedKeys.ZOMBIE_KEY.key())) {
          entity.remove();
        }
      }else if (entity.getType() == EntityType.CREEPER) {
        if (entity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
          entity.remove();
        }
      }
    });
  }

  //Gets this class.
  @NotNull
  public static SoulBoss soulBoss() {
    return soulboss;
  }

  //Gets the instance, of the main class, of this system.
  @NotNull
  public Main main() {
    return Main.main();
  }

  //Gets the logger for this plugin.
  @NotNull
  public PluginLogger soulBossLogger() {
    return Instances.SOUL_BOSS_LOGGER;
  }
}
