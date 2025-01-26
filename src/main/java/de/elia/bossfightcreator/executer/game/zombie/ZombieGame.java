package de.elia.bossfightcreator.executer.game.zombie;

import de.elia.api.achievements.Achievements;
import de.elia.api.entities.BossEntity;
import de.elia.api.game.Game;
import de.elia.api.logging.PluginLogger;
import de.elia.api.timing.timer.TimerTasks;
import de.elia.api.timing.utils.TimerUtils;
import de.elia.bossfightcreator.BossFightCreatorMain;
import de.elia.bossfightcreator.arena.Arena;
import de.elia.bossfightcreator.arena.ArenaReBuilder;
import de.elia.bossfightcreator.executer.game.creeper.CreeperGame;
import de.elia.party.Party;
import de.elia.soulboss.entitys.zombie.ZombieBoss;
import de.elia.systemclasses.keys.NameSpacedKeys;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static de.elia.achivementssystem.achievement.Achievement.giveAchievement;
import static de.elia.api.entityRegion.EntityRegionBuilder.containsEntity;
import static de.elia.api.entityRegion.EntityRegionBuilder.createEntityRegionBorder;
import static de.elia.api.messages.builder.MessageBuilder.darkPurple;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.bossfightcreator.BossFightCreatorMain.playerStatusMap;

public class ZombieGame implements Game, Listener {

  //Instances:
  //An instance of the plugin logger
  private final PluginLogger logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
  //An instance of the spawn location of the world "world"
  Location worldSpawnLocation = Bukkit.getWorld("world").getSpawnLocation();
  //An instance of the main class
  private final JavaPlugin plugin;

  //Variables
  //The arena of this game
  private final Arena arena;
  //The game owner of this game
  private final Player gameOwner;
  //The spawn location of the arena
  private final Location spawnLocation;
  //The party of the game
  private final Party gameParty;
  //The bossbar of the boss in this game
  private final GameBossBar bossBar;
  //The name of the boss
  private final String bossName;
  //The boss
  private ZombieBoss boss;
  //The uuid of the boss
  private UUID bossUUID;
  //a boolean to check if the boss is dead
  private boolean bossDeath = false;

  public ZombieGame(@NotNull JavaPlugin plugin, @NotNull Arena arena, @NotNull Player gameOwner, @NotNull Location spawnLocation, @NotNull Party gameParty, @NotNull String bossName) {
    //Set the instances
    this.plugin = plugin;
    this.arena = arena;
    this.gameOwner = gameOwner;
    this.spawnLocation = spawnLocation;
    this.gameParty = gameParty;
    this.bossName = bossName;
    this.bossBar = new GameBossBar();
    //Register this class as event
    ALL_ACTIVE_GAMES.add(this);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    //Log the start information
    this.logger.logInfo("Start the Zombie-Game Start-Countdown!");
    //Start the start timer
    new GameStartTimer().start(121*20, gameOwner, spawnLocation, plugin);
  }

  /**
   * Start the game
   */
  public void startGame(){
    this.logger.logInfo("Spawn the boss...");
    new BukkitRunnable() {
      @Override
      public void run() {
        //Spawn the boss and override the die methode
        ZombieGame.this.boss = new ZombieBoss(ZombieGame.this.spawnLocation, ZombieGame.this.bossName) {
          @Override
          public void die(@NotNull DamageSource damageSource){
            //Create a strike lightning effect on the death position of the boss
            Bukkit.getWorld("world_bossfight").strikeLightningEffect(ZombieGame.this.boss.getBukkitEntity().getLocation());
            //Log the boss is dead
            ZombieGame.this.logger.logInfo("The Boss " + this + this.getName() + " is dead!");
            super.die(damageSource);
            //Send logs
            ZombieGame.this.logger.logInfo("The Boss is dead!, End game...");
            ZombieGame.this.logger.logInfo("Start end timer...");
            //Give every game player the achievement
            ZombieGame.this.gameParty.members().forEach(partyPlayer -> {
              giveAchievement(partyPlayer, Achievements.BOSSFIGHT_ZOMBIE_END);
            });
            //Start end timer
            new ZombieGame.GameEndTimer().start(60*20, null, Bukkit.getWorld("world").getSpawnLocation(), plugin);
          }
        };
        ZombieGame.this.bossUUID = ZombieGame.this.boss.getUUID();
      }
    }.runTask(this.plugin);
    this.logger.logInfo("Boss spawned!");
    this.logger.logInfo("Game started!");
  }

  public void endGame(@NotNull Location spawnLocation){
    this.logger.logInfo("Teleport all Players to the normal world (Location: " + spawnLocation + ") and reset the game status of the players!");
    this.gameParty.members().forEach(partyPlayer -> {
      partyPlayer.teleport(spawnLocation);
      message(partyPlayer, darkPurple("Thank you ").append(gray("for playing this ")).append(darkPurple("boss fight")).append(gray("! We hope you ")).append(darkPurple("enjoyed ")).append(gray("it!")));
      playerStatusMap().replace(partyPlayer, 0);
    });
    this.logger.logInfo("Remove the party of this game!");
    this.gameParty.removeParty(this.gameOwner);
    this.logger.logInfo("Rebuild the arena " + this.arena.getName() + this.arena.getArenaID());
    ArenaReBuilder.reBuildArena(this.arena);
    this.logger.logInfo("Remove this game!");
    ALL_ACTIVE_GAMES.remove(this);
    this.logger.logInfo("Game successful end!");
  }

  /**
   * This methode kills this game
   * @param reason Required the reason
   * @param isRestart If not a restart, unload the game "normal"
   */
  @Override
  public void kill(String reason, boolean isRestart) {
    if (!isRestart) {
      this.gameParty.members().forEach(partyPlayer -> message(partyPlayer, red("THE GAME ENDED BECAUSE: " + reason + "!")));
      endGame(this.spawnLocation);
    }
  }

  @EventHandler
  private void onPlayerDisconnectServerListener(@NotNull PlayerQuitEvent event) {
    var player = event.getPlayer();
    if (this.gameParty.members().contains(player)) {
      if (player.getPersistentDataContainer().has(new NamespacedKey(this.plugin, Integer.toString(this.gameParty.id())))) {
        this.kill("Party Owner disconnected!", false);
      }else {
        this.gameParty.members().remove(player);
        playerStatusMap().replace(player, 0);
      }
    }
  }

  @EventHandler
  private void onPlayerDiedListener(@NotNull PlayerDeathEvent event) {
    var player = event.getPlayer();
    if (this.gameParty.members().contains(player)) {
      if (player.getPersistentDataContainer().has(new NamespacedKey(this.plugin, Integer.toString(this.gameParty.id())))) {
        new GameEndTimer().start(61, null, this.spawnLocation, this.plugin);
      }else {
        this.gameParty.members().remove(player);
        playerStatusMap().replace(player, 0);
      }
    }
  }

  @EventHandler
  private void onPotion(@NotNull EntitySpawnEvent event){
    var entity = event.getEntity();
    if (entity.getType() == EntityType.ZOMBIE) {
      if (entity.getPersistentDataContainer().has(NameSpacedKeys.ZOMBIE_KEY.key())) {
        LivingEntity boss = (LivingEntity) entity;
        Collection<PotionEffect> effects = new ArrayList<>();
        PotionEffect speed  = new PotionEffect(PotionEffectType.SPEED, 999999999, 2, false, false, false);
        effects.add(speed);
        PotionEffect fireResistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999999, 255, false, false, false);
        effects.add(fireResistance);
        PotionEffect harm  = new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 999999999, 255, false, false, false);
        effects.add(harm);
        boss.addPotionEffects(effects);
      }
    }
  }

  @EventHandler
  private void onUpdateBossBar(EntityDamageEvent event) {
    //Get the entity of the event
    var entity = event.getEntity();
    //Checks if the entity type a zombie
    if (entity.getType() == EntityType.ZOMBIE) {
      //Checks if the boss is null, if it true ends this code
      if (this.boss == null)return;
      //Checks if the boss uuid the same of the saved uuid of this game
      if (entity.getUniqueId() == this.boss.getUUID()) {
        this.bossBar.update(this.boss);
      }
    }
  }

  private class GameBossBar {
    /**
     * This methode creates the Bossbar
     * @param boss Requires the boss of the game
     */
    protected void create(@NotNull BossEntity boss){
      //Create a new boss bar
      BossBar bossbar = Bukkit.createBossBar("Test_BossBar_Zombie", BarColor.PINK, BarStyle.SEGMENTED_10);
      //Add every player of the game party to the boss bar
      ZombieGame.this.gameParty.members().forEach(bossbar::addPlayer);
      //Divides the progress from the max value and the current value of life
      double progress = boss.health() / boss.maxHealth();
      //Set the progress
      bossbar.setProgress(progress);
      //Shows the boss bar
      bossbar.setVisible(true);
      //saves the bossbar in a map
      ZombieGame.bossBossBar.put(boss, bossbar);
    }

    /**
     * This methode updates the Bossbar
     * @param boss Requires the boss of the game
     */
    protected void update(BossEntity boss) {
      //Checks if the boss registerd in the map
      if (ZombieGame.bossBossBar.containsKey(boss)) {
        //Gets the boss bar of the map
        BossBar bossBar = ZombieGame.bossBossBar.get(boss);
        //Divides the progress from the max value and the current value of life
        double progress = boss.health() / boss.maxHealth();
        //Set the progress
        bossBar.setProgress(progress);
      }
    }

    /**
     * This methode removes the Bossbar
     * @param boss Requires the boss of the game
     */
    protected void remove(BossEntity boss) {
      //Checks if the boss registerd in the map
      if (ZombieGame.bossBossBar.containsKey(boss)) {
        //Gets the boss bar of the map
        BossBar bossBar = ZombieGame.bossBossBar.get(boss);
        //Remove all players of the bossbar
        bossBar.removeAll();
        //Delete the bossbar of the map
        ZombieGame.bossBossBar.remove(boss);
      }
    }
  }

  private class GameStartTimer extends TimerTasks {

    /**
     * Send a message to all players in the party
     * @param seconds requires the time in seconds
     */
    private void timeMessage(int seconds) {
      ZombieGame.this.gameParty.members().forEach(player -> message(player, gray("The game starts in ").append(darkPurple(String.valueOf(seconds))).append(gray("!"))));
    }

    @Override
    public void start(int time, Player player, Location location, @NotNull Plugin plugin) {
      //Create a new Countdown
      (new TimerUtils()).countdownAndRun(time, new Runnable() {
        public void run() {
          //Send the information that's the countdown starts
          ZombieGame.this.logger.logInfo("Start-Countdown started!");
          ZombieGame.this.gameParty.members().forEach(partyPlayer -> message(partyPlayer, gray("The bossfight ").append(darkPurple("starts")).append(gray("!"))));
        }
      }, plugin);
      (new TimerUtils()).countdownInterval(time, new TimerUtils.TimeRunnable() {
        public void run(int ticks) {
          if (ticks % 20 == 0) {
            int seconds = ticks / 20;
            //Send a message if the time 100 secounds
            if (seconds == 100)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 80 secounds
            if (seconds == 80)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 60 secounds
            if (seconds == 60)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 40 secounds
            if (seconds == 40)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 30 secounds
            if (seconds == 30)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 20 secounds
            if (seconds == 20)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 15 secounds
            if (seconds == 15)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 10 secounds
            if (seconds == 10){
              ZombieGame.GameStartTimer.this.timeMessage(seconds);
              new BukkitRunnable() {
                @Override
                public void run() {
                  //Send the teleport log
                  ZombieGame.this.logger.logInfo("Teleport all players to Zombie-Game!");
                  ZombieGame.this.gameParty.members().forEach(partyPlayer -> {
                    //Send the teleport message and teleport the players to  the game
                    message(partyPlayer, gray("You will be teleported to the arena"));
                    partyPlayer.teleport(location);
                  });

                }
              }.runTask(plugin);
            }
            //Send a message if the time 5 secounds
            if (seconds == 5)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 4 secounds
            if (seconds == 4)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 3 secounds
            if (seconds == 3)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 2 secounds
            if (seconds == 2)ZombieGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 1 secounds
            if (seconds == 1)ZombieGame.GameStartTimer.this.timeMessage(seconds);
          }

        }
      }, new Runnable() {
        public void run() {
          ZombieGame.this.logger.logInfo("Start-Countdown ended!");
          ZombieGame.this.logger.logInfo("Start Fight...");
          //Start the game
          ZombieGame.this.startGame();
        }
      }, plugin);
    }

  }

  private class GameEndTimer extends TimerTasks {

    /**
     * Send a message to all players in the party
     * @param seconds requires the time in seconds
     */
    private void timeMessage(int seconds) {
      ZombieGame.this.gameParty.members().forEach(player -> message(player, gray("The game ends in ").append(darkPurple(String.valueOf(seconds))).append(gray("!"))));
    }

    @Override
    public void start(int time, Player player, Location location, @NotNull Plugin plugin) {
      new TimerUtils().countdownAndRun(time, new Runnable() {
        public void run() {
          if (!ZombieGame.this.bossDeath) {
            ZombieGame.this.boss.remove(Entity.RemovalReason.KILLED);
            ZombieGame.this.bossDeath = true;
          }
        }
      }, plugin);
      (new TimerUtils()).countdownInterval(time, new TimerUtils.TimeRunnable() {
        public void run(int ticks) {
          if (ticks % 20 == 0) {
            int seconds = ticks / 20;
            if (seconds == 60)GameEndTimer.this.timeMessage(seconds);
            if (seconds == 30)GameEndTimer.this.timeMessage(seconds);
            if (seconds == 20)GameEndTimer.this.timeMessage(seconds);
            if (seconds == 10)GameEndTimer.this.timeMessage(seconds);
            if (seconds == 5)GameEndTimer.this.timeMessage(seconds);
            if (seconds == 3)GameEndTimer.this.timeMessage(seconds);
            if (seconds == 2)GameEndTimer.this.timeMessage(seconds);
            if (seconds == 1)GameEndTimer.this.timeMessage(seconds);
          }
        }
      }, new Runnable() {
        public void run() {
          new BukkitRunnable() {
            @Override
            public void run() {
              ZombieGame.this.endGame(location);
            }
          }.runTask(plugin);
        }
      }, plugin);
    }
  }


}
