package de.elia.systemclasses.plugin.commands.idea;

import de.elia.api.logging.PluginLogger;

import de.elia.systemclasses.plugin.SoulBossSystemMain;
import de.elia.systemclasses.plugin.configuation.SoulBossSystemConfigurationLoader;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.gold;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.systemclasses.messages.Message.messageWithPrefix;

//This is a command to save ideas of other players
public class SoulBossSystemIdeaCommand extends Command {

  public SoulBossSystemIdeaCommand() {
    this("soulbosssystemidea", "The Player can send a idea for the server team.", "Use /soulbosssystemidea", Arrays.asList("sbsidea", "sbsi", "idea"));
  }

  public SoulBossSystemIdeaCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    PluginLogger logger = SoulBossSystemMain.soulBossSystemMain().soulBossSystemLogger();
    if (sender instanceof Player player) {
      if (args.length > 1) {
        StringBuilder idea = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
          idea.append(args[i]).append(" ");
        }
        if (SoulBossSystemConfigurationLoader.ideasStorage().get(player.getName()) == null) {
          String string = idea.toString();
          SoulBossSystemConfigurationLoader.ideasStorage().set(player.getName(), string);
          messageWithPrefix(player, gray("Du hast die Idee erfolgreich abgesendet!"));
          messageWithPrefix(player, gray("Deine Idee: "));
          messageWithPrefix(player, aqua(string));
          return true;
        }
        messageWithPrefix(player, red("Du hast eine Idee schon abgegeben!"));
        messageWithPrefix(player, gold("Eine neue Idee kannst du erst eingeben, wenn ein Teammitglied dein Antrag aus der Configuration ausgelesen hat!"));
        return false;
      }
      messageWithPrefix(player, red("/soulbosssystemidea [IDEA]"));
      return false;
    }
    logger.logWarning("You have to be a Player!");
    return false;
  }
}
