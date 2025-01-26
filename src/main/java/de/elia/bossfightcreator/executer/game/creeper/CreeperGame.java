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
import de.elia.soulboss.SoulBoss;
import de.elia.soulboss.entitys.creeper.CreeperBoss;
import de.elia.soulboss.entitys.creeper.drop.Drops;
import de.elia.soulboss.entitys.creeper.minis.MiniCreepers;
import de.elia.systemclasses.keys.NameSpacedKeys;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
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
import org.bukkit.entity.Entity;
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

/**
 * This class processed the Creeper Bossfights
 *
 * @author Elia
 * @since 3.0.0
 */
public class CreeperGame implements Game, Listener {

  //Instances:
  //An instance of the plugin logger
  private final PluginLogger logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
  //An instance of the spawn location in the world "world"
  private final Location mainSpawnLocation = Bukkit.getWorld("world").getSpawnLocation();
  //An instance of the main class
  private final JavaPlugin plugin;

  //Variables:
  //The arena of this game
  private final Arena arena;
  //The game owner for this game
  private final Player gameOwner;
  //The spawnlocation for the creeper boss
  private final Location spawnLocation;
  //The party for this game
  private final Party gameParty;
  //The bossbar for the boss of this game
  private final GameBossBar bossBar;
  //The name of the boss
  private final String bossName;
  //The boss entity
  private CreeperBoss boss;
  //The uuid of the boos
  private UUID bossUUID;
  //A region of the boss
  private EntityRegion region;
  //This variable saved the explosion-death location to respawn the boss on the same location
  private Location savedLocation;
  //A boolean to check if the boss used an attack
  private boolean ifAttackActive;

  //Settings
  //the explosion radius of the creeper boss
  private final int explosionRadius = 2;
  //The radius of the entity region
  private final double radius = 5;
  //Booleans to check if the specified attack used
  private boolean isArrowSaveAttackOn = false;
  private boolean isMiniCreeperAttackOn = false;

  //OTHERS
  //PAPER
  private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(CreeperBoss.class, EntityDataSerializers.BOOLEAN);

  /**
   * This creates a new Creeper game
   *
   * @param arena         The arena for this game
   * @param gameOwner     The game owner for this game
   * @param plugin        The main instance
   * @param spawnLocation The spawn location for all entities
   * @param gameParty     The party for this game
   */
  public CreeperGame(@NotNull Arena arena, @NotNull Player gameOwner, @NotNull JavaPlugin plugin, @NotNull Location spawnLocation, @NotNull Party gameParty) {
    this.arena = arena;
    this.gameOwner = gameOwner;
    this.plugin = plugin;
    this.spawnLocation = spawnLocation;
    this.gameParty = gameParty;
    this.bossBar = new GameBossBar();
    //Register this class as event listener
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    this.logger.logInfo("Start the Countdown for the start");
    //Add this game to the active game list
    ALL_ACTIVE_GAMES.add(this);
    //Set the boss name
    bossName = "TEST";
    //Start the start timer for this game
    new StartGameTimer().start(120 * 20, gameOwner, spawnLocation, plugin);
  }

  /**
   * This methode starts the game
   */
  protected void startGame() {
    //Create a runnable because this code can't be executing asynchron
    new BukkitRunnable() {
      @Override
      public void run() {
        CreeperGame.this.logger.logInfo("Spawn Boss...");
        //Spawn the boss and override the methode die and explodeCreeper
        CreeperGame.this.boss = new CreeperBoss(CreeperGame.this.spawnLocation, CreeperGame.this.bossName, null, null) {
          //Override die methode
          @Override
          public void die(@NotNull DamageSource damageSource) {
            CreeperGame.this.die(damageSource, CreeperGame.this.boss);
          }

          //Override explodeCreeper methode
          @Override
          public void explodeCreeper() {
            CreeperGame.this.explodeCreeper(CreeperGame.this.boss);
          }
        };
        //Set uuid
        bossUUID = CreeperGame.this.boss.getUUID();
        //Create bossbar
        CreeperGame.this.bossBar.create(CreeperGame.this.boss);
      }
      //Execute the runnable
    }.runTask(this.plugin);
  }

  /**
   * This methode ends the game
   */
  protected void endGame() {
    //Create a runnable because this code can't be executing asynchron
    new BukkitRunnable() {
      @Override
      public void run() {
        CreeperGame.this.logger.logInfo("Remove the game properties of all players and teleport this players back to World world...");
        //Get every player in the party.
        CreeperGame.this.gameParty.members().forEach(player -> {
          //Teleport the player back to the main spawn location of the server
          player.teleport(mainSpawnLocation);
          //Reset the state of the player
          playerStatusMap().replace(player, 0);
          //send Message
          message(player, darkPurple("Thank you ").append(gray("for playing this ")).append(darkPurple("boss fight")).append(gray("! We hope you ")).append(darkPurple("enjoyed ")).append(gray("it!")));
        });
        CreeperGame.this.logger.logInfo("Properties removed!");
        CreeperGame.this.logger.logInfo("Remove party...");
        //Delete the party of this game
        CreeperGame.this.gameParty.removeParty(CreeperGame.this.gameOwner);
        CreeperGame.this.logger.logInfo("Party removed!");
        CreeperGame.this.logger.logInfo("Rebuild the arena...");
        //Rebuild the arena of this game
        ArenaReBuilder.reBuildArena(CreeperGame.this.arena);
        CreeperGame.this.logger.logInfo("Arena rebuild!");
        //Delete the game of the active game list
        ALL_ACTIVE_GAMES.remove(CreeperGame.this);
        message(CreeperGame.this.gameOwner, gray("Game end!"));
      }
    }.runTask(this.plugin);
  }

  /**
   * This methode kills this game
   *
   * @param reason    Required the reason
   * @param isRestart If not a restart, unload the game "normal"
   */
  @Override
  public void kill(String reason, boolean isRestart) {
    killGame(reason, isRestart);
  }

  /**
   * This methode kills this game
   *
   * @param reason    Required the reason
   * @param isRestart If not a restart, unload the game "normal"
   */
  protected void killGame(String reason, boolean isRestart) {
    if (!isRestart) {
      //Create a runnable because this code can't be executing asynchron
      new BukkitRunnable() {
        @Override
        public void run() {
          CreeperGame.this.logger.logInfo("Remove the game properties of all players and teleport this players back to World world...");
          //Get every player in the party.
          CreeperGame.this.gameParty.members().forEach(player -> {
            //Teleport the player back to the main spawn location of the server
            player.teleport(mainSpawnLocation);
            //Reset the state of the player
            playerStatusMap().replace(player, 0);
            message(player, red("DAS GAME WURDE BEENDET WEIL: " + reason + "!"));
            //Remove the bossbar
            CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
          });
          CreeperGame.this.logger.logInfo("Properties removed!");
          CreeperGame.this.logger.logInfo("Remove party...");
          //Delete the party of this game
          CreeperGame.this.gameParty.removeParty(CreeperGame.this.gameOwner);
          CreeperGame.this.logger.logInfo("Party removed!");
          //Delete the game of the active game list
          ALL_ACTIVE_GAMES.remove(CreeperGame.this);
          message(CreeperGame.this.gameOwner, gray("Game end!"));
        }
      }.runTask(this.plugin);
    }
  }

  /**
   * This methode executes the tasks if the creeper dies
   *
   * @param damageSource The source of damamge because the boss dies
   * @param entity       Requires the boss
   */
  public void die(@NotNull DamageSource damageSource, Creeper entity) {
    if (CreeperGame.this.region == null) {
      //Generate a strike lightning effect on the death location of the boss
      entity.getBukkitEntity().getWorld().strikeLightningEffect(entity.getBukkitEntity().getLocation());
      //Give all players the achievement for killing the boss
      CreeperGame.this.gameParty.members().forEach(player -> giveAchievement(player, Achievements.BOSSFIGHT_CREEPER_END));
      //Generate drops for the players
      //TODO: FOR ALL PLAYERS IN THE PARTY DROPS
      new Drops(this.gameOwner, entity.getBukkitEntity(), entity.getBukkitEntity().getLocation());
      //Remove the bossbar
      CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
      CreeperGame.this.logger.logInfo("The Boss is die!");
      CreeperGame.this.logger.logInfo("End Game!");
      CreeperGame.this.logger.logInfo("Start game end timer!");
      //Start for every player in the party the end timer
      CreeperGame.this.gameParty.members().forEach(player -> new EndGameTimer().start(60 * 20, player, CreeperGame.this.mainSpawnLocation, CreeperGame.this.plugin));
    } else {
      //Generate a strike lightning effect on the death location of the boss
      entity.getBukkitEntity().getWorld().strikeLightningEffect(entity.getBukkitEntity().getLocation());
      //Give all players the achievement for killing the boss
      CreeperGame.this.gameParty.members().forEach(player -> giveAchievement(player, Achievements.BOSSFIGHT_CREEPER_END));
      //Remove the bossbar
      CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
      CreeperGame.this.region.delete();
      CreeperGame.this.logger.logInfo("The Boss is die!");
      CreeperGame.this.logger.logInfo("End Game!");
      CreeperGame.this.logger.logInfo("Start game end timer!");
      //Start the game end timer
      new EndGameTimer().start(61 * 20, null, CreeperGame.this.mainSpawnLocation, CreeperGame.this.plugin);
    }

  }

  /**
   * This methode executes the tasks if the creeper exploded
   *
   * @param entity Required the {@link Creeper} boss
   */
  public void explodeCreeper(@NotNull Creeper entity) {
    if (!entity.level().isClientSide) {
      float f = entity.isPowered() ? 10.0F : 5.0F;//Edit by Elia
      // CraftBukkit start
      ExplosionPrimeEvent event = CraftEventFactory.callExplosionPrimeEvent(entity, CreeperGame.this.explosionRadius * f, true);
      if (!event.isCancelled()) {
        // CraftBukkit end
        /*
        Removed by elia
        this.dead = true;
         */
        //Set new Health and location
        float health = entity.getHealth();
        float newHealth = health + 100.0F;
        Location l = entity.getBukkitEntity().getLocation().clone();
        //Set Explosion and remove the old boss
        entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), event.getRadius(), event.getFire(), Level.ExplosionInteraction.MOB); // CraftBukkit
        entity.discard();
        //Remove of the old boss the bossbar
        CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
        //Create a new Boss
        CreeperBoss newBoss = new CreeperBoss(l, CreeperGame.this.bossName, newHealth, CreeperGame.this.bossUUID) {
          //Override the die methode (for more details, check the called die methode)
          @Override
          public void die(@NotNull DamageSource damageSource) {
            CreeperGame.this.die(damageSource, CreeperGame.this.boss);
          }

          //Override the explodeCreeper methode (for more details, check the called explodeCreeper methode)
          @Override
          public void explodeCreeper() {
            CreeperGame.this.explodeCreeper(CreeperGame.this.boss);
          }
        };
        bossUUID = CreeperGame.this.boss.getUUID();
        CreeperGame.this.boss = newBoss;
        //Create a new Bossbar
        CreeperGame.this.bossBar.create(newBoss);
        //copied explode particle in this methode
        // CraftBukkit start
      } else {
        entity.swell = 0;
        entity.getEntityData().set(DATA_IS_IGNITED, Boolean.valueOf(false)); // Paper
        CreeperGame.this.logger.logError("org.bukkit.event.entity.ExplosionPrimeEvent is cancelled! The BossFight is The boss fight is therefore more prone to errors!");//Elia
      }
      // CraftBukkit end
    }
  }

  /**
   * This methode starts the first attack of the creeper
   *
   * @param seconds  The seconds how long the attack lasts
   * @param location The location for the attack
   */
  protected void startAttack1(int seconds, Location location) {
    //Create a timer
    new BukkitRunnable() {
      private int ticksElapsed = 0;

      @Override
      public void run() {
        //Checks if the time ends
        if (ticksElapsed >= seconds) {
          //canceled this runnable
          this.cancel();
          //Reset elapsed ticks
          ticksElapsed = 0;
          //delete the region
          CreeperGame.this.region.delete();
          //Reset the variables
          CreeperGame.this.savedLocation = null;
          CreeperGame.this.ifAttackActive = false;
          CreeperGame.this.isArrowSaveAttackOn = false;
          return;
        }
        //Create the region border for the boss
        createEntityRegionBorder(CreeperGame.this.radius, location, Particle.FLAME);
        ticksElapsed++;
      }
    }.runTaskTimer(SoulBoss.soulBoss().main(), 0, 1);
  }

  /**
   * This methode starts the second attack of the creeper
   */
  public void startAttack2() {
    //Get every player in the game
    this.gameParty.members().forEach(player -> {
      //The location of the player
      Location playerLocation = player.getLocation();
      //Generate strike lightning by the player
      playerLocation.getWorld().strikeLightning(playerLocation);
      //Create the name for the creeper
      String creeperName = player.getName() + "'s Creeperfriend";
      //Spawned a new creeper for the player
      new MiniCreepers(playerLocation, creeperName);
      //Give the player blindness for 10 seconds
      player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 255, false, false, false));
      //Create a random time (in seconds) until the message is sent by the mini creeper
      Random random = new Random();
      int x = random.nextInt(100, 180);
      new BukkitRunnable() {
        private int y = 0;

        @Override
        public void run() {
          if (y >= x) {
            //Send the player the message
            message(player, gray(creeperName + ": Im here..."));
            //Canceled this runnable
            this.cancel();
          }
          y++;
        }
      }.runTaskTimer(this.plugin, 0, 1);
      //Play a sound
      player.playSound(playerLocation, Sound.ENTITY_CREEPER_HURT, 0.7f, 0.3f);
    });
  }

  /**
   * This Event kills the game if the party owner disconnects
   *
   * @param event This event is called if a player disconnects the server
   */
  @EventHandler
  private void onPlayerQuitServer(@NotNull PlayerQuitEvent event) {
    //Get the player from the event
    Player player = event.getPlayer();
    //Checks if the player in the game
    if (this.gameParty.members().contains(player)) {
      //Checks if the player the game owner
      if (player.getPersistentDataContainer().has(new NamespacedKey(BossFightCreatorMain.bossFightCreator().main(), Integer.toString(this.gameParty.id())))) {
        //Killed the game
        this.killGame("Party Owner disconnected!", false);
        return;
        //If the player in the party but not the owner, delete the player from the party
      } else this.gameParty.removePlayer(player);
    }
  }

  /**
   * This Event saves a location of the died player
   *
   * @param event This event is called if a player died
   */
  @EventHandler
  private void onPlayerDie(@NotNull PlayerDeathEvent event) {
    //Get the player from the event
    Player player = event.getPlayer();
    //Checks if the player in the game
    if (this.gameParty.members().contains(player)) {
      //Checks if the player the game owner
      if (player == this.gameOwner) {
        //Start the end timer of this game
        new EndGameTimer().start(61 * 20, this.gameOwner, this.mainSpawnLocation, this.plugin);
      } else {
        //Remove the player from the party
        this.gameParty.removePlayer(player);
      }
    }
  }


  /**
   * This event loads the attacks
   *
   * @param event This event is called if an entity damaged another entity
   */
  @EventHandler
  private void onInRegionAttack(@NotNull EntityDamageByEntityEvent event) {
    //Get the damaged entity
    Entity damagedEntity = event.getEntity();
    //Get the damager
    Entity damager = event.getDamager();
    //Get the location of the damaged entity
    Location location = damagedEntity.getLocation();
    //Checks if the arrow saves attack active
    if (isArrowSaveAttackOn) ;
    //Checks if the damager a projectile
    if (damager instanceof Projectile projectile) ;
    //Checks if the damaged entity the boss
    if (damagedEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) ;
    //Checks if the region contains the boss
    if (containsEntity(boss.getBukkitEntity(), savedLocation, this.radius)) {
      //cancel the event
      event.setCancelled(true);
      return;
    }
    //Generate a random number
    Random random = new Random();
    int randomNumber = random.nextInt(1, 100);
    //Checks if the number = or < then 10
    if (randomNumber <= 10) ;
    //Checks if an attack active
    if (!this.ifAttackActive) ;
    //Checks if the damaged entity the boss
    if (damagedEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) ;
    //Checks if the damager a projectile
    if (damager instanceof Projectile projectile)
      //Checks if the arrow saves attack not active
      if (!isArrowSaveAttackOn) {
        //set the attack to active
        this.ifAttackActive = true;
        this.isArrowSaveAttackOn = true;
        //Cloned the location and saved this
        this.savedLocation = location.clone();
        //Create a new region
        this.region = new EntityRegion(location, this.radius, this.boss.getBukkitEntity(), true, false);
        //Start the attack
        this.startAttack1(200, location);
        //Set this event to cancel
        event.setCancelled(true);
      }
  }

  /**
   * This event loads the second attack
   *
   * @param event This event is called if an entity damaged another entity
   */
  @EventHandler
  private void onBossDamage(@NotNull EntityDamageByEntityEvent event) {
    //Get the boss
    Entity bossEntity = event.getEntity();
    //Get the damager
    Entity damager = event.getDamager();
    //Generate a new random number
    Random random = new Random();
    int randomAttackNumber = random.nextInt(1, 200);
    //Checks if the damager a Player and create a variable of this Player
    if (damager instanceof Player pDamager) {
      //Checks if the player in the party
      if (this.gameParty.members().contains(pDamager)) ;
      //Checks if the boss entity the boss creeper boss
      if (bossEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) ;
      //Check if an attack not active
      if (!this.ifAttackActive) ;
      //Check if the other attack active
      if (!this.isMiniCreeperAttackOn) ;
      //Check if the random number smaller than 10
      if (randomAttackNumber < 10) {
        //Sets thats an attack used
        this.ifAttackActive = true;
        //Sets that the mini creeper attack is used
        this.isMiniCreeperAttackOn = true;
        //Start this attack
        this.startAttack2();
        //Set the time for the runnable in ticks
        int x = 220;
        new BukkitRunnable() {
          //A variable of the elapsed time
          private int y = 0;

          @Override
          public void run() {
            //Checks if the elapsed time eqals or higher with than x
            if (y >= x) {
              //If that's correct set active attack varriables to false
              CreeperGame.this.ifAttackActive = false;
              CreeperGame.this.isMiniCreeperAttackOn = false;
              //Reset y
              y = 0;
              //Cancel this runnable
              this.cancel();
            }
            //Count y by a tick higher
            y++;
          }
          //Defines the runnable
        }.runTaskTimer(this.plugin, 0, 1);
      }
      //Checks if the damager a Projective and create a variable of this Projectile
    } else if (damager instanceof Projectile projectile) {
      //Checks if the boss entity the boss creeper boss
      if (bossEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) ;
      //Check if an attack not active
      if (!this.ifAttackActive) ;
      //Check if the other attack active
      if (!this.isMiniCreeperAttackOn) ;
      //Check if the random number smaller than 10
      if (randomAttackNumber < 30) {
        //Sets thats an attack used
        this.ifAttackActive = true;
        //Sets that the mini creeper attack is used
        this.isMiniCreeperAttackOn = true;
        //Start this attack
        this.startAttack2();
        //Set the time for the runnable in ticks
        int x = 220;
        new BukkitRunnable() {
          //A variable of the elapsed time
          private int y = 0;

          @Override
          public void run() {
            //Checks if the elapsed time eqals or higher with than x
            if (y >= x) {
              //If that's correct set active attack varriables to false
              CreeperGame.this.ifAttackActive = false;
              CreeperGame.this.isMiniCreeperAttackOn = false;
              //Reset y
              y = 0;
              //Cancel this runnable
              this.cancel();
            }
            //Count y by a tick higher
            y++;
          }
          //Defines the runnable
        }.runTaskTimer(this.plugin, 0, 1);
      }
    }
  }

  /**
   * This event updates the Bossbar
   *
   * @param event This event is called if an entity get damage
   */
  @EventHandler
  private void onUpdateBar(@NotNull EntityDamageEvent event) {
    //Get the entity of the event
    Entity entity = event.getEntity();
    //Checks if the entity type a creeper
    if (entity.getType() == EntityType.CREEPER) {
      //Checks if the boss is null, if it true ends this code
      if (CreeperGame.this.boss == null) return;
      //Checks if the boss uuid the same of the saved uuid of this game
      if (CreeperGame.this.boss.getUUID() == this.bossUUID) {
        //Update the bossbar
        this.bossBar.update(CreeperGame.this.boss);
      }
    }
  }

  /**
   * This class creates, updates and removes the Bossbar
   */
  private class GameBossBar {



    /**
     * This methode creates the Bossbar
     * @param boss Requires the boss of the game
     */
    protected void create(BossEntity boss) {
      //Create a new boss bar
      BossBar bossBar = Bukkit.createBossBar("Test_BossBar_Creeper", BarColor.PINK, BarStyle.SEGMENTED_10);
      //Add every player of the game party to the boss bar
      CreeperGame.this.gameParty.members().forEach(bossBar::addPlayer);
      //Divides the progress from the max value and the current value of life
      double progress = boss.health() / boss.maxHealth();
      //Set the progress
      bossBar.setProgress(progress);
      //Shows the boss bar
      bossBar.setVisible(true);
      //saves the bossbar in a map
      CreeperGame.bossBossBar.put(boss, bossBar);
    }

    /**
     * This methode updates the Bossbar
     * @param boss Requires the boss of the game
     */
    protected void update(BossEntity boss) {
      //Checks if the boss registerd in the map
      if (CreeperGame.bossBossBar.containsKey(boss)) {
        //Gets the boss bar of the map
        BossBar bossBar = CreeperGame.bossBossBar.get(boss);
        //Divides the progress from the max value and the current value of life
        double progress = boss.health() / boss.maxHealth();
        //Set the progress
        bossBar.setProgress(progress);
      }
    }

    //This methode removes the Bossbar
    protected void remove(BossEntity boss) {
      //Checks if the boss registerd in the map
      if (CreeperGame.bossBossBar.containsKey(boss)) {
        //Gets the boss bar of the map
        BossBar bossBar = CreeperGame.bossBossBar.get(boss);
        //Remove all players of the bossbar
        bossBar.removeAll();
        //Delete the bossbar of the map
        CreeperGame.bossBossBar.remove(boss);
      }
    }

  }

  /**
   * This class starts a timer for the start
   */
  private class StartGameTimer extends TimerTasks {

    /**
     * Send a message to every player in the game party
     *
     * @param time Requires the time for the message
     */
    private void timeMessage(int time) {
      CreeperGame.this.gameParty.members().forEach(partyPlayer -> message(partyPlayer, gray("This game starts in ").append(darkPurple(Integer.toString(time))).append(gray(" seconds!"))));
    }

    /**
     * Override the start methode
     *
     * @param time     Requires the time in ticks
     * @param player   Requires a player
     * @param location A location that you need in the timer, but can be null
     * @param plugin   Requires the instance of the plugin
     */
    @Override
    public void start(int time, Player player, Location location, @NotNull Plugin plugin) {
      //Create a new Countdown
      new TimerUtils().countdownAndRun(time, new Runnable() {
        @Override
        public void run() {
          //Send the information that's the countdown starts
          CreeperGame.this.logger.logInfo("Start countdown started!");
          message(CreeperGame.this.gameOwner, gray("The bossfight ").append(darkPurple("starts")).append(gray("!")));
        }
      }, plugin);
      new TimerUtils().countdownInterval(time, new TimerUtils.TimeRunnable() {
        @Override
        public void run(int ticks) {
          //Checks if the ticks null
          if (ticks % 20 == 0) {
            //Divide ticks by 20 to get the time in seconds
            int secounds = ticks / 20;
            //Send a message if the time 100 secounds
            if (secounds == 100) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 80 secounds
            if (secounds == 80) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 60 secounds
            if (secounds == 60) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 40 secounds
            if (secounds == 40) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 30 secounds
            if (secounds == 30) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 20 secounds
            if (secounds == 20) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 15 secounds
            if (secounds == 15) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Checks if the time 10 secounds
            if (secounds == 10) {
              //Send a message
              CreeperGame.StartGameTimer.this.timeMessage(secounds);
              new BukkitRunnable() {
                @Override
                public void run() {
                  //Send the messages for the teleport action
                  CreeperGame.this.logger.logInfo("Teleport all Players in the Arena!");
                  message(CreeperGame.this.gameOwner, gray("You and your team will be teleported in the arena!"));
                  //Teleport all players to the arena
                  CreeperGame.this.gameParty.members().forEach(player -> player.teleport(location));
                }
              }.runTask(plugin);
            }
            //Send a message if the time 5 secounds
            if (secounds == 5) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 4 secounds
            if (secounds == 4) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 3 secounds
            if (secounds == 3) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 2 secounds
            if (secounds == 2) CreeperGame.StartGameTimer.this.timeMessage(secounds);
            //Send a message if the time 1 secounds
            if (secounds == 1) CreeperGame.StartGameTimer.this.timeMessage(secounds);
          }
        }
      }, new Runnable() {
        @Override
        public void run() {
          //Send the logs to start the game
          CreeperGame.this.logger.logInfo("Game start timer ends!");
          CreeperGame.this.logger.logInfo("Start fight!");
          //Start the game
          CreeperGame.this.startGame();
        }
      }, plugin);
    }
  }

  /**
   * This class starts a timer for the end
   */
  private class EndGameTimer extends TimerTasks {

    /**
     * Send a message to every player in the game party
     *
     * @param time Requires the time for the message
     */
    private void timeMessage(int time) {
      CreeperGame.this.gameParty.members().forEach(gamePlayer -> message(gamePlayer, gray("The game ends in ").append(darkPurple(Integer.toString(time))).append(gray(" seconds!"))));
    }

    /**
     * Override the start methode
     *
     * @param time     Requires the time in ticks
     * @param player   Requires a player
     * @param location A location that you need in the timer, but can be null
     * @param plugin   Requires the instance of the plugin
     */
    @Override
    public void start(int time, Player player, Location location, @NotNull Plugin plugin) {
      new TimerUtils().countdownAndRun(time, new Runnable() {
        @Override
        public void run() {
          new BukkitRunnable() {
            @Override
            public void run() {
              //Checks if the boss not dead
              if (!CreeperGame.this.boss.isDeadOrDying()) {
                //Set the health to 0
                CreeperGame.this.boss.setHealth(0.0F);
                //Remove the bossbar
                CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
              }
            }
          }.runTask(CreeperGame.this.plugin);
        }
      }, plugin);
      new TimerUtils().countdownInterval(time, new TimerUtils.TimeRunnable() {
        @Override
        public void run(int ticks) {
          if (ticks % 20 == 0) {
            int secounds = ticks / 20;
            //Send a message if the time 60 secounds
            if (secounds == 60) CreeperGame.EndGameTimer.this.timeMessage(secounds);
            //Send a message if the time 50 secounds
            if (secounds == 50) CreeperGame.EndGameTimer.this.timeMessage(secounds);
            //Send a message if the time 30 secounds
            if (secounds == 30) CreeperGame.EndGameTimer.this.timeMessage(secounds);
            //Send a message if the time 20 secounds
            if (secounds == 20) CreeperGame.EndGameTimer.this.timeMessage(secounds);
            //Send a message if the time 10 secounds
            if (secounds == 10) CreeperGame.EndGameTimer.this.timeMessage(secounds);
            //Send a message if the time 5 secounds
            if (secounds == 5) CreeperGame.EndGameTimer.this.timeMessage(secounds);
            //Send a message if the time 3 secounds
            if (secounds == 3) CreeperGame.EndGameTimer.this.timeMessage(secounds);
            //Send a message if the time 2 secounds
            if (secounds == 2) CreeperGame.EndGameTimer.this.timeMessage(secounds);
            //Send a message if the time 1 secounds
            if (secounds == 1) CreeperGame.EndGameTimer.this.timeMessage(secounds);
          }
        }

      }, new Runnable() {
        @Override
        public void run() {
          //Ends the game
          CreeperGame.this.endGame();
        }
      }, plugin);
    }
  }
}
