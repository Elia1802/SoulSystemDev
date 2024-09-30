package de.elia.systemclasses.register.commands;

import de.elia.achivementssystem.commands.give.AchievementGiveCommand;
import de.elia.achivementssystem.commands.help.AchievementHelpCommand;
import de.elia.bossfightcreator.commands.help.BossFightCreatorHelpCommand;
import de.elia.bossfightcreator.commands.world.BossFightCreatorWorldCommand;
import de.elia.items.commands.help.ItemHelpCommand;
import de.elia.items.commands.items.ItemGiveCommand;
import de.elia.party.commands.PartyCommand;
import de.elia.systemclasses.plugin.commands.help.SoulBossSystemHelpCommand;
import de.elia.systemclasses.plugin.commands.idea.SoulBossSystemIdeaCommand;
import de.elia.systemclasses.plugin.commands.plugin.SoulBossSystemCommand;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;

import java.util.HashMap;
import java.util.Map;


//This class registers all commands
public class CommandRegister {
  private static final Map<String, Command> COMMANDS = new HashMap<>();

  public static void registerCommands(Server server) {
    COMMANDS.forEach((s, command) -> server.getCommandMap().register(s, "Soulbosssytem", command));
  }

  public static void registerCommands() {
    CommandRegister.registerCommands(Bukkit.getServer());
  }

  static {
    COMMANDS.put("achievementgive", new AchievementGiveCommand());
    COMMANDS.put("achievementhelp", new AchievementHelpCommand());
    COMMANDS.put("bossfightcreatorhelp", new BossFightCreatorHelpCommand());
    COMMANDS.put("bossfightcreatoworld", new BossFightCreatorWorldCommand());
    //COMMANDS.put("soulpack", new TexturePackManager(main(), main().getServer().getPluginManager()));
    COMMANDS.put("itemhelp", new ItemHelpCommand());
    COMMANDS.put("itemgive", new ItemGiveCommand());
    COMMANDS.put("soulbosssystemhelp", new SoulBossSystemHelpCommand());
    COMMANDS.put("soulbosssystemidea", new SoulBossSystemIdeaCommand());
    COMMANDS.put("soulbosssystem", new SoulBossSystemCommand());
    COMMANDS.put("party", new PartyCommand());
  }
}
