package de.elia.systemclasses.plugin.commands.help;

import de.elia.api.logging.PluginLogger;

import de.elia.systemclasses.plugin.SoulBossSystemMain;

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

//This is the main help command for all commands
public class SoulBossSystemHelpCommand extends Command {

  public SoulBossSystemHelpCommand() {
    this("PlanetEcosystems", "Send a help about all plugins in this plugin", "/PlanetEcosystems [HELP]", Arrays.asList("sbshelp", "sbsh"));
  }

  public SoulBossSystemHelpCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    PluginLogger logger = SoulBossSystemMain.soulBossSystemMain().soulBossSystemLogger();
    if (sender instanceof Player player) {
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("achievementplugin")) {
          messageWithPrefix(player, gray("Der command").append(aqua("/achievementgive [PLAYER] [ACHIEVEMENT]")).append(gray("gibt einen Spieler ein bestimmtes achievement.")));
          messageWithPrefix(player, gray("Der command").append(aqua("/achievementhelp [COMMAND]")).append(gray("gibt dir eine Hilfe über dieses Plugin.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("bossfightcreatorplugin")) {
          messageWithPrefix(player, gray("Der command").append(aqua("/bossfightcreatorworld [PLAYER] [WORLD]")).append(gray("teleportiert einen Spieler in die Welten vom SoulBossSystem und zurück.")));
          messageWithPrefix(player, gray("Der command").append(aqua("/bossfightcreatorhelp [COMMAND]")).append(gray("gibt dir eine Hilfe über dieses Plugin.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("itemsplugin")) {
          messageWithPrefix(player, gray("Der command").append(aqua("/itemgive [ITEM] [AMOUNT] [PLAYER]")).append(gray(" gibt dir eine bestimmte Anzahl an Items an einen bestimmten Spieler.")));
          messageWithPrefix(player, gray("Der command").append(aqua("/itemhelp [COMMAND]")).append(gray("gibt dir eine Hilfe über dieses Plugin.")));
          return true;
        }
        if (args[0].equalsIgnoreCase("soulbosssystemplugin")) {
          messageWithPrefix(player, gray("Der command").append(aqua("soulbosssystem [HELP]")).append(gray(" gibt dir diese Hilfe")));
          messageWithPrefix(player, gray("Der command").append(aqua("/soulbosssystemidea [IDEA]")).append(gray("ermöglicht dir eine Idee abzusenden.")));
          messageWithPrefix(player, gray("Der command").append(aqua("/soulbosssystemplugin help")).append(gray(" gibt dir Hilfe für den command selbst.")));
          return true;
        }
        messageWithPrefix(player, red("Dieser Command exsistiert nicht!"));
        return false;
      }
      messageWithPrefix(player, red("Dieser Command exsistiert nicht!"));
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
      tab.add("achievementplugin");
      tab.add("bossfightcreatorplugin");
      tab.add("itemsplugin");
      tab.add("soulbosssystemplugin");
      return tab;
    }
    return null;
  }
}
