package de.elia.items.recipes;

import de.elia.api.thezepserapi.Complex;

import de.elia.items.items.Item;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

import org.jetbrains.annotations.Nullable;

//This class creates the recipe for the creeper spawn egg recipe
public class CreeperSpawnEggRecipe {

  private static ShapedRecipe creeperSpawnEggRecipe;

  public static void eggRecipe() throws CloneNotSupportedException {
    ShapedRecipe recipe = new ShapedRecipe(NameSpacedKeys.CREEPER_RECIPE_KEY.key(), Item.get(Complex.CREEPER_SPAWN_EGG).getItem());
    recipe.shape(
      " T ",
              " C ",
              "FSE");
    recipe.setIngredient('T', Material.TNT);
    recipe.setIngredient('C', Material.CREEPER_HEAD);
    recipe.setIngredient('F', Material.FLINT_AND_STEEL);
    recipe.setIngredient('S', Material.PORKCHOP);
    recipe.setIngredient('E', Material.EGG);
    creeperSpawnEggRecipe = recipe;
  }

  @Nullable
  public static ShapedRecipe getCreeperSpawnEggRecipe() {
    return creeperSpawnEggRecipe;
  }
}
