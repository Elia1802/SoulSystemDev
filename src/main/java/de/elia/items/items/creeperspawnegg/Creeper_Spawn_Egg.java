package de.elia.items.items.creeperspawnegg;

import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.TheZepserAPI;
import de.elia.api.thezepserapi.components.ComplexItem;

import de.elia.api.thezepserapi.enums.Type;
import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static de.elia.items.ItemMain.MINI_MESSAGE;

/**
 * This class creates the creeper boss spawn egg
 */
public class Creeper_Spawn_Egg {

  //Create a variable of the item
  private static ComplexItem CSE;

  /**
   * This creates the item
   * @param plugin Requires the plugin instance of the main
   */
  public Creeper_Spawn_Egg(@NotNull Plugin plugin) {
    //Checks if the item variable is null
    if (CSE == null) {
      //Create a list for the lore
      ArrayList<Component> lore = new ArrayList<>();
      //Fill the list with a lore
      lore.add(MINI_MESSAGE.deserialize("<gray>With this egg you can spawn the </gray><green>Creeper Boss</green>"));
      //Create a name
      Component name = MINI_MESSAGE.deserialize("<green>Creeper Boss</green> <gray>Spawn Egg</gray>");
      //Create the item
      CSE = TheZepserAPI.Item.create(Material.CREEPER_SPAWN_EGG, name, lore)
              //Set the custom model data
              .setCustomModelData(1)
              //Set the amount
              .setAmount(1)
              //Set the Key of the item
              .setKey(Complex.CREEPER_SPAWN_EGG, plugin)
              //Set the type of the item
              .setType(Type.SPAWN_EGG)
              //Save the item
              .save();
    }
  }

}
