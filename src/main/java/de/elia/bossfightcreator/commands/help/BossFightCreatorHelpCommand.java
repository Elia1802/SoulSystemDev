package de.elia.bossfightcreator.commands.help;

import de.elia.api.logging.PluginLogger;

import de.elia.bossfightcreator.BossFightCreatorMain;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.systemclasses.messages.Message.messageWithPrefix;

//This command sends a help about the Bossfightcreator plugin
public class BossFightCreatorHelpCommand extends Command {

  public BossFightCreatorHelpCommand() {
    this("bossfightcreatorhelp", "The bossfightcreatorhelp command send the player a help about the bossfightcreator plugin in this Plugin", "Use /bossfightcreatorhelp [HELP]", Arrays.asList("bossfightcreatorh", "bfchelp"));
  }

  public BossFightCreatorHelpCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    PluginLogger logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
    if (sender instanceof Player player) {
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("tpworldcommand")) {
          messageWithPrefix(player, gray("This Command ").append(aqua("/bossfightcreatorworld [PLAYER] [WORLD]")).append(gray("teleports a player to the world from SoulBossSystem and back.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("helpcommand")) {
          messageWithPrefix(player, gray("This Command ").append(aqua("/bossfightcreatorhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("allcommands")) {
          messageWithPrefix(player, gray("This Command ").append(aqua("/bossfightcreatorworld [PLAYER] [WORLD]")).append(gray("teleports a player to the world from SoulBossSystem and back.")));
          messageWithPrefix(player, gray("This Command ").append(aqua("/bossfightcreatorhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        messageWithPrefix(player, red("This command does not exist!"));
        return false;
      }
      messageWithPrefix(player, red("/bossfighthelp [HELP]"));
      return false;
    }
    logger.logWarning("You have to be a Player!");
    return false;
  }

  @Override
  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
    ArrayList<String> tab = new ArrayList<>();
    if (args.length == 1) {
      tab.add("tpworldcommand");
      tab.add("helpcommand");
      tab.add("allcommands");
      return tab;
    }
    return null;
  }
}
