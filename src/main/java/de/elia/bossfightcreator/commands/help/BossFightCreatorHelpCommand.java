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

/**
 * This command sends a help about the Bossfightcreator plugin
 * @author Elia
 * @since 1.0.0
 */
public class BossFightCreatorHelpCommand extends Command {

  /**
   * Set the settings of this command
   */
  public BossFightCreatorHelpCommand() {
    super("bossfightcreatorhelp", "The bossfightcreatorhelp command send the player a help about the bossfightcreator plugin in this Plugin", "Use /bossfightcreatorhelp [HELP]", Arrays.asList("bossfightcreatorh", "bfchelp"));
  }

  /**
   * The server executes this method when this command is entered
   * @param sender The {@link CommandSender} is the one that executed the command
   * @param commandLabel The command name (in this code achievementgive)
   * @param args The arguments of this command
   * @return Returns true if everything worked, otherwise false
   */
  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    //Get logger instnace
    PluginLogger logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
    //Checks if the sender is a player
    if (sender instanceof Player player) {
      //Checks if the command length correct
      if (args.length == 1) {
        //checks if the argument "tpworldcommand"
        if (args[0].equalsIgnoreCase("tpworldcommand")) {
          //Send a help message
          messageWithPrefix(player, gray("This Command ").append(aqua("/bossfightcreatorworld [PLAYER] [WORLD]")).append(gray("teleports a player to the world from SoulBossSystem and back.")));
          return true;
        }
        //checks if the argument "helpcommand"
        if (args[0].equalsIgnoreCase("helpcommand")) {
          //Send a help message
          messageWithPrefix(player, gray("This Command ").append(aqua("/bossfightcreatorhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        //checks if the argument "allcommands"
        if (args[0].equalsIgnoreCase("allcommands")) {
          //Send a help message
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

  /**
   * Create the tab function
   * @param sender Source object which is executing this command
   * @param alias the alias being used
   * @param args All arguments passed to the command, split via ' '
   * @return A list with the arguments
   */
  @Override
  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
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
