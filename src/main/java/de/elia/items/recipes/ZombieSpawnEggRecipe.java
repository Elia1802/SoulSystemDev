package de.elia.items.recipes;

import de.elia.api.thezepserapi.Complex;

import de.elia.items.items.Item;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

/**
 * This class creates the recipe for the zombie spawn egg recipe
 * @author Elia
 * @since 2.0.0
 */
public class ZombieSpawnEggRecipe {

  private static ShapedRecipe bossSpawnEggRecipe;

  /**
   * This methode create the recipe
   * @throws CloneNotSupportedException The object could not or should not be cloned.
   */
  public static void eggRecipe() throws CloneNotSupportedException {
    ShapedRecipe recipe = new ShapedRecipe(NameSpacedKeys.ZOMBIE_RECIPE_KEY.key(), Item.get(Complex.ZOMBIE_SPAWN_EGG).getItem());
    recipe.shape("ZZZ", "WSW", "CSC");
    recipe.setIngredient('Z', Material.ZOMBIE_HEAD);
    recipe.setIngredient('W', Material.GREEN_WOOL);
    recipe.setIngredient('S', Material.SOUL_SAND);
    recipe.setIngredient('C', Material.CYAN_CONCRETE);
    bossSpawnEggRecipe = recipe;
  }

  /**
   * Get the recipe
   * @return the recipe
   */
  @Nullable
  public static ShapedRecipe bossSpawnEggRecipe() {
    return bossSpawnEggRecipe;
  }
}
