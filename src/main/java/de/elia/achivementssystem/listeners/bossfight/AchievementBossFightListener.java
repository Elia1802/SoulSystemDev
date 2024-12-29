package de.elia.achivementssystem.listeners.bossfight;

import de.elia.api.achievements.Achievements;
import de.elia.systemclasses.keys.NameSpacedKeys;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static de.elia.achivementssystem.achievement.Achievement.giveAchievement;

/**
 * This Listener start if the Zombieboss hit of a Player. If the Player doesn't have the Achievement, Bossfight gives this event this Achievement.
 * @author Elia
 * @since 1.0.0
 */
public class AchievementBossFightListener implements Listener {

  /**
   * Checks if a {@link Player} hit a {@link de.elia.soulboss.entitys.BossEntity} to give him the achievement Bossfight.
   * @param event Give the damaged entity and the damager entity
   */
  @EventHandler
  public void onBossDamaged(@NotNull EntityDamageByEntityEvent event) {
    Entity damagedEntity = event.getDamager(); //Get of the entity who was damaged
    Entity damagerEntity = event.getEntity(); //Gets the entity who has damaged the other entity
    if (damagerEntity instanceof Player player) { //Checks if the damagerEntity is a player
      if (damagedEntity.getType() == EntityType.ZOMBIE && damagedEntity.getPersistentDataContainer().has(NameSpacedKeys.ZOMBIE_KEY.key())) { //Checks if the damaged entity the zombie boss
        giveAchievement(player, Achievements.BOSSFIGHT); //Give the player the achievement
      }else if (damagedEntity.getType() == EntityType.CREEPER && damagedEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) { //Checks if the damaged entity the creeper boss
        giveAchievement(player, Achievements.BOSSFIGHT); //Give the player the achievement
      }
    }
  }
}
