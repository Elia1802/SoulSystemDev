package de.elia.bossfightcreator.listeners.bossfightconnections.join;

import de.elia.api.events.bossfight.connections.join.BossFightJoinEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.jetbrains.annotations.NotNull;

import static de.elia.api.messages.builder.MessageBuilder.gold;
import static de.elia.api.messages.builder.MessageBuilder.gray;

//This Event called if a player joins a party
public class BossfightJoinListener implements Listener {

  @EventHandler
  private void onJoin(@NotNull BossFightJoinEvent event){
    event.sendJoinMessage(gold(event.getJoinedPlayer().getName()).append(gray(" has joined the game!")));
  }

}
