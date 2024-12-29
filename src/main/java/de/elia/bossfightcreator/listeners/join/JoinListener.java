package de.elia.bossfightcreator.listeners.join;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.jetbrains.annotations.NotNull;

/**
 * This event teleports the player to the World "world" if the player is in the World "world_bossfight"
 * @author Elia
 * @since 2.0.0
 */
public class JoinListener implements Listener {

  /**
   * This event teleports the player to the World "world" if the player is in the World "world_bossfight"
   * @param event This event is called if a player joined the server
   */
  @EventHandler
  private void onJoin(@NotNull PlayerJoinEvent event) {
    //Get the player of the event
    Player eventPlayer = event.getPlayer();
    //Checks if the world "world_bossfight" contains the player
    if (Bukkit.getWorld("world_bossfight").getEntities().contains(eventPlayer)) {
      //Teleport the player to the normal world
      eventPlayer.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }
  }

}
