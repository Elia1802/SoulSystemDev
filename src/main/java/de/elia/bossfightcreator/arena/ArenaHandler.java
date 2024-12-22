package de.elia.bossfightcreator.arena;

import de.elia.Main;
import de.elia.systemclasses.Instances;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static de.elia.api.messages.builder.MessageBuilder.*;

/**
 * This class handled and loaded all arenas.
 */
public class ArenaHandler {

  public static final File FILE_PATH = new File(Main.main().getDataFolder() + "/arenas/");//Path of the schematics
  private static final int AREANS_TO_GENERATE = 10;//Set how many arenas to generate
  private static final int ARENAS_X_DISTANCE = 200;//Set the distance to the next arena on the x coordinate
  private static final int ARENAS_Z_DISTANCE = 200;//Set the distance to the next arena on the z coordinate
  private static final HashMap<String, Set<Arena>> arenas = new HashMap<>();
  public static int ARENA_OFFSET = 0;

  /**
   * Gets a free arena.
   * @param type Requires the mob type
   * @param gameOwner Requires the future game owner
   * @return a free arena with the matching requirements
   */
  public static @NotNull Optional<Arena> getFreeArena(@NotNull ArenaMobType type, Player gameOwner) {
    return ArenaHandler.getArenaWithType(ArenaState.FREE, type, gameOwner);
  }

  /**
   * Gets an arena with a specified state of a list.
   * @param arenaState Requires the state of the arena
   * @param type Requires the mob type
   * @param gameOwner Requires the future game owner
   * @return an arena with the matching requirements
   */
  @NotNull
  public static Optional<Arena> getArenaWithType(@NotNull ArenaState arenaState, @NotNull ArenaMobType type, Player gameOwner) {
    try {
      Optional<ArrayList<Arena>> t = Optional.of(ArenaHandler.getArenasWithType(arenaState, type));
      Random random = new Random();
      int x = random.nextInt(t.get().size());
      return Optional.ofNullable(ArenaHandler.getArenasWithType(arenaState, type).get(x));
    } catch (Exception exception) {
      exception.printStackTrace();
      kickMessage(gameOwner, newLines(darkRed(exception.getMessage()), red("!!!PLEASE INFORM THE STAFF ABOUT THIS ERROR IMMEDIATELY!!!")));
      return null;
    }
  }

  /**
   * Gets all Arenas with a specify ArenaState of a list.
   * @param arenaState Requires the state of the arena
   * @param type Requires the mob type
   * @return arenas with the matching requirements
   */
  @NotNull
  public static ArrayList<Arena> getArenasWithType(@NotNull ArenaState arenaState, @NotNull ArenaMobType type) {
    ArrayList<Arena> collectedArenas = new ArrayList<>();
    arenas.values().forEach(arenas1 -> collectedArenas.addAll(arenas1.stream().filter(arena -> arena.getState() == arenaState && arena.getMobType() == type).toList()));
    return collectedArenas;
  }

  /**
   * Initialized the arenas by server start.
   * @param main Requries the plugin instance of the {@link Main}
   */
  public static void init(@NotNull JavaPlugin main) {
    if (!FILE_PATH.exists()) {
      FILE_PATH.mkdir();
    }
    ArenaHandler.generateArenas("arena_1",Bukkit.getWorld("world_bossfight"), ARENAS_Z_DISTANCE, ArenaMobType.ZOMBIE);
    ArenaHandler.generateArenas("arena_2",Bukkit.getWorld("world_bossfight"), ARENAS_Z_DISTANCE, ArenaMobType.CREEPER);
  }

  /**
   * Generate the arenas.
   * @param schem need the schematic for the arenas
   * @param world the world for the arena/schematic
   * @param z the distance to the last arena on the z cordinate
   * @param mobType The mob type for the arena
   */
  private static void generateArenas(String schem, @NotNull World world, int z, ArenaMobType mobType) {
    HashSet<Arena> setArena = new HashSet<>();
    for (int i = 0; i < AREANS_TO_GENERATE; ++i) {
      setArena.add(ArenaHandler.generateArena(schem, world, z, schem + "." + i, mobType));
      Instances.BOSS_FIGHT_CREATOR_LOGGER.logInfo("ARENA ID: " + schem + "." + i);
      ++ARENA_OFFSET;
    }
    arenas.put(schem, setArena);
    ARENA_OFFSET = 0;
  }

  /**
   * Generate a arena
   * @param schem need the schematic for the arenas
   * @param world the world for the arena/schematic
   * @param z the distance to the last arena on the z cordinate
   * @param arenaID A custom id for this arena
   * @param mobType The mob type for the arena
   * @return the loaded arena
   */
  @NotNull
  private static Arena generateArena(@NotNull String schem, @NotNull World world, int z, String arenaID, ArenaMobType mobType) {
    return new Arena(schem, new Location(world, (ARENAS_X_DISTANCE * ARENA_OFFSET), 100.0, z), arenaID, mobType);
  }

}
