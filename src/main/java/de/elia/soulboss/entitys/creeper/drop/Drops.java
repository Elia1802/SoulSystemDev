package de.elia.soulboss.entitys.creeper.drop;

import de.elia.api.thezepserapi.Complex;

import de.elia.bossfightcreator.BossFightCreatorMain;
import de.elia.items.items.Item;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;

//Create Drops if the creeper dies
public class Drops {

  public Drops(@NotNull Player gameOwner, @NotNull Entity creeperBoss, @NotNull Location dropLocation){
    Random random = new Random();
    int itemNumber = random.nextInt(0, 7);
    this.drop(gameOwner, creeperBoss, dropLocation, random, itemNumber);
  }

  //TODO: CREATE CHEST FOR EVERY PLAYER WITH THE SAME LOOT!!!!
  private void drop(@NotNull Player gameOwner, @NotNull Entity creeperBoss, @NotNull Location dropLocation, @NotNull Random random, int itemNumber) {
    var logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
    int amount = random.nextInt(3, 11);
    if (itemNumber == 1) {
      float chance = random.nextFloat();
      if (chance < 0.1) {
        Item.drop(dropLocation, Complex.MAGIC_STICK, 1);
        return;
      }else this.drop(gameOwner, creeperBoss, dropLocation, random, itemNumber+1);
    }else if (itemNumber == 2) {
      float chance = random.nextFloat();
      if (chance < 0.2) {
        dropLocation.getWorld().dropItem(dropLocation, this.enchantedBook(random));
        return;
      }else this.drop(gameOwner, creeperBoss, dropLocation, random, itemNumber+1);
    }else if (itemNumber == 3) {
      dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.NETHERITE_BLOCK, amount));
      return;
    }else if (itemNumber == 4) {
      dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.NETHERITE_INGOT, amount));
      return;
    }else if (itemNumber == 5) {
      dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.DIAMOND_BLOCK, amount));
      return;
    }else if (itemNumber == 6) {
      dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.DIAMOND, amount));
      return;
    }else {
      message(gameOwner, red("Failed to create Drops of the entity" + creeperBoss.getName() + "because: Item " + itemNumber + " is not exist"));
      logger.logError("Failed to create Drops for the Player " + gameOwner.getName() + "  of the entity " + creeperBoss.getName() + "because: Item " + itemNumber + " is not exist");
      return;
    }
  }

  @NotNull
  private ItemStack enchantedBook(@NotNull Random random) {
    int book = random.nextInt(4);
    if (book == 1) {
      ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta)itemStack.getItemMeta();
      meta.addStoredEnchant(Enchantment.PROTECTION, 5, true);
      itemStack.setItemMeta(meta);
      return itemStack;
    }else if (book == 2){
      ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta)itemStack.getItemMeta();
      meta.addStoredEnchant(Enchantment.MENDING, 1, true);
      itemStack.setItemMeta(meta);
      return itemStack;
    }else {
      ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta)itemStack.getItemMeta();
      meta.addStoredEnchant(Enchantment.LUCK_OF_THE_SEA, 5, true);
      itemStack.setItemMeta(meta);
      return itemStack;
    }
  }

}
