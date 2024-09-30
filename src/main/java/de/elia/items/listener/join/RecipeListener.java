package de.elia.items.listener.join;

import de.elia.items.ItemMain;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.white;

//This event gives a notice to the recipes for the spawn egg
public class RecipeListener implements Listener {

  @EventHandler
  private void onJoin(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    Random random = new Random();
    int chance = random.nextInt(25);
    if (chance == 1) {
      float x = random.nextFloat();
      if (x < 0.5) {
        message(player, white("[Unknown voice] Click this..."));
        message(player, white("[Unknown voice] https://www.youtube.com/watch?v=Gow5H1NG0zA"));
      }else {
        message(player, white("[Unknown voice] Click this..."));
        message(player, white("[Unknown voice] https://www.youtube.com/shorts/YI3-xM45oh8"));
      }
    }
    ItemMain.itemMain().itemLogger().logWarning("The chance for God is " + chance);
  }
}
