package de.elia.bossfightcreator.arena;

import de.elia.api.logging.error.SaveError;

import de.elia.bossfightcreator.BossFightCreatorMain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.String;
import java.util.ArrayList;

import static de.elia.api.messages.builder.MessageBuilder.gold;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.api.messages.builder.MessageBuilder.yellow;

/**
 * This class gets the spawn location for the arenas
 * @author Elia
 * @since 1.0.0
 */
public class ArenaSpawnLocation {

  private static final ArrayList<String> ARENA_IDs = new ArrayList<>();

  static {
    ARENA_IDs.add("arena_1.0");
    ARENA_IDs.add("arena_1.1");
    ARENA_IDs.add("arena_1.2");
    ARENA_IDs.add("arena_1.3");
    ARENA_IDs.add("arena_1.4");
    ARENA_IDs.add("arena_1.5");
    ARENA_IDs.add("arena_1.6");
    ARENA_IDs.add("arena_1.7");
    ARENA_IDs.add("arena_1.8");
    ARENA_IDs.add("arena_1.9");
    ARENA_IDs.add("arena_2.0");
    ARENA_IDs.add("arena_2.1");
    ARENA_IDs.add("arena_2.2");
    ARENA_IDs.add("arena_2.3");
    ARENA_IDs.add("arena_2.4");
    ARENA_IDs.add("arena_2.5");
    ARENA_IDs.add("arena_2.6");
    ARENA_IDs.add("arena_2.7");
    ARENA_IDs.add("arena_2.8");
    ARENA_IDs.add("arena_2.9");
  }

  /**
   * Set a spawn location for the arena
   * @param arenaID Requires the id of the arena
   * @param player A game owner for error messages
   * @return the location for the arena
   */
  @Nullable
  public static Location spawnLocation(@NotNull String arenaID, Player player){
    if (ARENA_IDs.contains(arenaID)) {
      switch (arenaID) {
        case "arena_1.0" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), -4.0, 101.0, 213.0);
        }
        case "arena_1.1" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 196.0, 101.0, 213.0);
        }
        case "arena_1.2" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 396.0, 101.0, 213.0);
        }
        case "arena_1.3" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 596.0, 101.0, 213.0);
        }
        case "arena_1.4" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 796.0, 101.0, 213.0);
        }
        case "arena_1.5" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 996.0, 101.0, 213.0);
        }
        case "arena_1.6" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1196.0, 101.0, 213.0);
        }
        case "arena_1.7" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1396.0, 101.0, 213.0);
        }
        case "arena_1.8" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1596.0, 101.0, 213.0);
        }
        case "arena_1.9" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1796.0, 101.0, 213.0);
        }
        case "arena_2.0" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 3, 104, 174);
        }
        case "arena_2.1" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 203, 104, 174);
        }
        case "arena_2.2" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 403, 104, 174);
        }
        case "arena_2.3" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 603, 104, 174);
        }
        case "arena_2.4" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 803, 104, 174);
        }
        case "arena_2.5" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1003, 104, 174);
        }
        case "arena_2.6" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1203, 104, 174);
        }
        case "arena_2.7" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1403, 104, 174);
        }
        case "arena_2.8" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1603, 104, 174);
        }
        case "arena_2.9" -> {
          return new Location(Bukkit.getWorld("world_bossfight"), 1803, 104, 174);
        }
      }
      message(player, red("ERROR: AT THIS THERE ARE NO SPAWN LOCATION FOR YOUR ARENA!"));
      message(player, red("This can depend on:"));
      message(player, yellow("  - No arenas aviable at this time"));
      message(player, yellow("  - this arena id does not exist"));
      message(player, yellow("  - a error while creating the game"));
      message(player, yellow("  - the world you're trying to join does not exist"));
      message(player, gold("Inform the Staff about this error as a bug report!"));
      message(player, gray("ArenaID: " + arenaID + " has no spawn location -> location = null..."));
      try {
        throw new NullPointerException("ArenaID: " + arenaID + " has no spawn location -> location = null");
      }catch (NullPointerException exception) {
        exception.printStackTrace();
        SaveError.saveError(BossFightCreatorMain.bossFightCreator().main(), exception, "ArenaSpawnLocation-spawnLocation-The_location_of_the_arena_" + arenaID + "_is_null=NullPointerException");
      }
      return null;
    }else {
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logError("The arena id " + arenaID + " is null!");
      return null;
    }
  }
}
