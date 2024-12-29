package de.elia.items.items.zombiespawnegg;

import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.TheZepserAPI;
import de.elia.api.thezepserapi.components.ComplexItem;

import de.elia.api.thezepserapi.enums.Type;
import de.elia.items.ItemMain;

import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

/**
 * This class creates the zombie boss spawn egg
 * @author Elia
 * @since 2.0.0
 */
public class Zombie_Spawn_Egg {

  //Create a variable of the item
  private static ComplexItem ZSE;

  /**
   * This creates the item
   * @param plugin Requires the plugin instance of the main
   */
  public Zombie_Spawn_Egg(Plugin plugin) {
    //Checks if the item variable is null
    if (ZSE == null) {
      //Create a list for the lore
      ArrayList<Component> list = new ArrayList<>();
      //Fill the list with a lore
      list.add(ItemMain.MINI_MESSAGE.deserialize("<gray>With this egg you can spawn the</gray> <dark_green>Zombie Boss</dark_green>"));
      //Create a name
      Component name = ItemMain.MINI_MESSAGE.deserialize("<dark_green>Zombie Boss</dark_green> <gray>Spawn Egg</gray>");
      //Create the item
      ZSE = TheZepserAPI.Item.create(Material.ZOMBIE_SPAWN_EGG, name, list)
        //Set the custom model data
              .setCustomModelData(1)
        //Set the amount
              .setAmount(1)
        //Set the Key of the item
              .setKey(Complex.ZOMBIE_SPAWN_EGG, plugin)
        //Set the type of the item
              .setType(Type.SPAWN_EGG)
        //Save the item
              .save();
    }
  }
}
