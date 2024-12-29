package de.elia.items.recipes;

import de.elia.api.thezepserapi.Complex;

import de.elia.items.items.Item;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

import org.jetbrains.annotations.Nullable;

/**
 * This class creates the recipe for the creeper spawn egg recipe
 * @author Elia
 * @since 3.0.0
 */
public class CreeperSpawnEggRecipe {

  private static ShapedRecipe creeperSpawnEggRecipe;

  /**
   * This methode create the recipe
   * @throws CloneNotSupportedException The object could not or should not be cloned.
   */
  public static void eggRecipe() throws CloneNotSupportedException {
    //Create the new recipe
    ShapedRecipe recipe = new ShapedRecipe(NameSpacedKeys.CREEPER_RECIPE_KEY.key(), Item.get(Complex.CREEPER_SPAWN_EGG).getItem());
    //Shaped the recipe
    recipe.shape(
      " T ",
      " C ",
      "FSE");
    //set the ingedients
    recipe.setIngredient('T', Material.TNT);
    recipe.setIngredient('C', Material.CREEPER_HEAD);
    recipe.setIngredient('F', Material.FLINT_AND_STEEL);
    recipe.setIngredient('S', Material.PORKCHOP);
    recipe.setIngredient('E', Material.EGG);
    //Set the recipe
    creeperSpawnEggRecipe = recipe;
  }

  /**
   * Get the recipe
   * @return the recipe
   */
  @Nullable
  public static ShapedRecipe getCreeperSpawnEggRecipe() {
    return creeperSpawnEggRecipe;
  }
}
