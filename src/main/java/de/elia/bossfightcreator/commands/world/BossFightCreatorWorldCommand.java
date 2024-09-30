package de.elia.bossfightcreator.commands.world;

import de.elia.api.logging.PluginLogger;

import de.elia.bossfightcreator.BossFightCreatorMain;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.systemclasses.messages.Message.messageWithPrefix;

//This command teleports a player in another world
public class BossFightCreatorWorldCommand extends Command {

  public BossFightCreatorWorldCommand() {
    this("bossfightcreatoworld", "The bossfightcreatorworld teleported a specify player in the worlds of this plugin and back to the normal world", "Use /bossfightcreatorhelp [PLAYER] [WORLD]", Arrays.asList("bossfightcreatorw", "bfchelw", "world", "tpworld"));
  }

  public BossFightCreatorWorldCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    PluginLogger logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
    if (sender instanceof Player player) {
      if (player.hasPermission("soulbosssystem.bossfightgenerator.tpworld")) {
        if (args.length == 2) {
          Player target = Bukkit.getPlayer(args[0]);
          if (target.getName() == null) {
            message(player, red(args[0] + "does not exist or isn't online!"));
            return false;
          }
          if (args[1].equalsIgnoreCase("world_bossfight")) {
            target.teleport(Bukkit.getWorld("world_bossfight").getSpawnLocation());
            messageWithPrefix(target, gray("You were teleported into world ").append(aqua("world_bossfight")).append(gray("!")));
            messageWithPrefix(player, gray("You teleported player ").append(aqua(target.getName())).append(gray(" into world ")).append(aqua("world_bossfight")).append(gray("!")));
            return true;
          }
          if (args[1].equalsIgnoreCase("world")) {
            target.teleport(Bukkit.getWorld("world").getSpawnLocation());
            messageWithPrefix(target, gray("You were teleported into world ").append(aqua("world")).append(gray("!")));
            messageWithPrefix(player, gray("You teleported player ").append(aqua(target.getName())).append(gray(" into world ")).append(aqua("world")).append(gray("!")));
            return true;
          }
          messageWithPrefix(player, red("This command does not exist!"));
          return false;
        }
        messageWithPrefix(player, red("/tpworld world_bossfight"));
        messageWithPrefix(player, red("/tpworld world"));
        return false;
      }
      messageWithPrefix(player, red("You can not used this command!"));
      return false;
    }
    logger.logWarning("You have to be a Player!");
    return false;
  }

  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
    ArrayList<String> tab1 = new ArrayList<>();
    ArrayList<String> tab2 = new ArrayList<>();
    if (args.length == 1) {
      Bukkit.getOnlinePlayers().forEach(player -> tab1.add(player.getName()));
      return tab1;
    }
    if (args.length == 2) {
      tab2.add("world");
      tab2.add("world_bossfight");
      return tab2;
    }
    return null;
  }
}
