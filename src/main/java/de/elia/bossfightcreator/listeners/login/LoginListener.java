package de.elia.bossfightcreator.listeners.login;

import de.elia.api.logging.PluginLogger;

import de.elia.bossfightcreator.BossFightCreatorMain;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This event gives the player a game status.
 * State 0: player hasn't a game
 * State 1: player has a game
 * @author Elia
 * @since 2.0.0
 */
public class LoginListener implements Listener {

  @EventHandler
  public void onConnection(@NotNull PlayerLoginEvent event) {
    PluginLogger log = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
    Player player = event.getPlayer();
    Map<Player, Integer> status = BossFightCreatorMain.playerStatusMap();
    if (status.get(player) == null) {
      status.put(player, 0);
      log.logInfo(player.getName() + " has status 0!");
    } else {
      log.logError("This player (" + player + ") has a exist Status!");
      log.logWarning("Status of the Player set to 0!");
      status.replace(player, 0);
    }
  }
}
