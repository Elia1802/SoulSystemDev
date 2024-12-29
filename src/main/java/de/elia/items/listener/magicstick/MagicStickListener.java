package de.elia.items.listener.magicstick;

import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.TheZepserAPI.Item;
import de.elia.api.thezepserapi.spells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.jetbrains.annotations.NotNull;

import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;

/**
 * This class creates the attack for the magic stick
 * @author Elia
 * @since 2.0.0
 */
public class MagicStickListener implements Listener {

  private final Plugin plugin;
  private boolean isAttackOn = false;
  //Set the attack length in ticks (20 ticks = 1 Secound)
  private int maxTicks = 100;


  /**
   * This constructer set the plugin variable for the class
   * @param plugin Requires the main instance of the plugin
   */
  public MagicStickListener(@NotNull Plugin plugin){
    this.plugin = plugin;
  }

  /**
   * Start the magic of the magic stick
   * @param event This event is called if the player interact with an item.
   */
  @EventHandler
  private void onInteract(@NotNull PlayerInteractEvent event){
    //Get the player of the event
    Player player = event.getPlayer();
    //Checks if the attack used
    if (!this.isAttackOn) {
      //Checks if the player has right-clicked and if it is the correct item
      if (event.getAction().isRightClick() && Item.hasKey(event.getItem(), Item.createKey(Complex.MAGIC_STICK), this.plugin)) {
        //canceled this event
        event.setCancelled(true);
        //set the attack to on
        this.isAttackOn = true;
        //Start the spell explosion attack
        Spells.EXPLOSION_ATTACK(player, this.plugin);
        //Create a new Runnable
        new BukkitRunnable() {
          private int ticks = 0;
          @Override
          public void run() {
            //Checks if ticks reached maxTicks
            if (ticks >= maxTicks) {
              //Cancel this runnable
              this.cancel();
              //Set that the attack is not used
              MagicStickListener.this.isAttackOn = false;
            }
            //Count x +1
            ticks++;
          }
        }.runTaskTimer(this.plugin, 0L, 1L);
      }
    }else {
      //Cancel this event
      event.setCancelled(true);
      //Send player a message
      message(player, gray("The item is currently loading!"));
    }
  }

}
