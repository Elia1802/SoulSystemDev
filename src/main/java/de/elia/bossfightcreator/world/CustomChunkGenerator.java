package de.elia.bossfightcreator.world;

import de.elia.Main;
import de.elia.systemclasses.worldsettings.WorldSettings;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

//This class overrides the Chunkgenerator of the World "world_bossfight"
public class CustomChunkGenerator extends ChunkGenerator {

  private final WorldSettings settings = new WorldSettings(null, false, false, false, false, false, false);

  /**
   * Gets a fixed spawn location to use for a given world.
   * A null value is returned if a world should not use a fixed spawn point, and will instead attempt to find one randomly.
   * @param world The world to locate a spawn point for
   * @param random Random generator to use in the calculation
   * @return
   */
  @Override
  @Nullable
  public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
    WorldMain worldMain = Main.main().worldMain();
    worldMain.logInfo("Set Spawnlocation...");
    Location location = new Location(world, 0, 0, 0);
    worldMain.logInfo("Spawnlocation sets!");
    return location;
  }


  /**
   * Gets if the server should generate Vanilla caves.
   * @return true if the server should generate Vanilla caves
   */
  @Override
  public boolean shouldGenerateCaves() {
    return this.settings.caves();
  }

  /**
   * Gets if the server should generate Vanilla decorations after this ChunkGenerator.
   * @return true if the server should generate Vanilla decorations
   */
  @Override
  public boolean shouldGenerateDecorations() {
    return this.settings.decorations();
  }

  /**
   * Gets if the server should generate Vanilla mobs after this ChunkGenerator.
   * @return true if the server should generate Vanilla mobs
   */
  @Override
  public boolean shouldGenerateMobs() {
    return this.settings.mobs();
  }

  /**
   * Gets if the server should generate Vanilla noise.
   * @return true if the server should generate Vanilla noise
   */
  @Override
  public boolean shouldGenerateNoise() {
    return this.settings.noise();
  }

  /**
   * Gets if the server should generate Vanilla structures after this ChunkGenerator.
   * @return true if the server should generate Vanilla structures
   */
  @Override
  public boolean shouldGenerateStructures() {
    return this.settings.structures();
  }

  /**
   * Gets if the server should generate Vanilla surface.
   * @return true if the server should generate Vanilla surface
   */
  @Override
  public boolean shouldGenerateSurface() {
    return this.settings.surface();
  }

}
