package de.elia.soulboss.entitys.zombie.equipment.armors.boots;

import de.elia.soulboss.random.RandomUtils;

import net.minecraft.world.entity.monster.Zombie;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//Create the Boots for the Zombie
public class Boots {

  public void boots(Zombie zombie) {
    ItemStack diamond = new ItemStack(Material.DIAMOND_BOOTS);
    ItemMeta diamondMeta = diamond.getItemMeta();
    diamondMeta.addEnchant(Enchantment.FIRE_PROTECTION, 5, true);
    diamondMeta.addEnchant(Enchantment.FROST_WALKER, 5, true);
    diamondMeta.addEnchant(Enchantment.FEATHER_FALLING, 5, true);
    diamondMeta.addEnchant(Enchantment.THORNS, 5, true);
    diamondMeta.setUnbreakable(true);
    diamond.setItemMeta(diamondMeta);
    ItemStack netherite = new ItemStack(Material.NETHERITE_BOOTS);
    ItemMeta netheriteMeta = netherite.getItemMeta();
    netheriteMeta.addEnchant(Enchantment.FIRE_PROTECTION, 5, true);
    netheriteMeta.addEnchant(Enchantment.FROST_WALKER, 5, true);
    netheriteMeta.addEnchant(Enchantment.FEATHER_FALLING, 5, true);
    netheriteMeta.addEnchant(Enchantment.THORNS, 5, true);
    netheriteMeta.setUnbreakable(true);
    netherite.setItemMeta(netheriteMeta);
    new RandomUtils().randomItem(zombie, 0.5f, diamond, netherite);
  }
}
