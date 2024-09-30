package de.elia.bossfightcreator.listeners.join;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.jetbrains.annotations.NotNull;

//This event teleports the player to the World "world" if the player is in the World "world_bossfight"
public class JoinListener implements Listener {

  @EventHandler
  private void onJoin(@NotNull PlayerJoinEvent event) {
    Player eventPlayer = event.getPlayer();
    if (Bukkit.getWorld("world_bossfight").getEntities().contains(eventPlayer)) {
      eventPlayer.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }
  }

}
