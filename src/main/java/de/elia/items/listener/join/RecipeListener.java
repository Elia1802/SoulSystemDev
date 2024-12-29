package de.elia.items.listener.join;

import de.elia.items.ItemMain;

import de.elia.systemclasses.annotations.ToBeDeleted;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.white;

/**
 * This event gives a notice to the recipes for the spawn egg
 */
@Deprecated //NEW IDEA FOR THIS!!! SEE: SKRIPT FÜR SPAWN EGGS BEANSCHAFFUNG.txt in BETA_leveling
@ToBeDeleted(removalVersion = "5.0.0", reason = "New Idea for this")
public class RecipeListener implements Listener {

  /**
   * This event gives a notice to the recipes for the spawn egg
   * @param event This event is called if join a player
   */
  @EventHandler
  private void onJoin(@NotNull PlayerJoinEvent event) {
    //Get the player of the event
    Player player = event.getPlayer();
    //create a random variable
    Random random = new Random();
    //Generate a random int
    int chance = random.nextInt(25);
    //checks if chance = 1
    if (chance == 1) {
      //Generate a random float
      float x = random.nextFloat();
      //Checks if x smaller than 0.5
      if (x < 0.5) {
        //Send the Player a message
        message(player, white("[Unknown voice] Click this..."));
        message(player, white("[Unknown voice] https://www.youtube.com/watch?v=Gow5H1NG0zA"));
      //If x higher than 0.5
      }else {
        //Send the Player a message
        message(player, white("[Unknown voice] Click this..."));
        message(player, white("[Unknown voice] https://www.youtube.com/shorts/YI3-xM45oh8"));
      }
    }
    ItemMain.itemMain().itemLogger().logWarning("The chance for God is " + chance);
  }
}
