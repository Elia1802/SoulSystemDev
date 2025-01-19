package de.elia.items;

import de.elia.Main;
import de.elia.api.loader.SoulPlugin;
import de.elia.api.logging.PluginLogger;

import de.elia.items.items.Item;
import de.elia.items.recipes.loader.RecipeLoader;
import de.elia.systemclasses.Instances;

import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

//This class is the main class of the item plugin
public class ItemMain implements SoulPlugin {

  public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
  public static final Set<NamespacedKey> RECIPES = new HashSet<>();
  private static ItemMain itemMain;

  //This methode loads this plugin.
  @Override
  public void enable(@NotNull JavaPlugin javaPlugin){
    this.itemLogger().logInfo("Load Item Plugin...");
    itemMain = this;
    this.itemLogger().logInfo("Load items...");
    Item.registerAll(javaPlugin);
    RecipeLoader.loadRecipe(javaPlugin);
    this.itemLogger().logInfo("Items loaded!");
    this.itemLogger().logInfo("Item plugin loaded!");
  }

  //This methode unloads this plugin.
  @Override
  public void disable(@NotNull JavaPlugin javaPlugin){
    this.itemLogger().logInfo("Item unloaded!");
    RECIPES.forEach(Bukkit::removeRecipe);
  }

  //Gets this class.
  @NotNull
  public static ItemMain itemMain() {
    return itemMain;
  }

  //Gets the instance, of the main class, of this system.
  @NotNull
  public Main main() {
    return Main.main();
  }

  //Gets the logger for this plugin.
  @NotNull
  public PluginLogger itemLogger() {
    return Instances.ITEM_LOGGER;
  }
}
