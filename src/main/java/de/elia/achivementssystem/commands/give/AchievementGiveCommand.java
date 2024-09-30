package de.elia.achivementssystem.commands.give;

import de.elia.achivementssystem.AchievementMain;
import de.elia.api.achievements.Achievements;
import de.elia.api.logging.PluginLogger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static de.elia.achivementssystem.achievement.Achievement.giveAchievement;
import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.gold;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.systemclasses.messages.Message.messageWithPrefix;

//A Command to give a Player an Achievement
public class AchievementGiveCommand extends Command {

  //Set the command, description, usage message and aliases
  private static final Map<String, String> ARGUMENTS = new HashMap<>();
  public AchievementGiveCommand() {
    this("achievementgive", "The achievementgive command give the player a Achievement.", "Use /achievementhelp [PLAYER] [ACHIEVEMENT]", Arrays.asList("achievementg", "agive"));
  }

  public AchievementGiveCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

  static {
    ARGUMENTS.put("bossfight", Achievements.BOSSFIGHT.getName());
    ARGUMENTS.put("bossfight_zombie", Achievements.BOSSFIGHT_ZOMBIE.getName());
    ARGUMENTS.put("bossfight_zombie_end", Achievements.BOSSFIGHT_ZOMBIE_END.getName());
    ARGUMENTS.put("bossfight_creeper", Achievements.BOSSFIGHT_CREEPER.getName());
    ARGUMENTS.put("bossfight_creeper_end", Achievements.BOSSFIGHT_CREEPER_END.getName());
    ARGUMENTS.put("bossfight_creeper_mini", Achievements.BOSSFIGHT_CREEPER_MINI.getName());
  }

  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String subCommand, String[] args) {
    //check if the executer a player
    if (sender instanceof Player player) {
      //check if the player has the permission for this command
      if (player.hasPermission("soulbosssystem.achievement.give")) {
        //check if the length of this command correct
        if (args.length == 2) {
          //get the target of the first argument
          Player target = Bukkit.getPlayer(args[0]);
          //check if the target online
          if (target.getName() == null) {
            message(player, gold("Dieser Spieler existiert nicht oder ist nicht online!"));
            return false;
          }
          //check witch achievement is selected in the second argument
          String argument = args[1];
          String predefinedArgument = ARGUMENTS.get(argument);
          if (args[1].equalsIgnoreCase("bossfight")) {
            //Try to give the player the achievement Bossfight if an error occurred, save this error and print this in the console
            this.achievement(target, Achievements.BOSSFIGHT);
            //Send the player and the target a message
            this.sendMessage(player, target, Achievements.BOSSFIGHT);
            return true;
          }
          if (args[1].equalsIgnoreCase("bossfight_zombie")) {
            //Try to give the player the achievement Bossfight_Zombie if an error occurred, save this error and print this in the console
            this.achievement(target, Achievements.BOSSFIGHT_ZOMBIE);
            //Send the player and the target a message
            this.sendMessage(player, target, Achievements.BOSSFIGHT_ZOMBIE);
            return true;
          }
          if (args[1].equalsIgnoreCase("bossfight_zombie_end")) {
            //Try to give the player the achievement Bossfight_Zombie_End if an error occurred, save this error and print this in the console
            this.achievement(target, Achievements.BOSSFIGHT_CREEPER_END);
            //Send the player and the target a message
            this.sendMessage(player, target, Achievements.BOSSFIGHT_ZOMBIE_END);
            return true;
          }
          if (args[1].equalsIgnoreCase("bossfight_creeper")) {
            //Try to give the player the achievement Bossfight_Creeper if an error occurred, save this error and print this in the console
            this.achievement(target, Achievements.BOSSFIGHT_CREEPER);
            //Send the player and the target a message
            this.sendMessage(player, target, Achievements.BOSSFIGHT_CREEPER);
            return true;
          }
          if (args[1].equalsIgnoreCase("bossfight_creeper_end")) {
            //Try to give the player the achievement Bossfight_Creeper_End if an error occurred, save this error and print this in the console
            this.achievement(target, Achievements.BOSSFIGHT_CREEPER_END);
            //Send the player and the target a message
            this.sendMessage(player, target, Achievements.BOSSFIGHT_CREEPER_END);
            return true;
          }
          if (args[1].equalsIgnoreCase("bossfight_creeper_mini")) {
            //Try to give the player the achievement Bossfight_Creeper_Mini if an error occurred, save this error and print this in the console
            this.achievement(target, Achievements.BOSSFIGHT_CREEPER_MINI);
            //Send the player and the target a message
            this.sendMessage(player, target, Achievements.BOSSFIGHT_CREEPER_MINI);
            return true;
          }
          message(player, red("The Achievement ").append(aqua(args[1])).append(red(" does not exist!")));
          return false;
        }
        message(player, red("/achievementgive [PLAYER] [ACHIEVEMENT]"));
        return false;
      }
      message(player, red("You can not use this command!"));
      return false;
    }
    message(sender, red("You have to be a Player!"));
    return false;
  }

  //This methode sends the messages if the admin gives the target an achievement
  private void sendMessage(@NotNull Player player, @NotNull Player target, @NotNull Achievements achievement){
    PluginLogger logger = AchievementMain.achievementMain().achievementPluginLogger();
    messageWithPrefix(target, gray("You achieved ").append(aqua(achievement.getName())).append(gray(" from the teammember ")).append(aqua(player.getName())).append(gray("!")));
    messageWithPrefix(player, gray("You gave ").append(aqua(target.getName())).append(gray(" the achievement ")).append(aqua(achievement.getName())).append(gray("!")));
    logger.logInfo("The teammember " + player.getName() + " gave the player " + target.getName() + " the achievement " + achievement.getName());
  }

  //Give the target the achievement
  private void achievement(@NotNull Player target, @NotNull Achievements achievement){
    giveAchievement(target, achievement);
  }

  @Override
  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String subCommand, String[] args) {
    ArrayList<String> tab1 = new ArrayList<>();
    ArrayList<String> tab2 = new ArrayList<>();
    if (args.length == 1) {
      Bukkit.getOnlinePlayers().forEach(player -> tab1.add(player.getName()));
      return tab1;
    }
    if (args.length == 2) {
      tab2.add("bossfight");
      tab2.add("bossfight_zombie");
      tab2.add("bossfight_zombie_end");
      tab2.add("bossfight_creeper");
      tab2.add("bossfight_creeper_end");
      tab2.add("bossfight_creeper_mini");
      return tab2;
    }
    return null;
  }
}
