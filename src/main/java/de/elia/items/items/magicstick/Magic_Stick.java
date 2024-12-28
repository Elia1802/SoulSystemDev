package de.elia.items.items.magicstick;

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
 * This class creates the Magic stick
 */
public class Magic_Stick {

  //Create a variable of the item
  private static ComplexItem MS;

  /**
   * This creates the item
   * @param plugin Requires the plugin instance of the main
   */
  public Magic_Stick(@NotNull Plugin plugin){
    //Checks if the item variable is null
    if (MS == null) {
      //Create a list for the lore
      ArrayList<Component> lore = new ArrayList<>();
      //Fill the list with a lore
      lore.add(MINI_MESSAGE.deserialize("<gray>With this stick you can create explosions and light</gray>"));
      //Create a name
      Component name = MINI_MESSAGE.deserialize("<obfuscated>#</obfuscated> <dark_purple>Magic Book</dark_purple> <obfuscated>#</obfuscated>");
      //Create the item
      MS = TheZepserAPI.Item.create(Material.WOODEN_HOE, name, lore)
              //Set the custom model data
              .setCustomModelData(2)
              //Set the Key of the item
              .setKey(Complex.MAGIC_STICK, plugin)
              //Set the amount
              .setAmount(1)
              //Set the type of the item
              .setType(Type.MAGIC)
              //Save the item
              .save();
    }
  }

}
