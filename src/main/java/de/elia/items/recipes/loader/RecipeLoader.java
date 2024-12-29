package de.elia.items.recipes.loader;

import de.elia.api.logging.error.SaveError;

import de.elia.items.ItemMain;
import de.elia.items.recipes.CreeperSpawnEggRecipe;
import de.elia.items.recipes.ZombieSpawnEggRecipe;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

/**
 * This class loads all recipes of the items.
 * @author Elia
 * @since 2.0.0
 */
public class RecipeLoader {

  /**
   * This loads the recipes
   * @param plugin Requires the main instance of the plugin
   */
  public static void loadRecipe(Plugin plugin) {
    registerEggRecipe(plugin);
  }

  /**
   * This loads the recipe of the spawn eggs
   * @param plugin Requires the main instance of the plugin
   */
  private static void registerEggRecipe(@NotNull Plugin plugin) {
    try {
      //Load the zombie spawn egg recipe
      ZombieSpawnEggRecipe.eggRecipe();
      //Add the recipe to the Server
      plugin.getServer().addRecipe(ZombieSpawnEggRecipe.bossSpawnEggRecipe());
      //Gives the recipe a Mame spaced key
      ItemMain.RECIPES.add(NameSpacedKeys.ZOMBIE_RECIPE_KEY.key());
      //Load the zombie spawn egg recipe
      CreeperSpawnEggRecipe.eggRecipe();
      //Add the recipe to the Server
      plugin.getServer().addRecipe(CreeperSpawnEggRecipe.getCreeperSpawnEggRecipe());
      //Gives the recipe a Mame spaced key
      ItemMain.RECIPES.add(NameSpacedKeys.CREEPER_RECIPE_KEY.key());
    }catch (CloneNotSupportedException exception) {
      //Save and print an error
      SaveError.saveError(ItemMain.itemMain().main(), exception, "RecipeLoader-registerEggRecipe-An_error_occurred_because_you_cant_load_clone_recipes=CloneNotSupportedException");
      exception.printStackTrace();
    }
  }
}
