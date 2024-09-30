package de.elia.bossfightcreator.executer.game.creeper;

import de.elia.api.achievements.Achievements;
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
import de.elia.soulboss.entitys.BossEntity;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static de.elia.achivementssystem.achievement.Achievement.giveAchievement;
import static de.elia.api.entityRegion.EntityRegionBuilder.containsEntity;
import static de.elia.api.entityRegion.EntityRegionBuilder.createEntityRegionBorder;
import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.darkPurple;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.bossfightcreator.BossFightCreatorMain.playerStatusMap;

//This class processed the Creeper Bossfights
public class CreeperGame implements Game, Listener {

  private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(CreeperBoss.class, EntityDataSerializers.BOOLEAN);
  private final Map<Player, Location> lastPlayerLocations = new HashMap<>();
  private final PluginLogger logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
  private final Location mainSpawnLocation = Bukkit.getWorld("world").getSpawnLocation();
  private final int explosionRadius = 2;
  private final Arena arena;
  private final Player gameOwner;
  private final JavaPlugin plugin;
  private final Location spawnLocation;
  private final Party gameParty;
  private final GameBossBar bossBar;
  private final String bossName;
  private final double radius = 5;
  private CreeperBoss boss;
  private UUID bossUUID;
  private EntityRegion region;
  private Location savedLocation;
  private boolean isArrowSaveAttackOn = false;
  private boolean isMiniCreeperAttackOn = false;
  private boolean ifAttackActive;

  //This creates a new Creeper game
  public CreeperGame(@NotNull Arena arena, @NotNull Player gameOwner, @NotNull JavaPlugin plugin, @NotNull Location spawnLocation, @NotNull Party gameParty){
    this.arena = arena;
    this.gameOwner = gameOwner;
    this.plugin = plugin;
    this.spawnLocation = spawnLocation;
    this.gameParty = gameParty;
    this.bossBar = new GameBossBar();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    this.logger.logInfo("Start the Countdown for the start");
    ALL_ACTIVE_GAMES.add(this);
    bossName = "TEST";
    new StartGameTimer().start(120*20, gameOwner, spawnLocation, plugin);
  }

  //This methode starts the game
  protected void startGame() {
    new BukkitRunnable() {
      @Override
      public void run() {
        CreeperGame.this.logger.logInfo("Spawn Boss...");
        CreeperGame.this.boss = new CreeperBoss(CreeperGame.this.spawnLocation, CreeperGame.this.bossName, null, null) {
          @Override
          public void die(@NotNull DamageSource damageSource) {
            CreeperGame.this.die(damageSource, CreeperGame.this.boss);
          }
          @Override
          public void explodeCreeper() {
            CreeperGame.this.explodeCreeper(CreeperGame.this.boss);
          }
        };
        bossUUID = CreeperGame.this.boss.getUUID();
        CreeperGame.this.bossBar.create(CreeperGame.this.boss);
      }
    }.runTask(this.plugin);
  }

  //This methode ends the game
  protected void endGame(){
    new BukkitRunnable(){
      @Override
      public void run() {
        CreeperGame.this.logger.logInfo("Remove the game properties of all players and teleport this players back to World world...");
        CreeperGame.this.gameParty.members().forEach(player -> {
          player.teleport(mainSpawnLocation);
          playerStatusMap().replace(player, 0);
        });
        CreeperGame.this.logger.logInfo("Properties removed!");
        CreeperGame.this.logger.logInfo("Remove party...");
        CreeperGame.this.gameParty.removeParty(CreeperGame.this.gameOwner);
        CreeperGame.this.logger.logInfo("Party removed!");
        CreeperGame.this.logger.logInfo("Rebuild the arena...");
        ArenaReBuilder.reBuildArena(CreeperGame.this.arena);
        CreeperGame.this.logger.logInfo("Arena rebuild!");
        ALL_ACTIVE_GAMES.remove(CreeperGame.this);
        message(CreeperGame.this.gameOwner, gray("Game end!"));
      }
    }.runTask(this.plugin);
  }

  //This methode kills this game
  @Override
  public void kill(String reason, boolean isRestart){
    killGame(reason, isRestart);
  }

  //This methode kills this game
  protected void killGame(String reason, boolean isRestart){
    if (!isRestart) {
      new BukkitRunnable(){
        @Override
        public void run() {
          CreeperGame.this.logger.logInfo("Remove the game properties of all players and teleport this players back to World world...");
          CreeperGame.this.gameParty.members().forEach(player -> {
            player.teleport(mainSpawnLocation);
            playerStatusMap().replace(player, 0);
            message(player, red("DAS GAME WURDE BEENDET WEIL: " + reason + "!"));
            CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
          });
          CreeperGame.this.logger.logInfo("Properties removed!");
          CreeperGame.this.logger.logInfo("Remove party...");
          CreeperGame.this.gameParty.removeParty(CreeperGame.this.gameOwner);
          CreeperGame.this.logger.logInfo("Party removed!");
          ALL_ACTIVE_GAMES.remove(CreeperGame.this);
          message(CreeperGame.this.gameOwner, gray("Game end!"));
        }
      }.runTask(this.plugin);
    }
  }

  //This methode executes the tasks if the creeper dies
  public void die(@NotNull DamageSource damageSource, Creeper entity) {
    if (CreeperGame.this.region == null){
      entity.getBukkitEntity().getWorld().strikeLightningEffect(entity.getBukkitEntity().getLocation());
      CreeperGame.this.gameParty.members().forEach(player -> giveAchievement(player, Achievements.BOSSFIGHT_CREEPER_END));
      new Drops(this.gameOwner, entity.getBukkitEntity(), entity.getBukkitEntity().getLocation());
      CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
      CreeperGame.this.logger.logInfo("The Boss is die!");
      CreeperGame.this.logger.logInfo("End Game!");
      CreeperGame.this.logger.logInfo("Start game end timer!");
      CreeperGame.this.gameParty.members().forEach(player -> new EndGameTimer().start(60*20, player, CreeperGame.this.mainSpawnLocation, CreeperGame.this.plugin));
    }else {
      entity.getBukkitEntity().getWorld().strikeLightningEffect(entity.getBukkitEntity().getLocation());
      CreeperGame.this.gameParty.members().forEach(player -> giveAchievement(player, Achievements.BOSSFIGHT_CREEPER_END));
      CreeperGame.this.bossBar.remove(CreeperGame.this.boss);
      CreeperGame.this.region.delete();
      CreeperGame.this.logger.logInfo("The Boss is die!");
      CreeperGame.this.logger.logInfo("End Game!");
      CreeperGame.this.logger.logInfo("Start game end timer!");
      new EndGameTimer().start(61 * 20, null, CreeperGame.this.mainSpawnLocation, CreeperGame.this.plugin);
    }

  }

  //This methode executes the tasks if the creeper exploded
  public void explodeCreeper(@NotNull Creeper entity) {
    if (!entity.level().isClientSide) {
      float f = entity.isPowered() ? 10.0F : 5.0F;//Edit by Elia
      // CraftBukkit start
      ExplosionPrimeEvent event = CraftEventFactory.callExplosionPrimeEvent(entity, CreeperGame.this.explosionRadius * f, true);
      if (!event.isCancelled()) {
        // CraftBukkit end
        //this.dead = true; Removed by elia
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
        CreeperBoss newBoss = new CreeperBoss(l, CreeperGame.this.bossName, newHealth, CreeperGame.this.bossUUID){

          @Override
          public void die(@NotNull DamageSource damageSource) {
            CreeperGame.this.die(damageSource, CreeperGame.this.boss);
          }

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

  //This methode starts the first attack of the creeper
  protected void startAttack1(int seconds, Location location){
    new BukkitRunnable() {
      private int ticksElapsed = 0;
      @Override
      public void run() {
        if (ticksElapsed >= seconds){
          this.cancel();
          ticksElapsed = 0;
          CreeperGame.this.region.delete();
          CreeperGame.this.savedLocation = null;
          CreeperGame.this.ifAttackActive = false;
          CreeperGame.this.isArrowSaveAttackOn = false;
          return;
        }
        createEntityRegionBorder(CreeperGame.this.radius, location, Particle.FLAME);
        ticksElapsed++;
      }
    }.runTaskTimer(SoulBoss.soulBoss().main(), 0, 1);
  }

  //This methode starts the second attack of the creeper
  public void startAttack2(){
    this.gameParty.members().forEach(player -> {
      Location playerLocation = player.getLocation();
      playerLocation.getWorld().strikeLightning(playerLocation);
      String creeperName = player.getName() + "'s Creeperfriend";
      new MiniCreepers(playerLocation, creeperName);
      player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 255, false, false, false));
      Random random = new Random();
      int x = random.nextInt(100, 180);
      new BukkitRunnable(){
        private int y = 0;
        @Override
        public void run() {
          if (y >= x){
            message(player, gray(creeperName + ": Im here..."));
            this.cancel();
          }
          y++;
        }
      }.runTaskTimer(this.plugin, 0, 1);
      player.playSound(playerLocation, Sound.ENTITY_CREEPER_HURT, 0.7f, 0.3f);
    });
  }

  //This Event kills the game if the party owner disconnects
  @EventHandler
  private void onPlayerQuitServer(@NotNull PlayerQuitEvent event){
    Player player = event.getPlayer();
    if (this.gameParty.members().contains(player)) {
      if (player.getPersistentDataContainer().has(new NamespacedKey(BossFightCreatorMain.bossFightCreator().main(), Integer.toString(this.gameParty.id())))) {
        this.killGame("Party Owner disconnected!", false);
        return;
      }else this.gameParty.removePlayer(player);
    }
  }

  //This Event saves a location of the died player
  @EventHandler
  private void onPlayerDie(@NotNull PlayerDeathEvent event){
    Player player = event.getPlayer();
    if (this.gameParty.members().contains(player)) {
      if (player == this.gameOwner) {
        new EndGameTimer().start(61*20, this.gameOwner, this.mainSpawnLocation, this.plugin);
      }else {
        this.gameParty.removePlayer(player);
      }
    }
  }

  //This event loads the attacks
  @EventHandler
  private void onInRegionAttack(@NotNull EntityDamageByEntityEvent event){
    Entity damagedEntity = event.getEntity();
    Entity damager = event.getDamager();
    Location location = damagedEntity.getLocation();
    if (isArrowSaveAttackOn) {
      if (damager instanceof Projectile projectile) {
        if (damagedEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
          if (containsEntity(boss.getBukkitEntity(), savedLocation, this.radius)) {
            event.setCancelled(true);
            return;
          }
        }
      }
    }
    Random random = new Random();
    int randomNumber = random.nextInt(1, 100);
    if (randomNumber <= 10) {
      if (!this.ifAttackActive) {
        if (damagedEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
          if (damager instanceof Projectile projectile) {
            if (!isArrowSaveAttackOn) {
              this.ifAttackActive = true;
              this.isArrowSaveAttackOn = true;
              this.savedLocation = location.clone();
              this.region = new EntityRegion(location, this.radius, this.boss.getBukkitEntity(), true, false);
              this.startAttack1(200, location);
              event.setCancelled(true);
            }
          }
        }
      }
    }
  }

  //This event loads the attacks
  @EventHandler
  private void onBossDamage(@NotNull EntityDamageByEntityEvent event){
    Entity bossEntity = event.getEntity();
    Entity damager = event.getDamager();
    Random random = new Random();
    int randomAttackNumber = random.nextInt(1, 200);
    if (damager instanceof Player pDamager) {
      if (this.gameParty.members().contains(pDamager)) {
        if (bossEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
          if (!this.ifAttackActive) {
            if (!this.isMiniCreeperAttackOn) {
              if (randomAttackNumber < 10) {
                this.ifAttackActive = true;
                this.isMiniCreeperAttackOn = true;
                this.startAttack2();
                int x = 220;
                new BukkitRunnable() {
                  private int y = 0;
                  @Override
                  public void run() {
                    if (y >= x) {
                      CreeperGame.this.ifAttackActive = false;
                      CreeperGame.this.isMiniCreeperAttackOn = false;
                      y = 0;
                      this.cancel();
                    }
                    y++;
                  }
                }.runTaskTimer(this.plugin, 0, 1);
              }
            }
          }
        }
      }
    }else if (damager instanceof Projectile projectile) {
      if (bossEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
        if (!this.ifAttackActive) {
          if (!this.isMiniCreeperAttackOn) {
            if (randomAttackNumber < 30) {
              this.ifAttackActive = true;
              this.isMiniCreeperAttackOn = true;
              this.startAttack2();
              int x = 220;
              new BukkitRunnable() {
                private int y = 0;
                @Override
                public void run() {
                  if (y >= x) {
                    CreeperGame.this.ifAttackActive = false;
                    CreeperGame.this.isMiniCreeperAttackOn = false;
                    y = 0;
                    this.cancel();
                  }
                  y++;
                }
              }.runTaskTimer(this.plugin, 0, 1);
            }
          }
        }
      }
    }
  }

  //This event updates the Bossbar
  @EventHandler
  private void onUpdateBar(@NotNull EntityDamageEvent event) {
    Entity entity = event.getEntity();
    if (entity.getType() == EntityType.CREEPER) {
      if (CreeperGame.this.boss == null)return;
      if (CreeperGame.this.boss.getUUID() == this.bossUUID) {
        this.bossBar.update(CreeperGame.this.boss);
      }
    }
  }

  //This class creates, updates and removes the Bossbar
  private class GameBossBar {

    private final Map<BossEntity, BossBar> bossBossBar = new HashMap<>();

    //This methode creates the Bossbar
    protected void create(BossEntity boss) {
      BossBar bossBar = Bukkit.createBossBar("Test_BossBar_Creeper", BarColor.PINK, BarStyle.SEGMENTED_10);
      CreeperGame.this.gameParty.members().forEach(bossBar::addPlayer);
      double progress = boss.health() / boss.maxHealth();
      bossBar.setProgress(progress);
      bossBar.setVisible(true);
      this.bossBossBar.put(boss, bossBar);
    }

    //This methode updates the Bossbar
    protected void update(BossEntity boss) {
      if (this.bossBossBar.containsKey(boss)) {
        BossBar bossBar = this.bossBossBar.get(boss);
        double progress = boss.health() / boss.maxHealth();
        bossBar.setProgress(progress);
      }
    }

    //This methode removes the Bossbar
    protected void remove(BossEntity boss) {
      if (this.bossBossBar.containsKey(boss)) {
        BossBar bossBar = this.bossBossBar.get(boss);
        bossBar.removeAll();
        this.bossBossBar.remove(boss);
      }
    }

  }

  //This class starts a timer for the start
  private class StartGameTimer extends TimerTasks {

    private void timeMessage(int time){
      CreeperGame.this.gameParty.members().forEach(partyPlayer -> message(partyPlayer, gray("This game starts in ").append(aqua(Integer.toString(time))).append(gray(" seconds!"))));
    }

    @Override
    public void start(int time, Player player,  Location location, @NotNull Plugin plugin) {
      new TimerUtils().countdownAndRun(time, new Runnable() {
        @Override
        public void run() {
          CreeperGame.this.logger.logInfo("Start countdown started!");
          message(CreeperGame.this.gameOwner, gray("The bossfight ").append(darkPurple("starts")).append(gray("!")));
        }
      }, plugin);
      new TimerUtils().countdownInterval(time, new TimerUtils.TimeRunnable() {
        @Override
        public void run(int ticks) {
          if (ticks % 20 == 0) {
            int secounds = ticks / 20;
            if (secounds == 100)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 80)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 60)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 40)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 30)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 20)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 15)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 10) {
              CreeperGame.StartGameTimer.this.timeMessage(secounds);
              new BukkitRunnable() {
                @Override
                public void run() {
                  CreeperGame.this.logger.logInfo("Teleport all Players in the Arena!");
                  message(CreeperGame.this.gameOwner, gray("You and your team will be teleported in the arena!"));
                  CreeperGame.this.gameParty.members().forEach(player -> player.teleport(location));
                }
              }.runTask(plugin);
            }
            if (secounds == 5)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 4)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 3)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 2)CreeperGame.StartGameTimer.this.timeMessage(secounds);
            if (secounds == 1)CreeperGame.StartGameTimer.this.timeMessage(secounds);
          }
        }
      }, new Runnable() {
        @Override
        public void run() {
          CreeperGame.this.logger.logInfo("Game start timer ends!");
          CreeperGame.this.logger.logInfo("Start fight!");
          CreeperGame.this.startGame();
        }
      }, plugin);
    }

  }

  //This class starts a timer for the end
  private class EndGameTimer extends TimerTasks {

    private void timeMessage(int time){
      CreeperGame.this.gameParty.members().forEach(gamePlayer -> message(gamePlayer, gray("The game ends in ").append(aqua(Integer.toString(time))).append(gray(" seconds!"))));
    }

    @Override
    public void start(int time, Player player, Location location, @NotNull Plugin plugin) {
      new TimerUtils().countdownAndRun(time, new Runnable() {
        @Override
        public void run() {
          new BukkitRunnable(){

            @Override
            public void run() {
              if (!CreeperGame.this.boss.isDeadOrDying()) {
                CreeperGame.this.boss.setHealth(0.0F);
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
            if (secounds == 60)CreeperGame.EndGameTimer.this.timeMessage(secounds);
            if (secounds == 50)CreeperGame.EndGameTimer.this.timeMessage(secounds);
            if (secounds == 30)CreeperGame.EndGameTimer.this.timeMessage(secounds);
            if (secounds == 20)CreeperGame.EndGameTimer.this.timeMessage(secounds);
            if (secounds == 10)CreeperGame.EndGameTimer.this.timeMessage(secounds);
            if (secounds == 5)CreeperGame.EndGameTimer.this.timeMessage(secounds);
            if (secounds == 3)CreeperGame.EndGameTimer.this.timeMessage(secounds);
            if (secounds == 2)CreeperGame.EndGameTimer.this.timeMessage(secounds);
            if (secounds == 1)CreeperGame.EndGameTimer.this.timeMessage(secounds);
          }
        }

      }, new Runnable() {
        @Override
        public void run(){CreeperGame.this.endGame();}
      }, plugin);
    }
  }
}
