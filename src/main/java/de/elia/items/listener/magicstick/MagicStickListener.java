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

//This class creates the attack for the magic stick
public class MagicStickListener implements Listener {

  private final Plugin plugin;
  private boolean isAttackOn = false;
  private int maxTicks = 100;

  public MagicStickListener(@NotNull Plugin plugin){
    this.plugin = plugin;
  }

  @EventHandler
  private void onInteract(@NotNull PlayerInteractEvent event){
    Player player = event.getPlayer();
    if (!this.isAttackOn) {
      if (event.getAction().isRightClick() && Item.hasKey(event.getItem(), Item.createKey(Complex.MAGIC_STICK), this.plugin)) {
        event.setCancelled(true);
        this.isAttackOn = true;
        Spells.EXPLOSION_ATTACK(player, this.plugin);
        new BukkitRunnable() {

          private int ticks = 0;

          @Override
          public void run() {
            if (ticks >= maxTicks) {
              this.cancel();
              MagicStickListener.this.isAttackOn = false;
            }
            ticks++;
          }
        }.runTaskTimer(this.plugin, 0L, 1L);
      }
    }else {
      event.setCancelled(true);
      message(player, gray("The item is currently loading!"));
    }
  }

}
