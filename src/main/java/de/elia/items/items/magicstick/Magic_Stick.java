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

//This class creates the Magic stick
public class Magic_Stick {

  private static ComplexItem MS;

  public Magic_Stick(@NotNull Plugin plugin){
    if (MS == null) {
      ArrayList<Component> lore = new ArrayList<>();
      lore.add(MINI_MESSAGE.deserialize("<gray>With this stick you can create explosions and light</gray>"));
      Component name = MINI_MESSAGE.deserialize("<obfuscated>#</obfuscated> <dark_purple>Magic Book</dark_purple> <obfuscated>#</obfuscated>");
      MS = TheZepserAPI.Item.create(Material.WOODEN_HOE, name, lore)
              .setCustomModelData(2)
              .setKey(Complex.MAGIC_STICK, plugin)
              .setAmount(1)
              .setType(Type.MAGIC)
              .save();
    }
  }

}
