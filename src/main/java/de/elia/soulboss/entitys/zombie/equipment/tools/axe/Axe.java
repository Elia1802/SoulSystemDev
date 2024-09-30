package de.elia.soulboss.entitys.zombie.equipment.tools.axe;

import de.elia.soulboss.random.RandomUtils;

import net.minecraft.world.entity.monster.Zombie;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//Create the axe for the Zombie
public class Axe {
  public void axe(Zombie zombie) {
    ItemStack diamond = new ItemStack(Material.DIAMOND_AXE);
    ItemMeta diamondMeta = diamond.getItemMeta();
    diamondMeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
    diamondMeta.setUnbreakable(true);
    diamond.setItemMeta(diamondMeta);
    ItemStack netherite = new ItemStack(Material.NETHERITE_AXE);
    ItemMeta netheriteMeta = netherite.getItemMeta();
    netheriteMeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
    netheriteMeta.setUnbreakable(true);
    netherite.setItemMeta(netheriteMeta);
    new RandomUtils().randomItem(zombie, 0.5f, diamond, netherite);
  }
}
