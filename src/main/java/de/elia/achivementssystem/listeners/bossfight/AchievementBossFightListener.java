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


//This Listener start if the Zombieboss hit of a Player. If the Player doesn't have the Achievement, Bossfight gives this event this Achievement.
public class AchievementBossFightListener implements Listener {

  @EventHandler
  public void onBossDamaged(@NotNull EntityDamageByEntityEvent event) throws SQLException {
    Entity damagedEntity = event.getDamager();
    Entity damagerEntity = event.getEntity();
    if (damagerEntity instanceof Player player) {
      if (damagedEntity.getType() == EntityType.ZOMBIE && damagedEntity.getPersistentDataContainer().has(NameSpacedKeys.ZOMBIE_KEY.key())) {
        giveAchievement(player, Achievements.BOSSFIGHT);
      }else if (damagedEntity.getType() == EntityType.CREEPER && damagedEntity.getPersistentDataContainer().has(NameSpacedKeys.CREEPER_KEY.key())) {
        giveAchievement(player, Achievements.BOSSFIGHT);
      }
    }
  }
}
