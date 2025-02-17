package de.elia.soulboss.entitys.zombie.equipment.tools.sword;

import de.elia.soulboss.random.RandomUtils;

import net.minecraft.world.entity.monster.Zombie;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//Create the sword for the Zombie
public class Sword {
  public void sword(Zombie zombie) {
    ItemStack diamond = new ItemStack(Material.DIAMOND_SWORD);
    ItemMeta diamondMeta = diamond.getItemMeta();
    diamondMeta.addEnchant(Enchantment.FIRE_ASPECT, 5, true);
    diamondMeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
    diamondMeta.setUnbreakable(true);
    diamond.setItemMeta(diamondMeta);
    ItemStack netherite = new ItemStack(Material.NETHERITE_SWORD);
    ItemMeta netheriteMeta = netherite.getItemMeta();
    netheriteMeta.addEnchant(Enchantment.FIRE_ASPECT, 5, true);
    netheriteMeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
    netheriteMeta.setUnbreakable(true);
    netherite.setItemMeta(netheriteMeta);
    new RandomUtils().randomItem(zombie, 0.5f, diamond, netherite);
  }
}
