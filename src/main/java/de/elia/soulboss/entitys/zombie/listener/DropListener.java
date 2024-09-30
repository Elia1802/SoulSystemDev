package de.elia.soulboss.entitys.zombie.listener;

import de.elia.soulboss.entitys.zombie.drop.DropUtils;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import org.jetbrains.annotations.NotNull;

//This event drops the Drops of the zombie boss if he died
public class DropListener implements Listener {
  @EventHandler
  public void onDeath(@NotNull EntityDeathEvent event) {
    if (event.getEntityType() == EntityType.ZOMBIE && event.getEntity().getPersistentDataContainer().has(NameSpacedKeys.ZOMBIE_KEY.key())) {
      event.getDrops().clear();
      DropUtils.drop(event.getEntity().getLocation());
    }
  }
}
