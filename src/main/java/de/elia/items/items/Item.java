package de.elia.items.items;

import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.components.ComplexItem;

import de.elia.items.items.creeperspawnegg.Creeper_Spawn_Egg;
import de.elia.items.items.magicbook.Magic_Book;
import de.elia.items.items.magicstick.Magic_Stick;
import de.elia.items.items.zombiespawnegg.Zombie_Spawn_Egg;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is a part of the TheZepserAPI of Zopnote. With this class you can get, drop and give an Item
 * @author Elia
 * @since 2.0.0
 */
public class Item {

  /**
   * This methode register all items
   * @param plugin Requires the main instance of the plugin
   */
  public static void registerAll(@NotNull Plugin plugin) {
    new Zombie_Spawn_Egg(plugin);
    new Creeper_Spawn_Egg(plugin);
    new Magic_Book(plugin);
    new Magic_Stick(plugin);
  }

  /**
   * Gets a specified item.
   * @param item Requires the
   * @return The item if it found
   * @throws CloneNotSupportedException The object could not or should not be cloned.
   */
  @Nullable
  public static ComplexItem get(Complex item) throws CloneNotSupportedException {
    //Checks if the item key is saved
    if (ComplexItem.SAVED.containsKey(item)) {
      //Returns the item
      return (ComplexItem)(ComplexItem.SAVED.get(item)).clone();
    }
    return null;
  }

  /**
   * Give the Player the Item.
   * @param player The player to give him the item
   * @param item The item
   * @param amount the ammount of the itemm
   */
  public static void give(@NotNull Player player, @NotNull Complex item, int amount) {
    //Checks if the amount smaller or eqauls as 0
    if (amount <= 0) {
      //TODO: ERRROR MESSAGE
      return;
    }
    //Checks if the item key is saved
    if (ComplexItem.SAVED.containsKey(item)) {
      //Give the player item
      ComplexItem.SAVED.get(item).setAmount(Math.min(amount, 64)).giveItem(player);
    }
  }

  /**
   * Drop the item a specified location in the world.
   * @param location The location where the item will dropped
   * @param item the item
   * @param amount the amount
   */
  public static void drop(@NotNull Location location, @NotNull Complex item, int amount) {
    //Checks if the item key is saved
    if (ComplexItem.SAVED.containsKey(item)) {
      //Drop the item on the location
      ComplexItem.SAVED.get(item).setAmount(amount).drop(location);
    }
  }
}
