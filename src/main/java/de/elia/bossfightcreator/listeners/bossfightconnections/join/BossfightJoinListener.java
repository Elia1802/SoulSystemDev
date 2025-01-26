package de.elia.bossfightcreator.listeners.bossfightconnections.join;

import de.elia.api.game.party.connections.join.BossFightJoinEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.jetbrains.annotations.NotNull;

import static de.elia.api.messages.builder.MessageBuilder.gold;
import static de.elia.api.messages.builder.MessageBuilder.gray;

/**
 * This Event called if a player joins a party
 * @author Elia
 * @since 3.0.0
 */
public class BossfightJoinListener implements Listener {

  /**
   * This Event called if a player joins a party
   * @param event This Event is called if a player join the party
   */
  @EventHandler
  private void onJoin(@NotNull BossFightJoinEvent event){
    //Send the join message
    event.sendJoinMessage(gold(event.getJoinedPlayer().getName()).append(gray(" has joined the game!")));
  }

}
