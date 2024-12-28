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

/**
 * This command gives the player a help about the item plugin.
 */
public class ItemHelpCommand extends Command {

  /**
   * Set the command, description, usage message and aliases
   */
  public ItemHelpCommand() {
    super("itemhelp", "The itemhelp command send the player a help about the item plugin in this plugin", "Use /itemhelp [HELP]", Arrays.asList("ihelp", "itemh"));
  }

  /**
   * The server executes this method when this command is entered
   * @param sender Source object which is executing this command
   * @param commandLabel The alias of the command used
   * @param args All arguments passed to the command, split via ' '
   * @return true if the command was successful, otherwise false
   */
  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    //Create a logger variable
    PluginLogger logger = ItemMain.itemMain().itemLogger();
    //Checks if the sender a player and create a variable of the player
    if (sender instanceof Player player) {
      //Checks if the command length is correct
      if (args.length == 1) {
        //checks if the argument "givecommand"
        if (args[0].equalsIgnoreCase("givecommand")) {
          //Send help message
          messageWithPrefix(player, gray("This Command ").append(aqua("/itemgive [ITEM] [AMOUNT] [PLAYER]")).append(gray("gives an specific amount of custom items to a specific player.")));
          return true;
        }
        //checks if the argument "helpcommand"
        if (args[0].equalsIgnoreCase("helpcommand")) {
          //Send help message
          messageWithPrefix(player, gray("This Command ").append(aqua("/itemhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        //checks if the argument "allcommand"
        if (args[0].equalsIgnoreCase("allcommands")) {
          //Send help messages
          messageWithPrefix(player, gray("This Command ").append(aqua("/itemgive [ITEM] [AMOUNT] [PLAYER]")).append(gray("gives an specific amount of custom items to a specific player.")));
          messageWithPrefix(player, gray("This Command ").append(aqua("/itemhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        //Send error message
        messageWithPrefix(player, red("You can not used this command!"));
        return false;
      }
      //Send error message
      messageWithPrefix(player, red("/itemCommand [HELP]"));
      return false;
    }
    //Send error message
    logger.logWarning("You have to be a Player!");
    return false;
  }

  /**
   * While the CommandSender enters the command, the server gives suggestions for achievements and players
   * @param sender Source object which is executing this command
   * @param alias the alias being used
   * @param args All arguments passed to the command, split via ' '
   * @return a list of tab-completions for the specified arguments. This will never be null. List may be immutable.
   */
  @Override
  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
    //Create a list for all arguments of the command
    ArrayList<String> tab = new ArrayList<>();
    //Checks if the command length correct
    if (args.length == 1) {
      //Add all arguments in the list
      tab.add("givecommand");
      tab.add("helpcommand");
      tab.add("allcommands");
      return tab;
    }
    return null;
  }
}
