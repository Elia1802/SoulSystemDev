package de.elia.soulboss.entitys.zombie.listener;

import de.elia.soulboss.SoulBoss;
import de.elia.soulboss.entitys.zombie.magic.attack.Attack;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

//This methode loads the attacks for the zombie boss
public class AttackListener implements Listener {

  private boolean isAttackOn = false;
  private int maxticks = 200;

  @EventHandler
  public void onAttack(@NotNull EntityDamageEvent event) {
    boolean isAttackOn = false;
    if (event.isCancelled()) {
      return;
    }
    Random random = new Random();
    int fire = random.nextInt(100);
    int strikeLightning = random.nextInt(100);
    int teleport = random.nextInt(100);
    if (event.getEntity().getType() == EntityType.ZOMBIE && event.getEntity().getPersistentDataContainer().has(NameSpacedKeys.ZOMBIE_KEY.key())) {
      if (!isAttackOn) {
        if (fire <= 10) {
          isAttackOn = true;
          event.setCancelled(true);
          Attack.attackFire(event.getEntity());
          event.setCancelled(false);
          new BukkitRunnable()  {

            private int x = 0;

            @Override
            public void run() {
              if (x >= maxticks) {
                this.cancel();
                AttackListener.this.isAttackOn = false;
              }
              x++;
            }
          }.runTaskTimer(SoulBoss.soulBoss().main(), 0, 1);
          return;
        }
        if (strikeLightning <= 10) {
          isAttackOn = true;
          event.setCancelled(true);
          Attack.attackStrikeLightning(event.getEntity());
          event.setCancelled(false);
          new BukkitRunnable()  {

            private int x = 0;

            @Override
            public void run() {
              if (x >= maxticks) {
                this.cancel();
                AttackListener.this.isAttackOn = false;
              }
              x++;
            }
          }.runTaskTimer(SoulBoss.soulBoss().main(), 0, 1);
          return;
        }
        if (teleport <= 10) {
          isAttackOn = true;
          event.setCancelled(true);
          Attack.attackTeleport(event.getEntity());
          event.setCancelled(false);
          new BukkitRunnable()  {

            private int x = 0;

            @Override
            public void run() {
              if (x >= maxticks) {
                this.cancel();
                AttackListener.this.isAttackOn = false;
              }
            }
          }.runTaskTimer(SoulBoss.soulBoss().main(), 0, 1);
          return;
        }
      }
    }
  }
}
