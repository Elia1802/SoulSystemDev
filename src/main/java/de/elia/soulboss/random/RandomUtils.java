package de.elia.soulboss.random;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

//This Random generator equipped a random item to the Zombie boss
public class RandomUtils {
  public void randomItem(Zombie zombie, float chance, org.bukkit.inventory.ItemStack item1, org.bukkit.inventory.ItemStack item2) {
    Random random = new Random();
    float x = random.nextFloat();
    if (x < chance) {
      zombie.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.fromBukkitCopy(item1));
    } else {
      zombie.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.fromBukkitCopy(item2));
    }
  }
}
