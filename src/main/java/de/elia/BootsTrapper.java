package de.elia;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;

import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

import static de.elia.PluginInfo.API_NAME;
import static de.elia.PluginInfo.API_VERSION;
import static de.elia.PluginInfo.AUTHOR;
import static de.elia.PluginInfo.NAME;
import static de.elia.PluginInfo.VERSION;

//This class booting this plugin
public class BootsTrapper implements PluginBootstrap {

  @Override
  public void bootstrap(@NotNull BootstrapContext context) {
    context.getLogger().info("Boot " + NAME + "...");
    context.getLogger().warn("This is the dev build of " + NAME + "!");
  }

  @Override
  @NotNull
  public JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
    context.getLogger().info("Information about this Plugin");
    context.getLogger().info("Name: " + NAME);
    context.getLogger().info("API-Name: " + API_NAME);
    context.getLogger().info("API-Version: " + API_VERSION);
    context.getLogger().info("Version: " + VERSION);
    context.getLogger().info("Authors: " + AUTHOR);
    context.getLogger().info("Booting finished!");
    context.getLogger().info("Load Main...");
    return new Main();
  }
}
