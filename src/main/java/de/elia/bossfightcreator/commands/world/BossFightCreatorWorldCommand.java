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

/**
 * This command teleports a player in another world
 */
public class BossFightCreatorWorldCommand extends Command {

  public BossFightCreatorWorldCommand() {
    super("bossfightcreatoworld", "The bossfightcreatorworld teleported a specify player in the worlds of this plugin and back to the normal world", "Use /bossfightcreatorhelp [PLAYER] [WORLD]", Arrays.asList("bossfightcreatorw", "bfchelw", "world", "tpworld"));
  }

  /**
   * The server executes this method when this command is entered
   * @param sender The {@link CommandSender} is the one that executed the command
   * @param commandLabel The command name (in this code achievementgive)
   * @param args The arguments of this command
   * @return Returns true if everything worked, otherwise false
   */
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    //Get the logger for this subplugin
    PluginLogger logger = BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger();
    //Check if the sender a player (and cast a player)
    if (sender instanceof Player || sender.hasPermission("soulbosssystem.bossfightgenerator.tpworld")) {
      //Cast a player
      Player player = (Player) sender;
      //Check if the command length 2
      if (args.length == 2) {
        //Set a target player with the first argument
        Player target = Bukkit.getPlayer(args[0]);
        //Checks if the player exists
        if (target.getName() == null) {
          //If not exist send player an error message
          message(player, red(args[0] + "does not exist or isn't online!"));
          return false;
        //Check if the second argument the world "world_bossfight"
        }else if (args[1].equalsIgnoreCase("world_bossfight")) {
          //Teleport the target to the world
          target.teleport(Bukkit.getWorld("world_bossfight").getSpawnLocation());
          //Send a confirm messages
          messageWithPrefix(target, gray("You were teleported into world ").append(aqua("world_bossfight")).append(gray("!")));
          messageWithPrefix(player, gray("You teleported player ").append(aqua(target.getName())).append(gray(" into world ")).append(aqua("world_bossfight")).append(gray("!")));
          return true;
        //Check if the second argument the world "world"
        }else if (args[1].equalsIgnoreCase("world")) {
          //Teleport the target to the world
          target.teleport(Bukkit.getWorld("world").getSpawnLocation());
          //Send a confirm messages
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
  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
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
