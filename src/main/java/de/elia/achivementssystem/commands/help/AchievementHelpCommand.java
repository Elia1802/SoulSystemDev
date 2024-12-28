package de.elia.achivementssystem.commands.help;

import de.elia.achivementssystem.AchievementMain;
import de.elia.api.logging.PluginLogger;
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
 * Give the Player a help about the Achievement Plugin
 */
public class AchievementHelpCommand extends Command {

  /**
   * Set the command, description, usage message and aliases
   */
  public AchievementHelpCommand() {
    super("achievementhelp", "The achievementhelp command send the player a help about the achievement plugin in this plugin", "/achievementhelp [HELP]", Arrays.asList("achievementh", "ahelp"));
  }

  /**
   * This command will be giving a {@link Player} help about the achievement plugin
   * @param sender The {@link CommandSender} is the one that executed the command
   * @param commandLabel The command name (in this code achievementgive)
   * @param args The arguments of this command
   * @return Returns true if everything worked, otherwise false
   */
  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    PluginLogger logger = AchievementMain.achievementMain().achievementPluginLogger();
    if (sender instanceof Player player) {
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("givecommand")) {
          messageWithPrefix(player, gray("The Command ").append(aqua("/achievementgive [PLAYER] [ACHIEVEMENT]")).append(gray("will give the player a specific achievement.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("helpcommand")) {
          messageWithPrefix(player, gray("The Command ").append(aqua("/achievementhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("allcommands")) {
          messageWithPrefix(player, gray("The Command ").append(aqua("/achievementgive [PLAYER] [ACHIEVEMENT]")).append(gray("will give the player a specific achievement.")));
          messageWithPrefix(player, gray("The Command ").append(aqua("/achievementhelp [COMMAND]")).append(gray("will give you help and information about this command.")));
          return true;
        }
        messageWithPrefix(player, red("This command does not exist!"));
        return false;
      }
      messageWithPrefix(player, red("/achievementhelp [HELP]"));
      return false;
    }
    logger.logWarning("You have to be a Player!");
    return false;
  }

  /**
   * Executed on tab completion for this command, returning a list of options the player can tab through.
   * @param sender Source object which is executing this command
   * @param alias the alias being used
   * @param args All arguments passed to the command, split via ' '
   * @return a list of tab-completions for the specified arguments. This will never be null. List may be immutable.
   */
  @Override
  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
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
