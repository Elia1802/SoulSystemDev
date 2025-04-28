package de.elia.bossfightcreator.executer.game.creeper;

import de.elia.api.achievements.Achievements;
import de.elia.api.entities.BossEntity;
import de.elia.api.entityRegion.EntityRegion;
import de.elia.api.game.Game;
import de.elia.api.logging.PluginLogger;
import de.elia.api.timing.timer.TimerTasks;
import de.elia.api.timing.utils.TimerUtils;

import de.elia.bossfightcreator.BossFightCreatorMain;
import de.elia.bossfightcreator.arena.Arena;
import de.elia.bossfightcreator.arena.ArenaReBuilder;
import de.elia.party.Party;
import de.elia.soulboss.entitys.creeper.CreeperBoss;
import de.elia.soulboss.entitys.creeper.drop.Drops;
import de.elia.soulboss.entitys.creeper.minis.MiniCreepers;
import de.elia.systemclasses.keys.NameSpacedKeys;

import io.papermc.paper.event.entity.EntityMoveEvent;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import org.jetbrains.annotations.NotNull;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.Random;
import java.util.UUID;

import static de.elia.achivementssystem.achievement.Achievement.giveAchievement;
import static de.elia.api.entityRegion.EntityRegionBuilder.containsEntity;
import static de.elia.api.entityRegion.EntityRegionBuilder.createEntityRegionBorder;
import static de.elia.api.messages.builder.MessageBuilder.darkPurple;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.bossfightcreator.BossFightCreatorMain.playerStatusMap;

public class CreeperGame implements Game, Listener {

  //Instances:
  //An instance from the logger
  private final PluginLogger logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
  //An instance from the world spawn point
  private final Location worldSpawnLocation = Bukkit.getWorld("world").getSpawnLocation();
  //An instance to the main class
  private final JavaPlugin main = BossFightCreatorMain.bossFightCreator().main();
  //Creata a random instance
  private final Random random = new Random();
  //The arena of this game
  private final Arena arena;
  //The game owner of this game
  private final Player gameOwner;
  //The spawn location of this game
  private final Location gameSpawnLocation;
  //The party of this game
  private final Party gameParty;
  //The bossbar of this game
  private final GameBossBar bossBar;
  //The name of the boss
  private final String bossName;
  private final String gameID;

  //Variables:
  //The boss entity
  private CreeperBoss boss;
  //The boss uuid
  private UUID bossUUID;
  //A region of the boss
  private EntityRegion bossRegion;
  //This variable saved the boss location to respawn the exploded boss on the same location;
  private Location savedLocation;
  //A boolean to check if the boss use an attack
  private boolean useBossAttack = false;
  //Booleans to check if a specified attack active
  private boolean ifAttackOneUsed = false;
  private boolean ifAttackTwoUsed = false;

  //Settings
  //The explosion radius of the boss
  private final int explosionRadius = 2;
  //The radius of the boss region
  private final int bossRegionRaius = 5;

  //PAPER
  private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(CreeperBoss.class, EntityDataSerializers.BOOLEAN);

  /**
   * This creates a new Creeper game and starts the game
   * @param arena Required an arena for the fight
   * @param gameOwner Requiredd an owner
   * @param gameSpawnLocation Required the spawn location of the arena
   * @param gameParty Required the party for this game
   * @param bossName Required a name for the boss
   */
  public CreeperGame(@NotNull Arena arena, @NotNull Player gameOwner, @NotNull Location gameSpawnLocation, @NotNull Party gameParty, @NotNull String bossName) {
    this.logger.logInfo("Set instances and variables...");
    int id = random.nextInt(100000, 999999);
    this.arena = arena;
    this.gameOwner = gameOwner;
    this.gameSpawnLocation = gameSpawnLocation;
    this.gameParty = gameParty;
    this.bossBar = new GameBossBar();
    this.bossName = bossName;
    this.gameID = Integer.toString(id);
    this.logger.logInfo("The gane id is " + gameID);
    this.logger.logInfo(gameID + " All variables and instances set!");
    //Register the listener of this class
    this.main.getServer().getPluginManager().registerEvents(this, this.main);
    //Set this game to active
    ALL_ACTIVE_GAMES.add(this);
    //Start the game
    new GameStartTimer().start(120*20, gameOwner, gameSpawnLocation, this.main);
  }

  /**
   * This methode starts the fight
   */
  protected void startGame(){
    //Create a runnable because this code can't be executing asynchron
    new BukkitRunnable() {
      @Override
      public void run() {
        CreeperGame.this.logger.logInfo(gameID + " Spawning boss...");
        //spawn boss and override die and explosion function of the creeper boss
        CreeperGame.this.boss = new CreeperBoss(CreeperGame.this.gameSpawnLocation, CreeperGame.this.bossName, null, null) {
          @Override
          public void die(@NotNull DamageSource damageSource){
            CreeperGame.this.die(damageSource, this);
          }

          @Override
          public void explodeCreeper(){
            CreeperGame.this.explodeCreeper(this);
          }
        };
        //Set uuid and create the bossbar
        CreeperGame.this.bossUUID = CreeperGame.this.boss.getUUID();
        CreeperGame.this.bossBar.create(CreeperGame.this.boss);
      }
    }.runTask(this.main);
  }

  /**
   * This methode ends the game
   */
  protected void endGame(){
    //Create a runnable because this code can't be executing asynchron
    new BukkitRunnable() {
      @Override
      public void run() {
        CreeperGame.this.logger.logInfo("Removing all game properties of the players and teleport they to the world \"world\"...");
        //Get every player of the party
        CreeperGame.this.gameParty.members().forEach(player -> {
          //Teleport all players to the world "world"
          player.teleport(CreeperGame.this.worldSpawnLocation);
          //Reset game state of the players
          playerStatusMap().replace(player, 0);
          //Send a message
          message(player, darkPurple("Thank you ").append(gray("for playing this ")).append(darkPurple("Bossfight")).append(gray("! We hope you ")).append(darkPurple("enjoyed ")).append(gray("it!")));
          CreeperGame.this.logger.logInfo("Players teleported and all player properties are removed.");
          CreeperGame.this.logger.logInfo("Removing party...");
          //Remove party
          CreeperGame.this.gameParty.removeParty(CreeperGame.this.gameOwner);
          CreeperGame.this.logger.logInfo("Party removed.");
          CreeperGame.this.logger.logInfo("Rebuilding Arena...");
          //Rebuilding the arena
          ArenaReBuilder.reBuildArena(CreeperGame.this.arena);
          CreeperGame.this.logger.logInfo("Arena rebuilt.");
          CreeperGame.this.logger.logInfo("Delete game...");
          //Deleting game
          ALL_ACTIVE_GAMES.remove(CreeperGame.this);
          CreeperGame.this.logger.logInfo("Game deleted.");
          CreeperGame.this.logger.logInfo("Game end! (" + gameID + ")");
        });
      }
    }.runTask(this.main);
  }

  /**
   * This methode kills the game
   * @param reason Requires a resaon for the kill
   * @param isRestart To check if the kill is a restart
   */
  @Override
  public void kill(String reason, boolean isRestart){
    if (!isRestart) {
      new BukkitRunnable() {
        @Override
        public void run() {
          //Send information that the game is ends
          CreeperGame.this.logger.logError("Game " + gameID + " was killed because of " + reason + "!");
          CreeperGame.this.logger.logWarning("Remove the game properties of all players and teleport this players back to World world...");
          //Remove the bossbar and Boss
          CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
          CreeperGame.this.gameParty.members().forEach(player -> {
            message(player, red("The game was killed because of " + reason + "!"));
            //teleport the player back to the world
            player.teleport(CreeperGame.this.worldSpawnLocation);
            //Reset the state of the players
            playerStatusMap().replace(player, 0);
          });
          CreeperGame.this.logger.logInfo("Properties of all players removed and teleported back to world!");
          CreeperGame.this.logger.logInfo("Remove party...");
          //Delete the party
          CreeperGame.this.gameParty.removeParty(CreeperGame.this.gameOwner);
          CreeperGame.this.logger.logInfo("Party was removed!");
          //Delete the game from the "active game list"
          ALL_ACTIVE_GAMES.remove(CreeperGame.class);
          message(CreeperGame.this.gameOwner, gray("Game is now killed!"));
        }
      }.runTask(this.main);
    }
  }

  /**
   * This methode is for the death of the boss
   * @param damageSource The source of damamge because the boss dies
   * @param entity Requires the boss
   */
  protected void die(@NotNull DamageSource damageSource, @NotNull Creeper entity){
    if (this.bossRegion == null) {
      //Create a strike lightning effect on the death location
      entity.getBukkitEntity().getWorld().strikeLightningEffect(entity.getBukkitEntity().getLocation());
      //Give every player the achievement for kill the creeper boss
      this.gameParty.members().forEach(player -> {
        giveAchievement(player, Achievements.BOSSFIGHT_CREEPER_END);
        //Give the player the drops
        new Drops(player, this.boss.getBukkitEntity(), player.getLocation());
      });
      //Remove the bossbar
      this.bossBar.remove(this.boss);
      this.logger.logInfo("The Boss is die!");
      this.logger.logInfo("End Game!");
      this.logger.logInfo("Start game end timer!");
      //Start the game end timer
      new GameEndTimer().start(60*20, this.gameOwner, this.worldSpawnLocation, this.main);
    } else {
      //Create a strike lightning effect on the death location
      entity.getBukkitEntity().getWorld().strikeLightningEffect(entity.getBukkitEntity().getLocation());
      //Give every player the achievement for kill the creeper boss
      this.gameParty.members().forEach(player -> giveAchievement(player, Achievements.BOSSFIGHT_CREEPER_END));
      //Remove the bossbar
      this.bossBar.remove(this.boss);
      this.logger.logInfo("The Boss is die!");
      this.logger.logInfo("End Game!");
      this.logger.logInfo("Start game end timer!");
      //Start the game end timer
      new GameEndTimer().start(60*20, this.gameOwner, this.worldSpawnLocation, this.main);
    }
  }

  /**
   * This methode is for the explosion of the boss
   * @param entity Requires the creeper entity (the boss)
   */
  protected void explodeCreeper(@NotNull Creeper entity) {
    if (!entity.level().isClientSide) {
      float explosiveStrength = entity.isPowered() ? 10.0F : 5.0F;
      //Craft Bukkit start
      ExplosionPrimeEvent event = CraftEventFactory.callExplosionPrimeEvent(entity, this.explosionRadius * explosiveStrength, true);
      if (!event.isCancelled()) {
        //Craft bukkit end
        /*
        Removed by Elia
        this.dead = true;
         */
        //Set new health and location
        float health = entity.getHealth();
        float newHealth = health + 100.0F;
        Location dieLocation = entity.getBukkitEntity().getLocation().clone();
        //create explosion and remove the old boss
        entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), event.getRadius(), event.getFire(), Level.ExplosionInteraction.MOB);
        entity.discard();
        //Remove the bossbar
        this.bossBar.remove(this.boss);
        //Create a new
        CreeperBoss newBoss = new CreeperBoss(dieLocation, this.bossName, newHealth, this.bossUUID){
          @Override
          public void die(@NotNull DamageSource damageSource) {
            CreeperGame.this.die(damageSource, this);
          }
          @Override
          public void explodeCreeper(){
            CreeperGame.this.explodeCreeper(this);
          }
        };
        this.boss = newBoss;
        //Create a new Boss bar
        this.bossBar.create(newBoss);
        //copied explode particle in this methode
        // CraftBukkit start
      } else {
        entity.swell = 0;
        entity.getEntityData().set(DATA_IS_IGNITED, Boolean.valueOf(false)); //Papaer
      }
      //Craftbukkit ends
    }
  }

  /**
   * This methode starts the first attack of the creeper
   * @param ticks Requires how long is the attack
   * @param location Requires the location of the attack
   */
  protected void startAttackOne(int ticks, Location location) {
    //Create a timer
    new BukkitRunnable() {
      private int ticksElapsed = 0;

      @Override
      public void run(){
        //checks if the time ends
        if (ticksElapsed >= ticks) {
          //cancel the runnable
          this.cancel();
          //Reset elapsed ticks
          ticksElapsed = 0;
          //Delete the region
          CreeperGame.this.bossRegion.delete();
          //Reset variables
          CreeperGame.this.ifAttackOneUsed = false;
          CreeperGame.this.useBossAttack = false;
          return;
        }
        //Create the region border for the boss
        createEntityRegionBorder(CreeperGame.this.explosionRadius, location, Particle.FLAME);
        ticksElapsed++;
      }
    }.runTaskTimer(this.main, 0, 20);
  }

  /**
   * This methode starts the second attack of the creeper
   */
  protected void startAttackTwo(){
    //Get every player of the game
    this.gameParty.members().forEach(player -> {
      //Generate a strike ligthning on the player
      player.getWorld().strikeLightning(player.getLocation());
      String miniCreeperName = player.getName() + "'s Creepersfriend";
      new MiniCreepers(player.getLocation(), miniCreeperName);
      player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 255, false, false, false));
      int randomTicks = random.nextInt(40, 100);
      new BukkitRunnable() {
        private int ticksElapsed = 0;

        @Override
        public void run(){
          if (ticksElapsed >= randomTicks) {
            //Send the player the message
            message(player, gray(miniCreeperName + ": Im here..."));
            //Cancel this runnable
            this.cancel();
            return;
          }
          ticksElapsed++;
        }
      }.runTaskTimer(this.main, 0, 1);
      player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_HURT, 0.7F, 0.3F);
    });
  }

  /**
   * This Event kills the game if the party owner disconnects
   * @param event This event is called if a player disconnects the server
   */
  @EventHandler
  private void onPlayerQuitServer(PlayerQuitEvent event) {
    //Checks if the player in the game
    if (this.gameParty.members().contains(event.getPlayer())) {
      if (event.getPlayer().getPersistentDataContainer().has(new NamespacedKey(this.main, Integer.toString(this.gameParty.id())))) {
        //Killed the game
        this.kill("Party owner disconnected", false);
        return;
      }else this.gameParty.removePlayer(event.getPlayer());
    }
  }

  /**
   * This Event saves a location of the died player
   * @param event This event is called if a player died
   */
  @EventHandler
  private void onPlayerDeath(PlayerDeathEvent event) {
    //Checks if the player in the game
    if (this.gameParty.members().contains(event.getPlayer())) {
      //Checks if the player the game owner
      if (event.getPlayer() == this.gameOwner) {
        this.kill("Game Owner is dead", false);
        return;
      }else this.gameParty.removePlayer(event.getPlayer());
    }
  }

  /**
   * This event loads the first attack
   * @param event This event is called if an entity damaged another entity
   */
  @EventHandler
  private void onBossUseAttack(EntityDamageByEntityEvent event) {
    //Checks if an attack is on
    if (useBossAttack) {
      //Checks if the first attack is on
      if (ifAttackOneUsed) {
        //Checks if the damager a projectile
        if (event.getDamager() instanceof Projectile projectile) {
          //Checks if the damaged entity the boss
          if (event.getEntity().getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
            if (containsEntity(this.boss.getBukkitEntity(), this.savedLocation, this.bossRegionRaius)) {
              //Cancel this event
              event.setCancelled(true);
              return;
            }
          }
        }
      }
    }
    int x = random.nextInt(1, 100);
    //checks if the number = < then 10
    if (x <= 20) {
      //Checks if the attack is not active
      if (!useBossAttack && !ifAttackOneUsed) {
        //Checks if the damaged entity the boss
        if (event.getEntity().getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
          //Checks if the damager a projectile
          if (event.getDamager() instanceof Projectile projectile) {
            //Set the attack on
            this.useBossAttack = this.ifAttackOneUsed = true;
            //Create a boss region
            this.bossRegion = new EntityRegion(event.getEntity().getLocation(), this.bossRegionRaius, this.boss.getBukkitEntity(), true, false);
            //start the attack
            this.startAttackOne(200,  event.getEntity().getLocation());
          }
        }
      }
    } else if (x >= 90) {
      //Checks if the damager a Player and create a variable of this Player
      if (event.getDamager() instanceof Player pDamager) {
        //Checks if the player in the party
        if (this.gameParty.members().contains(pDamager)) {
          //Checks if the boss entity the boss creeper boss
          if (event.getEntity().getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
            //Checks if the attack is not active
            if (!useBossAttack && !ifAttackTwoUsed) {
              //Set the attack active
              this.useBossAttack = this.ifAttackTwoUsed = true;
              this.startAttackTwo();
              int ticks = 220;
              new BukkitRunnable() {
                private int ticksElapsed = 0;

                @Override
                public void run() {
                  if (this.ticksElapsed >= ticks) {
                    //If that's correct, set active attack varriables to false
                    CreeperGame.this.useBossAttack = CreeperGame.this.ifAttackTwoUsed = false;
                    //Reset elapsed ticks
                    this.ticksElapsed = 0;
                    this.cancel();
                    return;
                  }
                  this.ticksElapsed++;
                }
              }.runTaskTimer(this.main, 0, 1);
            }
          }
        }else event.setCancelled(true);
      }else if (event.getDamager() instanceof Projectile) {
        //Checks if the boss entity the boss creeper boss
        if (event.getEntity().getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
          //Checks if the attack is not active
          if (!useBossAttack && !ifAttackTwoUsed) {
            //Set the attack active
            this.useBossAttack = this.ifAttackTwoUsed = true;
            this.startAttackTwo();
            int ticks = 220;
            new BukkitRunnable() {
              private int ticksElapsed = 0;

              @Override
              public void run() {
                if (this.ticksElapsed >= ticks) {
                  //If that's correct, set active attack varriables to false
                  CreeperGame.this.useBossAttack = CreeperGame.this.ifAttackTwoUsed = false;
                  //Reset elapsed ticks
                  this.ticksElapsed = 0;
                  this.cancel();
                  return;
                }
                this.ticksElapsed++;
              }
            }.runTaskTimer(this.main, 0, 1);
          }
        }
      }
    }
  }

  @EventHandler
  private void onBossMove(@NotNull EntityMoveEvent event) {
    if (useBossAttack && ifAttackOneUsed) {
      if (event.getEntity().getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
        if (!(this.bossRegion == null)) {
          this.bossRegion.getCenter().set(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
          System.out.println("Boss moved");
          System.out.println(this.bossRegion.getCenter());
        }
      }
    }
  }

  /**
   * This event updates the Bossbar
   * @param event This event is called if an entity gets damage
   */
  @EventHandler
  private void onUpdateBossBar(EntityDamageEvent event) {
    //Checks if the boss is null, if it true ends this code
    if (this.boss == null)return;
    //Checks if the entity the boss
    if (event.getEntity().getType() == EntityType.CREEPER) {
      if (this.boss.getUUID() == this.bossUUID) {
        //Update the bossbar
        this.bossBar.update(this.boss);
      }
    }
  }

  /**
   * This class creates, updates and removes the Bossbar
   */
  protected class GameBossBar {

    /**
     * This methode creates the Bossbar
     * @param boss Requires the boss of the game
     */
    protected void create(BossEntity boss){
      //Create a new bossbar
      BossBar bar = Bukkit.createBossBar("Test_BossBar_CreeperBoss", BarColor.PINK, BarStyle.SEGMENTED_10);
      //Add every player to the boss bar
      CreeperGame.this.gameParty.members().forEach(bar::addPlayer);
      //Divides the progress from the max value and the current value of life
      double progress = boss.health() / boss.maxHealth();
      //Set the progress
      bar.setProgress(progress);
      //Show the bossbar
      bar.setVisible(true);
      //Saves the bossbar in a map
      CreeperGame.bossBossBar.put(boss, bar);
    }

    /**
     * This methode updates the Bossbar
     * @param boss Requires the boss of the game
     */
    protected void update(BossEntity boss){
      //Checks if the boss registered in the map
      if (CreeperGame.bossBossBar.containsKey(boss)) {
        //Gets the bossbar of the map
        BossBar bar = CreeperGame.bossBossBar.get(boss);
        //Divides the progress from the max value and the current value of life
        double progress = boss.health() / boss.maxHealth();
        //Set the progress
        bar.setProgress(progress);
      }
    }

    /**
     * This methode removes the Bossbar
     * @param boss Requires the boss of the game
     */
    protected void remove(BossEntity boss){
      //Checks if the boss registered in the map
      if (CreeperGame.bossBossBar.containsKey(boss)) {
        if (CreeperGame.this.boss.isAlive()) {
          CreeperGame.this.boss.remove(RemovalReason.KILLED);
        }
        //Gets the bossbar of the map
        BossBar bar = CreeperGame.bossBossBar.get(boss);
        //Remove the bossbar
        bar.removeAll();
        CreeperGame.bossBossBar.remove(boss);
      }
    }

  }

  /**
   * This class starts a timer for the start
   */
  protected class GameStartTimer extends TimerTasks {

    /**
     * Send a message to every player in the game party
     * @param seconds Requires the time for the message
     */
    protected void timeMessage(int seconds){
      CreeperGame.this.gameParty.members().forEach(player -> message(player, gray("This game starts in ").append(darkPurple(Integer.toString(seconds))).append(gray(" seconds!"))));
    }

    /**
     * Override the start methode
     * @param time Requires the time in ticks
     * @param player Requires a player
     * @param location A location that you need in the timer but can be null
     * @param plugin Requires an instance of the plugin
     */
    @Override
    public void start(int time, Player player, Location location, @NotNull Plugin plugin) {
      (new TimerUtils()).countdownAndRun(time, new Runnable() {
        public void run() {
          //Send information thats the countdown starts
          CreeperGame.this.logger.logInfo("Start countdown started!");
          message(CreeperGame.this.gameOwner, gray("The bossfight ").append(darkPurple("starts")).append(gray("!")));
        }
      }, plugin);
      (new TimerUtils()).countdownInterval(time, new TimerUtils.TimeRunnable() {
        public void run(int ticks) {
          if (ticks % 20 == 0) {
            int seconds = ticks / 20;
            //Send a message if the time 100 seconds
            if (seconds == 100) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if time 80 seconds
            if (seconds == 80) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 60 seconds
            if (seconds == 60) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if time 40 seconds
            if (seconds == 40) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send amessage if the time 30 seconds
            if (seconds == 30) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if time 20 seconds
            if (seconds == 20) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 15 seconds
            if (seconds == 15) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Checks if the time 10 seconds
            if (seconds == 10) {
              CreeperGame.GameStartTimer.this.timeMessage(seconds);
              new BukkitRunnable() {
                @Override
                public void run() {
                  //Send the information message to teleport the players
                  CreeperGame.this.logger.logInfo("Teleport players to the game!");
                  message(CreeperGame.this.gameOwner, gray("You and your team will be teleported in the arena!"));
                  CreeperGame.this.gameParty.members().forEach(player -> player.teleport(location));
                }
              }.runTask(CreeperGame.this.main);
            }
            //Send a message if the time 5 seconds
            if (seconds == 5) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 4 seconds
            if (seconds == 4) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 3 seconds
            if (seconds == 3) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 2 seconds
            if (seconds == 2) CreeperGame.GameStartTimer.this.timeMessage(seconds);
            //Send a message if the time 1 second
            if (seconds == 1) CreeperGame.GameStartTimer.this.timeMessage(seconds);
          }

        }
      }, new Runnable() {
        public void run() {
          //Send an information message to the console because the game starts
          CreeperGame.this.logger.logInfo("Game start time is over!");
          CreeperGame.this.logger.logInfo("Game starts!");
          //Start the game
          CreeperGame.this.startGame();
        }
      }, plugin);
    }

  }

  /**
   * This class starts a timer for the end
   */
  protected class GameEndTimer extends TimerTasks {

    /**
     * Send a message to every player in the game party
     * @param seconds Requires the time for the message
     */
    protected void timeMessage(int seconds){
      CreeperGame.this.gameParty.members().forEach(player -> message(player, gray("This game ends in ").append(darkPurple(Integer.toString(seconds))).append(gray(" seconds!"))));
    }

    /**
     * Override the start methode
     * @param time Requires the time in ticks
     * @param player Requires a player
     * @param location A location that you need in the timer but can be null
     * @param plugin Requires an instance of the plugin
     */
    @Override
    public void start(int time, Player player, Location location, @NotNull Plugin plugin) {
      (new TimerUtils()).countdownAndRun(time, new Runnable() {
        public void run() {
          //Checks if the boss is alive
          if (CreeperGame.this.boss.isAlive()) {
            //Kill the boss
            CreeperGame.this.boss.remove(RemovalReason.KILLED);
          }
          //Send information thats the countdown starts
          CreeperGame.this.logger.logInfo("End countdown started!");
        }
      }, plugin);
      (new TimerUtils()).countdownInterval(time, new TimerUtils.TimeRunnable() {
        public void run(int ticks) {
          if (ticks % 20 == 0) {
            int seconds = ticks / 20;
            //Send a message if the time 60 seconds
            if (seconds == 60) CreeperGame.GameEndTimer.this.timeMessage(seconds);
            //Send a message if the time 50 seconds
            if (seconds == 50) CreeperGame.GameEndTimer.this.timeMessage(seconds);
            //Send a message if the time 30 seconds
            if (seconds == 30) CreeperGame.GameEndTimer.this.timeMessage(seconds);
            //Send a message if the time 20 seconds
            if (seconds == 20) CreeperGame.GameEndTimer.this.timeMessage(seconds);
            //Send a message if the time 10 seconds
            if (seconds == 10) CreeperGame.GameEndTimer.this.timeMessage(seconds);
            //Send a message if the time 5 seconds
            if (seconds == 5) CreeperGame.GameEndTimer.this.timeMessage(seconds);
            //Send a message if the time 3 seconds
            if (seconds == 3) CreeperGame.GameEndTimer.this.timeMessage(seconds);
            //Send a message if the time 2 seconds
            if (seconds == 2) CreeperGame.GameEndTimer.this.timeMessage(seconds);
            //Send a message if the time 1 second
            if (seconds == 1) CreeperGame.GameEndTimer.this.timeMessage(seconds);
          }

        }
      }, new Runnable() {
        public void run() {
          //Send an information message to the console because the game starts
          CreeperGame.this.logger.logInfo("Game end time is over!");
          CreeperGame.this.logger.logInfo("Game ends!");
          //End the game
          CreeperGame.this.endGame();
        }
      }, plugin);
    }
  }
}
