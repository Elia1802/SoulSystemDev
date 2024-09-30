package de.elia.bossfightcreator.executer;

import de.elia.api.game.Game;
import de.elia.api.logging.PluginLogger;
import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.TheZepserAPI;

import de.elia.bossfightcreator.BossFightCreatorMain;
import de.elia.bossfightcreator.executer.game.creeper.CreeperGame;
import de.elia.bossfightcreator.executer.game.zombie.ZombieGame;
import de.elia.bossfightcreator.arena.Arena;
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

import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

import static de.elia.achivementssystem.achievement.Achievement.giveAchievement;
import static de.elia.api.achievements.Achievements.BOSSFIGHT_CREEPER;
import static de.elia.api.achievements.Achievements.BOSSFIGHT_ZOMBIE;
import static de.elia.api.logging.error.SaveError.saveError;
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

//This class executes a game if the player clicked a spawn egg
public class GameExecuter implements Listener {

  private final PluginLogger logger = bossFightCreator().bossFightCreatorLogger();
  private final Random randomPartyID = new Random();
  private final JavaPlugin plugin;
  private Game game;

  public GameExecuter(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  //The event checks if a spawnegg clicked
  @EventHandler(priority = EventPriority.HIGHEST)
  private void newGameExecuter(@NotNull PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (event.getItem() == null)return;
    try {
      if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && TheZepserAPI.Item.hasKey(event.getItem(), TheZepserAPI.Item.createKey(Complex.ZOMBIE_SPAWN_EGG), bossFightCreator().main())) {
        event.setCancelled(true);
        createZombieGame(player, event.getItem());
      }else if ((event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && TheZepserAPI.Item.hasKey(event.getItem(), TheZepserAPI.Item.createKey(Complex.CREEPER_SPAWN_EGG), bossFightCreator().main())) {
        event.setCancelled(true);
        createCreeperGame(player, event.getItem());
      }
    }catch (SQLException exception) {
      exception.printStackTrace();
      saveError(BossFightCreatorMain.bossFightCreator().main(), exception, "GameExecuter-newGameExecuter-An_error_occurred_by_create_a_game=SQLException");
    }
  }

  //This methode execute a zombie game
  protected void createZombieGame(@NotNull Player player, ItemStack item) throws SQLException {
    if (item == null)return;
    if (playerStatusMap().get(player) == 0) {
      playerStatusMap().replace(player, 1);
      Inventory inventory = player.getInventory();
      inventory.removeItem(item);
      logger.logInfo("Create a new Game for " + player.getName() + " with the ZombieBoss...");
      logger.logInfo("Create a new Party...");
      int partyId = randomPartyID.nextInt(100000, 999999);
      Party party = new Party(player, partyId);
      PARTYS.put(player, party);
      logger.logInfo("Party created!");
      giveAchievement(player, BOSSFIGHT_ZOMBIE);
      logger.logInfo("Load a Arena for the Game...");
      Optional<Arena> optionalArena = getFreeArena(ZOMBIE, player);
      if (optionalArena.isPresent()) {
        Arena arena = optionalArena.get();
        logger.logInfo("Arena Information:");
        logger.logInfo("  -> Arena: " + arena);
        logger.logInfo("  -> Arena schematic: " + arena.getName());
        logger.logInfo("  -> Arena ID: " + arena.getArenaID());
        logger.logInfo("  -> Arena Mob Type: " + arena.getMobType());
        logger.logInfo("  -> Arena spawn location: " + spawnLocation(arena.getArenaID(), player));
        reBuildArena(arena);
        arena.setState(USED);
        logger.logInfo("Arena loaded!");
        logger.logInfo("Arena " + arena.getArenaID() + " is " + arena.getState() + "!");
        logger.logInfo("Start the new Game of " + player.getName());
        this.game = new ZombieGame(arena, player, plugin, party);
        arena.setGame(this.game);
        logger.logInfo("The Game of " + arena.getArenaID() + " is " + this.game);
        logger.logInfo("A new Game was created for " + player.getName());
        return;
      }
      messageWithPrefix(player, red("There is no arena aviable right now!"));
      logger.logWarning("There was a problem by creating a new Game");
      logger.logError("Problem: NO ARENAS AVIABLE");
    }else {
      message(player, red("You are currently in a game!"));
    }
  }

  //This methode executes a creeper game
  protected void createCreeperGame(@NotNull Player player, ItemStack item) throws SQLException {
    if (item == null)return;
    if (playerStatusMap().get(player) == 0) {
      playerStatusMap().replace(player, 1);
      Inventory inventory = player.getInventory();
      inventory.removeItem(item);
      logger.logInfo("Create a new Game for " + player.getName() + " with the CreeperBoss...");
      logger.logInfo("Create a new Party...");
      int partyID = randomPartyID.nextInt(100000, 999999);
      Party party = new Party(player, partyID);
      PARTYS.put(player, party);
      logger.logInfo("Party crated!");
      giveAchievement(player, BOSSFIGHT_CREEPER);
      logger.logInfo("Load a Arena for the Game");
      Optional<Arena> optionalArena = getFreeArena(CREEPER, player);
      if (optionalArena.isPresent()) {
        Arena arena = optionalArena.get();
        logger.logInfo("Arena Information:");
        logger.logInfo("  -> Arena: " + arena);
        logger.logInfo("  -> Arena schematic: " + arena.getName());
        logger.logInfo("  -> Arena ID: " + arena.getArenaID());
        logger.logInfo("  -> Arena Mob Type: " + arena.getMobType());
        logger.logInfo("  -> Arena spawn location: " + spawnLocation(arena.getArenaID(), player));
        reBuildArena(arena);
        arena.setState(USED);
        logger.logInfo("Arena loaded!");
        logger.logInfo("Arena " + arena.getArenaID() + " is " + arena.getState() + "!");
        logger.logInfo("Start the new Game of " + player.getName());
        this.game = new CreeperGame(arena, player, plugin, spawnLocation(arena.getArenaID(), player), party);
        arena.setGame(game);
        logger.logInfo("The Game of " + arena.getArenaID() + " is " + this.game);
        logger.logInfo("A new Game was created for " + player.getName());
        return;
      }
    }else {
      message(player, red("You are currently in a game!"));
    }
  }

}
