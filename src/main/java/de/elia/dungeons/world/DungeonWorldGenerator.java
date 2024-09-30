package de.elia.dungeons.world;

import de.elia.api.logging.PluginLogger;
import de.elia.dungeons.DungeonsMain;
import de.elia.systemclasses.worldsettings.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DungeonWorldGenerator extends ChunkGenerator {

  private final WorldSettings settings = new WorldSettings(null, false, false, false, false, false, false);
  private final PluginLogger worldLogger = DungeonsMain.dungeonsMain().dungeonWorldLogger();

  @Override
  @Nullable
  public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
    this.worldLogger.logInfo("Set Spawnlocation...");
    Location location = new Location(world, 0, 0, 0);
    this.worldLogger.logInfo("Spawnlocation sets!");
    return location;
  }

  @Override
  public boolean shouldGenerateCaves() {
    return this.settings.caves();
  }

  @Override
  public boolean shouldGenerateDecorations() {
    return this.settings.decorations();
  }

  @Override
  public boolean shouldGenerateMobs() {
    return this.settings.mobs();
  }

  @Override
  public boolean shouldGenerateNoise() {
    return this.settings.noise();
  }

  @Override
  public boolean shouldGenerateStructures() {
    return this.settings.structures();
  }

  @Override
  public boolean shouldGenerateSurface() {
    return this.settings.surface();
  }

  public class WorldLoader {

    private final World world;
    private final WorldCreator worldCreator;
    private final String id;

    public WorldLoader(String id) {
      DungeonWorldGenerator.this.worldLogger.logInfo("0%");
      DungeonWorldGenerator.this.worldLogger.logInfo("Create a new World... (" + id + ")");
      this.id = id;
      DungeonWorldGenerator.this.worldLogger.logInfo("Load org.bukkit.WorldGenerator...");
      this.worldCreator = new WorldCreator(id);
      DungeonWorldGenerator.this.worldLogger.logInfo("org.bukkit.WorldGenerator loaded!");
      DungeonWorldGenerator.this.worldLogger.logInfo("25%");
      DungeonWorldGenerator.this.worldLogger.logInfo("Load de.elia.bossfightcreator.world.CustomChunkGenerator...");
      DungeonWorldGenerator generator = new DungeonWorldGenerator();
      DungeonWorldGenerator.this.worldLogger.logInfo("de.elia.bossfightcreator.world.CustomChunkGenerator loaded!");
      DungeonWorldGenerator.this.worldLogger.logInfo("50%");
      DungeonWorldGenerator.this.worldLogger.logInfo("Set the custom generator to the WorldGenerator...");
      this.worldCreator.generator(generator);
      DungeonWorldGenerator.this.worldLogger.logInfo("The custom generator to the WorldGenerator sets!");
      DungeonWorldGenerator.this.worldLogger.logInfo("75%");
      DungeonWorldGenerator.this.worldLogger.logInfo("Create a new org.bukkit.World...");
      this.world = Bukkit.createWorld(this.worldCreator);
      world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
      world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
      world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
      world.setGameRule(GameRule.KEEP_INVENTORY, true);
      DungeonWorldGenerator.this.worldLogger.logInfo("A new org.bukkit.World is created!");
      DungeonWorldGenerator.this.worldLogger.logInfo("Ending world creation progress...");
      DungeonWorldGenerator.this.worldLogger.logInfo("100%");
      DungeonWorldGenerator.this.worldLogger.logInfo("world creation progress ended!");
    }

    @NotNull
    public World getWorld() {
      return this.world;
    }

    @NotNull
    public WorldCreator getWorldCreator() {
      return this.worldCreator;
    }
  }

}
