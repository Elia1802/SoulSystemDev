package de.elia.items.listener.magicbook;

import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.TheZepserAPI;
import de.elia.api.thezepserapi.spells.Spells;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.jetbrains.annotations.NotNull;

import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;

/**
 * This event gives the magic book functions.
 * @author Elia,Zopnote
 * @since 3.0.0
 */
public class MagicBookListener implements Listener {

  private final Plugin plugin;
  private boolean isAttackOn = false;
  //Set the attack length in ticks (20 ticks = 1 Secound)
  private int maxTicks = 100;

  /**
   * This constructer set the plugin variable for the class
   * @param plugin Requires the main instance of the plugin
   */
  public MagicBookListener(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Start the magic of the magic book
   * @param event This event is called if the player interact with an item.
   */
  @EventHandler
  private void onRightClick(@NotNull PlayerInteractEvent event) {
    //Checks if the player has right-clicked and if it is the correct item
    if (event.getAction().isRightClick() && TheZepserAPI.Item.hasKey(event.getItem(), TheZepserAPI.Item.createKey(Complex.MAGIC_BOOK), this.plugin)) {
      //canceled this event
      event.setCancelled(true);
      //Checks if the attack used
      if (!isAttackOn) {
        //set the attack to on
        isAttackOn = true;
        //Start the spell fire attack
        Spells.FIRE_ATTACK(event.getPlayer(), true, this.plugin);
        //Create a new Runnable
        new BukkitRunnable() {
          private int x = 0;
          @Override
          public void run() {
            //Checks if x reached maxTicks
            if (maxTicks >= x) {
              //Cancel this runnable
              this.cancel();
              //Set that the attack is not used
              MagicBookListener.this.isAttackOn = false;
            }
            //Count x +1
            x++;
          }
        }.runTaskTimer(this.plugin, 0, 1);
      }else {
        //Cancel this event
        event.setCancelled(true);
        //Send player a message
        message(event.getPlayer(), gray("The item is currently loading"));
      }
    //Checks if the player has left-clicked and if it is the correct item
    }else if (event.getAction().isLeftClick() && TheZepserAPI.Item.hasKey(event.getItem(), TheZepserAPI.Item.createKey(Complex.MAGIC_BOOK), this.plugin)) {
      //canceled this event
      event.setCancelled(true);
      //Checks if the attack used
      if (!isAttackOn) {
        //set the attack to on
        isAttackOn = true;
        //Start the spell gravitation attack
        Spells.GRAVITATION_ATTACK(event.getPlayer(), true, this.plugin);
        //Create a new Runnable
        new BukkitRunnable() {
          private int x = 0;
          @Override
          public void run() {
            //Checks if x reached maxTicks
            if (maxTicks >= x) {
              //Cancel this runnable
              this.cancel();
              //Set that the attack is not used
              MagicBookListener.this.isAttackOn = false;
            }
            //Count x +1
            x++;
          }
        }.runTaskTimer(this.plugin, 0, 1);
      }else {
        //Cancel this event
        event.setCancelled(true);
        //Send player a message
        message(event.getPlayer(), gray("The item is currently loading"));
      }
    }
  }
}
