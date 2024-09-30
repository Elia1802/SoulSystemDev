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

//This class is a part of the TheZepserAPI of Zopnote. With this class you can get, drop and give an Item
//Coded by
public class Item {

  public static void registerAll(@NotNull Plugin plugin) {
    new Zombie_Spawn_Egg(plugin);
    new Creeper_Spawn_Egg(plugin);
    new Magic_Book(plugin);
    new Magic_Stick(plugin);
  }

  //Gets a specified item.
  @Nullable
  public static ComplexItem get(Complex item) throws CloneNotSupportedException {
    if (ComplexItem.SAVED.containsKey(item)) {
      return (ComplexItem)(ComplexItem.SAVED.get(item)).clone();
    }
    return null;
  }

  //Give the Player the Item.
  public static void give(@NotNull Player player, @NotNull Complex item, int amount) {
    if (amount <= 0) {
      return;
    }
    if (ComplexItem.SAVED.containsKey(item)) {
      ComplexItem.SAVED.get(item).setAmount(Math.min(amount, 64)).giveItem(player);
    }
  }

  //Drop the item a specified location in the world.
  public static void drop(@NotNull Location location, @NotNull Complex item, int amount) {
    if (ComplexItem.SAVED.containsKey(item)) {
      ComplexItem.SAVED.get(item).setAmount(amount).drop(location);
    }
  }
}
