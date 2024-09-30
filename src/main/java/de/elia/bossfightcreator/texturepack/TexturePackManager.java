package de.elia.bossfightcreator.texturepack;

import de.elia.bossfightcreator.BossFightCreatorMain;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static de.elia.api.messages.builder.MessageBuilder.gold;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.green;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.bossfightcreator.texturepack.TexturePackPlayerState.setState;
import static de.elia.bossfightcreator.texturepack.TexturePackPlayerState.state;

@Deprecated
public class TexturePackManager extends Command implements Listener {

  private Plugin plugin;
  //Resourcepack:
  private final String url = "";
  private final String hash = "";

  @Deprecated
  protected TexturePackManager(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

  @Deprecated
  public TexturePackManager(@NotNull Plugin pl, @NotNull PluginManager manager){
    this("soulpack", "If the player accept load the plugin a Resourcepack.", "/soulpack accept/decline confirm", List.of());
    this.plugin = pl;
    manager.registerEvents(this, pl);
  }

  @Deprecated
  private void askPlayer(Player player){
    message(player, gold("Do you want to load a resource pack from the SoulbossSystem plugin?"));
    message(player, gray("Use /soulpack accept to accept this or /soulpack decline to decline!"));
  }

  @Deprecated
  @EventHandler
  private void onJoin(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!state(player, this.plugin)) {
      this.askPlayer(player);
    }
  }

  @Deprecated
  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    var logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
    if (sender instanceof Player player) {
      if (args.length == 2) {
        if (args[0].equalsIgnoreCase("accept")) {
          player.setResourcePack(url, hash);
          message(player, green("The Resourcepack is loaded!"));
          setState(player, this.plugin, true);
          return true;
        }else if (args[0].equalsIgnoreCase("decline")) {
          if (args[1].equalsIgnoreCase("confirm")) {
            message(player, red("No resource pack will be loaded!"));
            setState(player, this.plugin, false);
          }else {
            message(player, gold("Are you sure you don't want the resource pack?"));
            message(player, gold("We take no responsibility if items are not loaded correctly!"));
            message(player, red("ERRORS MAY OCCUR WITH BEDROCK PLAYERS!!! NOT EVERYTHING WORKS ON BEDROCK!!!"));
            message(player, gold("Use \"/soulpack decline confirm\" to confirm!"));
            return false;
          }
          return false;
        }else {
          message(player, red(this.usageMessage));
          return false;
        }
      }else {
        message(player, red(this.usageMessage));
        return false;
      }
    }else {
      logger.logError("You have to be a Player for this action!");
      return false;
    }
  }

  @Deprecated
  public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
    List<String> list = new ArrayList<>();
    if (args.length == 1) {
      list.add("accept");
      list.add("decline");
      return list;
    }
    return null;
  }
}
