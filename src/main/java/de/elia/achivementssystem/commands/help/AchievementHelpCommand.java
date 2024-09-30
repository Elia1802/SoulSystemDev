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

//Give the Player a help about the Achievement Plugin
public class AchievementHelpCommand extends Command {

  public AchievementHelpCommand() {
    this("achievementhelp", "The achievementhelp command send the player a help about the achievement plugin in this plugin", "/achievementhelp [HELP]", Arrays.asList("achievementh", "ahelp"));
  }

  public AchievementHelpCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

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
