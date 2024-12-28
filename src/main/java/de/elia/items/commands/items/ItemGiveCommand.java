package de.elia.items.commands.items;

import de.elia.api.logging.PluginLogger;
import de.elia.api.thezepserapi.Complex;
import de.elia.api.thezepserapi.components.ComplexItem;

import de.elia.items.items.Item;
import de.elia.items.ItemMain;

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
 * This command gives a player an item.
 */
public class ItemGiveCommand extends Command {

  /**
   * Set the command, description, usage message and aliases
   */
  public ItemGiveCommand() {
    super("itemgive", "The itemgive command give the player a specify item", "Use /itemgive [ITEM] [AMOUNT] [PLAYER]", Arrays.asList("igive", "itemg"));
  }

  /**
   * Executes the command, returning its success
   * FIRST ARGUMENT = ITEM
   * SECOND ARGUMENT = AMOUNT OF THE ITEM
   * THIRD ARGUMENT = THE TARGET-PLAYER
   * @param sender Source object which is executing this command
   * @param commandLabel The alias of the command used
   * @param args All arguments passed to the command, split via ' '
   * @return true if the command was successful, otherwise false
   */
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    //Create a variable of the logger
    PluginLogger logger = ItemMain.itemMain().itemLogger();
    //Checks if the sender is a player and create a variable of the player
    if (sender instanceof Player player) {
      //Checks if the player has the permission to execute this command
      if (player.hasPermission("soulbosssystem.items.give")) {
        //Checks if the command length is correct
        if (args.length == 3) {
          //Gets the item of the first arument
          Complex complex = Complex.valueOf(args[0]);
          //Checks if key of the item saved
          if (ComplexItem.SAVED.containsKey(complex)) {
            //Get th target player of the third argument
            Player target = Bukkit.getPlayer(args[2]);
            //Checks if the not null
            if (target != null) {
              //Checks if the second argument contains "..."
              if (args[1].equalsIgnoreCase("...")) {
                //Send an error message to the player
                message(player, red("... is an example for an amounts higher than 9 and has no function."));
                return false;
              }
              //Give the target the item
              Item.give(target, Complex.valueOf(args[0]), Integer.parseInt(args[1]));
              //Send a message to the player
              messageWithPrefix(player, gray("You gave the player ").append(aqua(target.getName())).append(gray(" the item ")).append(aqua(args[0])).append(gray("!")));
              return true;
            }
            //Send an error message to the player
            message(player, red(args[2] + "is not online!"));
            return false;
          }
          return false;
        }
        //Send an error message to the player
        message(player, red("/itemgive [ITEM] [AMOUNT] [PLAYER]"));
        return false;
      }
      //Send an error message to the player
      messageWithPrefix(player, red("You can not used this command"));
      return false;
    }
    //Send an error message to the console
    logger.logWarning("You have to be a Player");
    return false;
  }

  /**
   * Executed on tab completion for this command, returning a list of options the player can tab through.
   * @param sender Source object which is executing this command
   * @param alias the alias being used
   * @param args All arguments passed to the command, split via ' '
   * @return a list of tab-completions for the specified arguments. This will never be null. List may be immutable.
   */
  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
    //Create 3 lists for th 3 arguments of the command
    ArrayList<String> tab1 = new ArrayList<>();
    ArrayList<String> tab2 = new ArrayList<>();
    ArrayList<String> tab3 = new ArrayList<>();
    //Checks if the command length 1
    if (args.length == 1) {
      //Get all saved items and add they to the list
      ComplexItem.SAVED.keySet().forEach(complex -> tab1.add(complex.toString()));
      return tab1;
    }
    //Checks if the command length 2
    if (args.length == 2) {
      //Add the numbers of 1 to 9 to the list
      tab2.add("1");
      tab2.add("2");
      tab2.add("3");
      tab2.add("4");
      tab2.add("5");
      tab2.add("6");
      tab2.add("7");
      tab2.add("8");
      tab2.add("9");
      tab2.add("...");
      return tab2;
    }
    //Checks if the command length 3
    if (args.length == 3) {
      //Get all online players and add they to the list
      Bukkit.getOnlinePlayers().forEach(player -> tab3.add(player.getName()));
      return tab3;
    }
    return null;
  }
}
