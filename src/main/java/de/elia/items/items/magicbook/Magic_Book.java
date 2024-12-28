package de.elia.items.items.magicbook;

import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.TheZepserAPI;
import de.elia.api.thezepserapi.components.ComplexItem;

import de.elia.api.thezepserapi.enums.Type;
import de.elia.items.ItemMain;

import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

/**
 * This class creates the item magic book
 */
public class Magic_Book {

  //Create a variable of the item
  private static ComplexItem MB;

  /**
   * This creates the item
   * @param plugin Requires the plugin instance of the main
   */
  public Magic_Book(Plugin plugin) {
    //Checks if the item variable is null
    if (MB == null) {
      //Create a list for the lore
      ArrayList<Component> list = new ArrayList<>();
      //Fill the list with a lore
      list.add(ItemMain.MINI_MESSAGE.deserialize("<gray>You can used this book</gray>"));
      list.add(ItemMain.MINI_MESSAGE.deserialize("<gray>to cast spells.</gray>"));
      //Create a name
      Component name = ItemMain.MINI_MESSAGE.deserialize("<obfuscated>#</obfuscated> <dark_purple>Magic Book</dark_purple> <obfuscated>#</obfuscated>");
      //Create the item
      MB = TheZepserAPI.Item.create(Material.BOOK, name, list)
        //Set the custom model data
        .setCustomModelData(1)
        //Set the Key of the item
        .setKey(Complex.MAGIC_BOOK, plugin)
        //Set the amount
        .setAmount(1)
        //Add attributes to the item
        //TODO: TEST THIS ATTRIBUTES (old attributes was GENERIC_MAX_HEALTH)
        .addAttribute(Attribute.MAX_HEALTH, 20.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND, ItemMain.itemMain().main())
        .addAttribute(Attribute.MAX_HEALTH, 20.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND, ItemMain.itemMain().main())
        //Add flags to the item
        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
        .addFlag(ItemFlag.HIDE_ENCHANTS)
        //Add enchantments to the item
        .addEnchantment(Enchantment.FLAME, 1)
        //Set the type of the item
        .setType(Type.MAGIC)
        //Save the item
        .save();
    }
  }
}

