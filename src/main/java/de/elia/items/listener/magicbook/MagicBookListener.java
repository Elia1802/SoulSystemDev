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

//This event gives the magic book functions.
//Coded by Elia and Zopnote
public class MagicBookListener implements Listener {

  private final Plugin plugin;
  private boolean isAttackOn = false;
  private int maxTicks = 100;

  public MagicBookListener(Plugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  private void onRightClick(@NotNull PlayerInteractEvent event) {
    if (event.getAction().isRightClick() && TheZepserAPI.Item.hasKey(event.getItem(), TheZepserAPI.Item.createKey(Complex.MAGIC_BOOK), this.plugin)) {
      event.setCancelled(true);
      if (!isAttackOn) {
        isAttackOn = true;
        Spells.FIRE_ATTACK(event.getPlayer(), true, this.plugin);
        new BukkitRunnable() {

          private int x = 0;

          @Override
          public void run() {
            if (maxTicks >= x) {
              this.cancel();
              MagicBookListener.this.isAttackOn = false;
            }
            x++;
          }
        }.runTaskTimer(this.plugin, 0, 1);
      }else {
        event.setCancelled(true);
        message(event.getPlayer(), gray("The item is currently loading"));
      }
    } else if (event.getAction().isLeftClick() && TheZepserAPI.Item.hasKey(event.getItem(), TheZepserAPI.Item.createKey(Complex.MAGIC_BOOK), this.plugin)) {
      if (!isAttackOn) {
        isAttackOn = true;
        Spells.GRAVITATION_ATTACK(event.getPlayer(), true, this.plugin);
        new BukkitRunnable() {

          private int x = 0;

          @Override
          public void run() {
            if (maxTicks >= x) {
              this.cancel();
              MagicBookListener.this.isAttackOn = false;
            }
            x++;
          }
        }.runTaskTimer(this.plugin, 0, 1);
      }else {
        event.setCancelled(true);
        message(event.getPlayer(), gray("The item is currently loading"));
      }
    }
  }
}
