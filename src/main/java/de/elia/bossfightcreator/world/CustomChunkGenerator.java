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

  @Override
  @Nullable
  public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
    WorldMain worldMain = Main.main().worldMain();
    worldMain.logInfo("Set Spawnlocation...");
    Location location = new Location(world, 0, 0, 0);
    worldMain.logInfo("Spawnlocation sets!");
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

}
