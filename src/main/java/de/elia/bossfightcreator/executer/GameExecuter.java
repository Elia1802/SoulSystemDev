package de.elia.bossfightcreator.executer;

import de.elia.api.game.Game;
import de.elia.api.logging.PluginLogger;
import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.TheZepserAPI;

import de.elia.bossfightcreator.BossFightCreatorMain;
import de.elia.bossfightcreator.executer.game.creeper.CreeperGame;
import de.elia.bossfightcreator.arena.Arena;
import de.elia.bossfightcreator.executer.game.zombie.ZombieGame;
import de.elia.party.Party;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

import static de.elia.achivementssystem.achievement.Achievement.giveAchievement;
import static de.elia.api.achievements.Achievements.BOSSFIGHT_CREEPER;
import static de.elia.api.achievements.Achievements.BOSSFIGHT_ZOMBIE;
import static de.elia.bossfightcreator.arena.ArenaMobType.CREEPER;
import static de.elia.bossfightcreator.arena.ArenaMobType.ZOMBIE;
import static de.elia.bossfightcreator.arena.ArenaState.USED;
import static de.elia.party.utils.PartyUtils.PARTYS;
import static de.elia.bossfightcreator.arena.ArenaReBuilder.reBuildArena;
import static de.elia.bossfightcreator.arena.ArenaSpawnLocation.spawnLocation;
import static de.elia.bossfightcreator.BossFightCreatorMain.bossFightCreator;
import static de.elia.bossfightcreator.BossFightCreatorMain.playerStatusMap;
import static de.elia.bossfightcreator.arena.ArenaHandler.getFreeArena;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.systemclasses.messages.Message.messageWithPrefix;

/**
 * This class executes a game if the player clicked a spawn egg
 * @author Elia
 * @since 2.0.0
 */
public class GameExecuter implements Listener {

  private final PluginLogger logger = bossFightCreator().bossFightCreatorLogger();
  private final Random randomPartyID = new Random();
  private final JavaPlugin plugin;
  private Game game;

  public GameExecuter(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * The event checks if a spawnegg clicked
   * @param event This event is called if the player interacts with an item.
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  private void newGameExecuter(@NotNull PlayerInteractEvent event) {
    //Get the player of the event
    Player player = event.getPlayer();
    //Checks if the item is null
    if (event.getItem() == null)return;
    //Check if the conditions for creeper game are correct
    if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && TheZepserAPI.Item.hasKey(event.getItem(), TheZepserAPI.Item.createKey(Complex.ZOMBIE_SPAWN_EGG), bossFightCreator().main())) {
      //Cancel this event
      event.setCancelled(true);
      //Create a new zombie game
      createZombieGame(player, event.getItem());
    //Check if the conditions for the zombie game are correct
    }else if ((event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && TheZepserAPI.Item.hasKey(event.getItem(), TheZepserAPI.Item.createKey(Complex.CREEPER_SPAWN_EGG), bossFightCreator().main())) {
      //Cancel this event
      event.setCancelled(true);
      //Create a new creeper game
      createCreeperGame(player, event.getItem());
    }
  }

  /**
   * This methode executes a zombie game
   * @param player The game owner
   * @param item The spawn egg of the zombie boss
   */
  protected void createZombieGame(@NotNull Player player, ItemStack item) {
    //Checks if the spawn egg is null
    if (item == null)return;
    //Checks if the player is not in a game
    if (playerStatusMap().get(player) == 0) {
      //Set that the player is in a game
      playerStatusMap().replace(player, 1);
      //Get the inventory of the player
      Inventory inventory = player.getInventory();
      //Remove the spawn egg
      inventory.removeItem(item);
      //log in the console
      logger.logInfo("Create a new Game for " + player.getName() + " with the ZombieBoss...");
      logger.logInfo("Create a new Party...");
      //Create a party id
      int partyId = randomPartyID.nextInt(100000, 999999);
      //Create a new party
      Party party = new Party(player, partyId);
      PARTYS.put(player, party);
      //log in the console
      logger.logInfo("Party created!");
      //Give the player an achievement for the first bossfight
      giveAchievement(player, BOSSFIGHT_ZOMBIE);
      //log in the console
      logger.logInfo("Load a Arena for the Game...");
      //Get a free arena
      Optional<Arena> optionalArena = getFreeArena(ZOMBIE, player);
      if (optionalArena.isPresent()) {
        Arena arena = optionalArena.get();
        //log in the console
        logger.logInfo("Arena Information:");
        logger.logInfo("  -> Arena: " + arena);
        logger.logInfo("  -> Arena schematic: " + arena.getName());
        logger.logInfo("  -> Arena ID: " + arena.getArenaID());
        logger.logInfo("  -> Arena Mob Type: " + arena.getMobType());
        logger.logInfo("  -> Arena spawn location: " + spawnLocation(arena.getArenaID(), player));
        //Rebuild the arena
        reBuildArena(arena);
        //Set the state of the arena to use
        arena.setState(USED);
        //log in the console
        logger.logInfo("Arena loaded!");
        logger.logInfo("Arena " + arena.getArenaID() + " is " + arena.getState() + "!");
        logger.logInfo("Start the new Game of " + player.getName());
        //Load the zombie game
        /* TODO: DEPRECATED
        this.game = new OldZombieGame(arena, player, plugin, party);
         */
        this.game = new ZombieGame(BossFightCreatorMain.bossFightCreator().main(), arena, player, spawnLocation(arena.getArenaID(), player), party, "TEST-V.2.0-ZOMBIE_BOSS-dev");
        //Set to the arena a game
        arena.setGame(this.game);
        //log in the console
        logger.logInfo("The Game of " + arena.getArenaID() + " is " + this.game);
        logger.logInfo("A new Game was created for " + player.getName());
        return;
      }
      messageWithPrefix(player, red("There is no arena aviable right now!"));
      //log in the console
      logger.logWarning("There was a problem by creating a new Game");
      logger.logError("Problem: NO ARENAS AVIABLE");
    }else {
      //Send an error message
      message(player, red("You are currently in a game!"));
    }
  }

  /**
   * This methode executes a creeper game
   * @param player The game owner
   * @param item The spawn egg of the creeper boss
   */
  protected void createCreeperGame(@NotNull Player player, ItemStack item) {
    //Checks if the spawn egg is null
    if (item == null)return;
    //Checks if the player is not in a game
    if (playerStatusMap().get(player) == 0) {
      //Set that the player is in a game
      playerStatusMap().replace(player, 1);
      //Get the inventory of the player
      Inventory inventory = player.getInventory();
      //Remove the spawn egg
      inventory.removeItem(item);
      //log in the console
      logger.logInfo("Create a new Game for " + player.getName() + " with the CreeperBoss...");
      logger.logInfo("Create a new Party...");
      //Create a party id
      int partyID = randomPartyID.nextInt(100000, 999999);
      //Create a new party
      Party party = new Party(player, partyID);
      PARTYS.put(player, party);
      //log in the console
      logger.logInfo("Party crated!");
      //Give the player an achievement for the first bossfight
      giveAchievement(player, BOSSFIGHT_CREEPER);
      //log in the console
      logger.logInfo("Load a Arena for the Game");
      //Get a free arena
      Optional<Arena> optionalArena = getFreeArena(CREEPER, player);
      if (optionalArena.isPresent()) {
        Arena arena = optionalArena.get();
        //log in the console
        logger.logInfo("Arena Information:");
        logger.logInfo("  -> Arena: " + arena);
        logger.logInfo("  -> Arena schematic: " + arena.getName());
        logger.logInfo("  -> Arena ID: " + arena.getArenaID());
        logger.logInfo("  -> Arena Mob Type: " + arena.getMobType());
        logger.logInfo("  -> Arena spawn location: " + spawnLocation(arena.getArenaID(), player));
        //Rebuild the arena
        reBuildArena(arena);
        //Set the state of the arena to use
        arena.setState(USED);
        //log in the console
        logger.logInfo("Arena loaded!");
        logger.logInfo("Arena " + arena.getArenaID() + " is " + arena.getState() + "!");
        logger.logInfo("Start the new Game of " + player.getName());
        //Load the zombie game
        /*
        this.game = new CreeperGameOld(arena, player, plugin, spawnLocation(arena.getArenaID(), player), party);
         */
        this.game = new CreeperGame(arena, player, spawnLocation(arena.getArenaID(), player), party, "TEST-V.2.0-CREEPER_BOSS-dev");
        //Set to the arena a game
        arena.setGame(game);
        //log in the console
        logger.logInfo("The Game of " + arena.getArenaID() + " is " + this.game);
        logger.logInfo("A new Game was created for " + player.getName());
        return;
      }
    }else {
      //Send an error message
      message(player, red("You are currently in a game!"));
    }
  }

}
