package de.elia.soulboss.entitys.zombie.equipment.armors.chestplate;

import de.elia.soulboss.random.RandomUtils;

import net.minecraft.world.entity.monster.Zombie;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//Create the chestplate for the Zombie
public class Chestplate {
  public void chestplate(Zombie zombie) {
    ItemStack diamond = new ItemStack(Material.DIAMOND_CHESTPLATE);
    ItemMeta diamondMeta = diamond.getItemMeta();
    diamondMeta.addEnchant(Enchantment.FIRE_PROTECTION, 5, true);
    diamondMeta.addEnchant(Enchantment.PROTECTION, 5, true);
    diamondMeta.addEnchant(Enchantment.THORNS, 5, true);
    diamondMeta.setUnbreakable(true);
    diamond.setItemMeta(diamondMeta);
    ItemStack netherite = new ItemStack(Material.NETHERITE_CHESTPLATE);
    ItemMeta netheriteMeta = netherite.getItemMeta();
    netheriteMeta.addEnchant(Enchantment.FIRE_PROTECTION, 5, true);
    netheriteMeta.addEnchant(Enchantment.PROTECTION, 5, true);
    netheriteMeta.addEnchant(Enchantment.THORNS, 5, true);
    netheriteMeta.setUnbreakable(true);
    netherite.setItemMeta(netheriteMeta);
    new RandomUtils().randomItem(zombie, 0.5f, diamond, netherite);
  }
}
