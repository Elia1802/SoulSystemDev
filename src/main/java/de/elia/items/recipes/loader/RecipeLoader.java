package de.elia.items.recipes.loader;

import de.elia.api.logging.error.SaveError;

import de.elia.items.ItemMain;
import de.elia.items.recipes.CreeperSpawnEggRecipe;
import de.elia.items.recipes.ZombieSpawnEggRecipe;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

//This class loads all recipes of the items.
public class RecipeLoader {

  //This loads the recipe of the spawn eggs
  public static class SpawnEggLoader {

    public static void loadRecipe(Plugin plugin) {
      registerEggRecipe(plugin);
    }

    private static void registerEggRecipe(@NotNull Plugin plugin) {
      try {
        ZombieSpawnEggRecipe.eggRecipe();
        plugin.getServer().addRecipe(ZombieSpawnEggRecipe.bossSpawnEggRecipe());
        ItemMain.RECIPES.add(NameSpacedKeys.ZOMBIE_RECIPE_KEY.key());
        CreeperSpawnEggRecipe.eggRecipe();
        plugin.getServer().addRecipe(CreeperSpawnEggRecipe.getCreeperSpawnEggRecipe());
        ItemMain.RECIPES.add(NameSpacedKeys.CREEPER_RECIPE_KEY.key());
      }catch (CloneNotSupportedException exception) {
        SaveError.saveError(ItemMain.itemMain().main(), exception, "RecipeLoader-registerEggRecipe-An_error_occurred_because_you_cant_load_clone_recipes=CloneNotSupportedException");
        exception.printStackTrace();
      }
    }
  }
}
