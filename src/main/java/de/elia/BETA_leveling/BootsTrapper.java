package de.elia;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Deprecated//USE THE BOOTSTRAPPER OF THE SYSTEM
public class BootsTrapper implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        context.getLogger().info("Boot " + context.getConfiguration().getName());
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        context.getLogger().info("Booting finished!");
        context.getLogger().info("Load the Plugin " + context.getConfiguration().getName());
        context.getLogger().info("Information about this Plugin");
        context.getLogger().info("Name:" + context.getConfiguration().getName());
        context.getLogger().info("API-Version:" + context.getConfiguration().getAPIVersion());
        context.getLogger().info("Version:" + context.getConfiguration().getVersion());
        context.getLogger().info("Authors:" + context.getConfiguration().getAuthors());
        return new Main();
    }
}
