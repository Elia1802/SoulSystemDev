package de.elia.items.commands.help;

import de.elia.api.logging.PluginLogger;

import de.elia.items.ItemMain;

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

//This command gives the player a help about the item plugin.
public class ItemHelpCommand extends Command {

  public ItemHelpCommand() {
    this("itemhelp", "The itemhelp command send the player a help about the item plugin in this plugin", "Use /itemhelp [HELP]", Arrays.asList("ihelp", "itemh"));
  }

  public ItemHelpCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    PluginLogger logger = ItemMain.itemMain().itemLogger();
    if (sender instanceof Player player) {
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("givecommand")) {
          messageWithPrefix(player, gray("This Command ").append(aqua("/itemgive [ITEM] [AMOUNT] [PLAYER]")).append(gray("gives an specific amount of custom items to a specific player.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("helpcommand")) {
          messageWithPrefix(player, gray("This Command ").append(aqua("/itemhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("allcommands")) {
          messageWithPrefix(player, gray("This Command ").append(aqua("/itemgive [ITEM] [AMOUNT] [PLAYER]")).append(gray("gives an specific amount of custom items to a specific player.")));
          messageWithPrefix(player, gray("This Command ").append(aqua("/itemhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        messageWithPrefix(player, red("You can not used this command!"));
        return false;
      }
      messageWithPrefix(player, red("/itemCommand [HELP]"));
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
      tab.add("givecommand");
      tab.add("helpcommand");
      tab.add("allcommands");
      return tab;
    }
    return null;
  }
}
