package de.elia.bossfightcreator.executer.game.zombie;

import de.elia.api.achievements.Achievements;
import de.elia.api.game.Game;
import de.elia.api.timing.timer.TimerTasks;
import de.elia.api.timing.utils.TimerUtils;
import de.elia.api.timing.utils.TimerUtils.TimeRunnable;

import de.elia.bossfightcreator.BossFightCreatorMain;
import de.elia.bossfightcreator.arena.Arena;
import de.elia.bossfightcreator.arena.ArenaReBuilder;
import de.elia.bossfightcreator.arena.ArenaSpawnLocation;
import de.elia.party.Party;
import de.elia.soulboss.entitys.zombie.ZombieBoss;

import net.minecraft.world.damagesource.DamageSource;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.jetbrains.annotations.NotNull;

import static de.elia.achivementssystem.achievement.Achievement.giveAchievement;
import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.darkRed;
import static de.elia.api.messages.builder.MessageBuilder.gold;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.systemclasses.messages.Message.messageWithPrefix;


//This class is the game for the bosses.
@Deprecated//I WILL UPDATE TO THE SAME SHEMA IN CreeperGame
//TODO: UPDATE THIS CLASS
public class ZombieGame implements Listener, Game {

  private final Arena arena;
  private final Player gameOwner;
  private final JavaPlugin plugin;
  private final String bossName;
  private final Location spawnLocation;
  public final Party party;
  private ZombieBoss boss;
  private boolean isBossDie = false;
  

  //This creates a new game.
  public ZombieGame(@NotNull Arena arena, @NotNull Player gameOwner, JavaPlugin plugin, @NotNull Party party) {
    this.arena = arena;
    this.gameOwner = gameOwner;
    this.plugin = plugin;
    this.spawnLocation = ArenaSpawnLocation.spawnLocation(arena.getArenaID(), gameOwner);
    this.bossName = "boss_" + arena.getArenaID() + "_" + gameOwner.getUniqueId();
    this.party = party;
    Bukkit.getPluginManager().registerEvents(this, plugin);
    ALL_ACTIVE_GAMES.add(this);
    BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Start the Countdown for the start!");
    new GameStartTimer().start(121*20, gameOwner, this.spawnLocation, plugin);
  }

  //This starts the game.
  public void startGame() {
    BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Spawn the Boss...");
    new BukkitRunnable(){
      @Override
      public void run() {
        ZombieGame.this.boss = new ZombieBoss(ZombieGame.this.spawnLocation, ZombieGame.this.bossName){
          @Override
          public void die(@NotNull DamageSource damageSource) {
            Bukkit.getWorld("world_bossfight").strikeLightningEffect(ZombieGame.this.boss.getBukkitEntity().getLocation());//TEST (Info next line)
            /*
            OLD: Bukkit.getWorld("world_bossfight").strikeLightningEffect(this.getBukkitCreature().getLocation());
            NEW: Bukkit.getWorld("world_bossfight").strikeLightningEffect(ZombieGame.this.boss.getBukkitEntity().getLocation());
            Change because in the old call, the method getBukkitCreator no longer exists
             */
            BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("The Boss " + this + this.getName() + "is death!");
            ZombieGame.this.party.members().forEach(player -> giveAchievement(player, Achievements.BOSSFIGHT_ZOMBIE_END));
            super.die(damageSource);
            BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("The Boss is die! End Game...");
            BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Start the GameEndTimer!");
            ZombieGame.this.party.members().forEach(gamePlayer -> new GameEndTimer().start(60*20, gamePlayer, Bukkit.getWorld("world").getSpawnLocation(), ZombieGame.this.plugin));
          }
        };
      }
    }.runTask(this.plugin);
  }

  //This methode ends this game
  public void end(Location location) {
    BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Teleport all game players in the World!");
    this.party.members().forEach(player -> {
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Teleport the game player " + player.getName() + " in the World!");
      player.teleport(Bukkit.getWorld("world").getSpawnLocation());
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Reset the status of the Player!");
      BossFightCreatorMain.playerStatusMap().replace(player, 0);
    });
    BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Remove the party!");
    party.removeParty(gameOwner);
    BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Rebuild the arena " + this.arena.getArenaID());
    ArenaReBuilder.reBuildArena(this.arena);
    ALL_ACTIVE_GAMES.remove(this);
    BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Game successful end!");
  }

  //This methode killed a game with a reason
  @Override
  public void kill(String reason, boolean isRestart){
    this.killGame(reason, isRestart);
  }

  //This methode killed a game with a reason
  public void killGame(String reason, boolean isRestart){
    if (!isRestart) {
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Teleport all game players in the World!");
      this.party.members().forEach(player -> {
        message(player, red("THE GAME ENDED BECAUSE: " + reason + "!"));
        BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Teleport the game player " + player.getName() + " in the World!");
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Reset the status of the Player!");
        BossFightCreatorMain.playerStatusMap().replace(player, 0);
      });
      this.boss.die(this.boss.damageSources().genericKill());
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Remove the party!");
      party.removeParty(gameOwner);
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Rebuild the arena " + this.arena.getArenaID());
      ArenaReBuilder.reBuildArena(this.arena);
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Game successful end!");
      ALL_ACTIVE_GAMES.remove(this);
    }
  }

  //Gets the Arena of this game.
  @NotNull
  public Arena getArena() {
    return this.arena;
  }

  //Gets the ZombieBoss of this game.
  @NotNull
  public ZombieBoss getBoss() {
    return this.boss;
  }

  //This event ends this game if the game owner disconnects.
  @EventHandler
  private void onPlayerQuitServer(@NotNull PlayerQuitEvent event) {
    if (event.getPlayer() == this.gameOwner) {
      party.members().forEach(player -> {
        message(player, darkRed("THE OWNER OF THIS GAME LEFT! THE WORLD WILL BE CLOSED!"));
        this.killGame("GAME OWNER DISCONNECTED", false);
        BossFightCreatorMain.playerStatusMap().replace(player, 0);
        return;
      });
    }else if (party.members().contains(event.getPlayer())) {
      party.members().forEach(player -> {
        message(this.gameOwner, gray("The player ").append(aqua(player.getName())).append(gray(" was kicked on the game because he left the server!")));
        BossFightCreatorMain.playerStatusMap().replace(player, 0);
      });
    }
  }

  //This event kills the game or kicks a player if he dies
  @EventHandler
  private void onPlayerDie(@NotNull PlayerDeathEvent event) {
    if (event.getPlayer() == this.gameOwner) {
      party.members().forEach(player -> {
        party.members().forEach(player1 -> message(player1, red("The game owner died! The game end now!")));this.party.removePlayer(player);
        this.killGame("GAME OWNER DIED", false);
      });
    }else {
      party.members().forEach(player -> {
        message(this.gameOwner, gray("The player ").append(aqua(player.getName())).append(gray(" died so he is eliminated!")));
        message(event.getPlayer(), gold("Du bist gestorben und somit raus!"));
        party.removePlayer(event.getPlayer());
        BossFightCreatorMain.playerStatusMap().replace(event.getPlayer(), 0);
      });
    }
  }

  //This class creates a timer for the start.
  public class GameStartTimer extends TimerTasks {

    private void timerMessage(int seconds) {
      String string = String.valueOf(seconds);
      ZombieGame.this.party.members().forEach(target -> messageWithPrefix(target, gray("his game starts in ").append(aqua(string).append(gray(" seconds!")))));
    }

    @Override
    public void start(int time, final @NotNull Player player, final Location location, final @NotNull Plugin plugin) {
      new TimerUtils().countdownAndRun(time, new Runnable(){
        @Override
        public void run() {
          messageWithPrefix(player, gray("Your Bossfight starts!"));
        }
      }, plugin);
      new TimerUtils().countdownInterval(time, new TimeRunnable(){
        @Override
        public void run(int ticks) {
          if (ticks % 20 == 0) {
            int seconds = ticks / 20;
            if (seconds == 100) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 80) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 60) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 40) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 30) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 20) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 15) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 10) {
              GameStartTimer.this.timerMessage(seconds);
              new BukkitRunnable(){
                @Override
                public void run() {
                  BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Teleporting all Players in the Arena.");
                  messageWithPrefix(player,gray("You and your team will be teleported in the arena!"));
                  ZombieGame.this.party.members().forEach(gamePlayer -> gamePlayer.teleport(location));
                }
              }.runTask(plugin);
            }
            if (seconds == 5) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 4) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 3) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 2) {
              GameStartTimer.this.timerMessage(seconds);
            }
            if (seconds == 1) {
              GameStartTimer.this.timerMessage(seconds);
            }
          }
        }
      }, new Runnable(){
        @Override
        public void run() {
          BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("GameStartTimer was end!");
          BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Game started!");
          ZombieGame.this.startGame();
        }
      }, plugin);
    }
  }

  //This class creates a timer for the end of the game.
  public class GameEndTimer extends TimerTasks {

    private void timerMessage(int seconds, Player target) {
      String string = String.valueOf(seconds);
      messageWithPrefix(target, gray("The game ends in ").append(aqua(string).append(gray(" seconds!"))));
    }
    @Override
    public void start(int time, @NotNull Player player, final Location location, final @NotNull Plugin plugin) {
      new TimerUtils().countdownAndRun(time, new Runnable(){
        @Override
        public void run() {
          if (!ZombieGame.this.isBossDie) {
            ZombieGame.this.boss.kill();
            ZombieGame.this.isBossDie = true;
          }
        }
      }, plugin);
      new TimerUtils().countdownInterval(time, new TimeRunnable(){
        public void run(int ticks) {
          if (ticks % 20 == 0) {
            int seconds = ticks / 20;
            if (seconds == 60) {
              GameEndTimer.this.timerMessage(seconds, player);
            }
            if (seconds == 30) {
              GameEndTimer.this.timerMessage(seconds, player);
            }
            if (seconds == 20) {
              GameEndTimer.this.timerMessage(seconds, player);
            }
            if (seconds == 10) {
              GameEndTimer.this.timerMessage(seconds, player);
            }
            if (seconds == 5) {
              GameEndTimer.this.timerMessage(seconds, player);
            }
            if (seconds == 3) {
              GameEndTimer.this.timerMessage(seconds, player);
            }
            if (seconds == 2) {
              GameEndTimer.this.timerMessage(seconds, player);
            }
            if (seconds == 1) {
              GameEndTimer.this.timerMessage(seconds, player);
            }
          }
        }
      }, new Runnable(){
        @Override
        public void run() {
          new BukkitRunnable(){
            @Override
            public void run() {
              ZombieGame.this.end(location);
            }
          }.runTask(plugin);
        }
      }, plugin);
    }
  }
}
