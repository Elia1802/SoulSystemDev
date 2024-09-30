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

//This class creates the creeper boss spawn egg
public class Creeper_Spawn_Egg {

  private static ComplexItem CSE;

  public Creeper_Spawn_Egg(@NotNull Plugin plugin) {
    if (CSE == null) {
      ArrayList<Component> lore = new ArrayList<>();
      lore.add(MINI_MESSAGE.deserialize("<gray>With this egg you can spawn the </gray><green>Creeper Boss</green>"));
      Component name = MINI_MESSAGE.deserialize("<green>Creeper Boss</green> <gray>Spawn Egg</gray>");
      CSE = TheZepserAPI.Item.create(Material.CREEPER_SPAWN_EGG, name, lore)
              .setCustomModelData(1)
              .setAmount(1)
              .setKey(Complex.CREEPER_SPAWN_EGG, plugin)
              .setType(Type.SPAWN_EGG)
              .save();
    }
  }

}
