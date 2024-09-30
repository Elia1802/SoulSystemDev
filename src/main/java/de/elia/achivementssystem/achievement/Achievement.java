package de.elia.achivementssystem.achievement;

import de.elia.achivementssystem.AchievementDB;
import de.elia.achivementssystem.AchievementMain;
import de.elia.api.achievements.Achievements;
import de.elia.api.annotation.Beta;
import de.elia.api.events.achievement.AchievementGiveEvent;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;
import static de.elia.systemclasses.messages.Message.broadcastWithPrefix;

//This class give and remove the Player an Achievement.
@Beta("WARNING! DATA WILL NOW BE STORED IN THE DATABASE! Errors can still occur when saving to the database!")
public class Achievement {

  private static final AchievementDB ACHIEVEMENT_DB = AchievementMain.achievementMain().getAchievementDataBase();
  //Requires has the Player an Achievement
  public static boolean hasAchievement(@NotNull Player player, @NotNull Achievements achievement){
    return ACHIEVEMENT_DB.getAchievementOfPlayer(player, achievement);
  }

  //Give the player an Achievement
  public static void giveAchievement(@NotNull Player player, @NotNull Achievements achievement){
    //Save to the player an achievement in the database
    ACHIEVEMENT_DB.setPlayerAchievement(player, achievement);
    //Send a broadcast message
    broadcastWithPrefix(aqua(player.getName()).append(gray(" reached the bossfight achievement ").append(aqua(achievement.getName()).append(gray("!")))));
    //Send the target to become this achievement
    message(player, gray(achievement.target()));
    //Give the Player xp and play a sound for the player
    player.giveExp(achievement.xp());
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    //Run the achievement event
    AchievementGiveEvent event = new AchievementGiveEvent(player, achievement);
    AchievementMain.achievementMain().main().getServer().getPluginManager().callEvent(event);
  }

  //Remove the Achievement of the player
  public static void removeAchievement(@NotNull Player player, @NotNull Achievements achievement){
    //Delete the achievement in the database of the player
    ACHIEVEMENT_DB.delPlayerAchievement(player, achievement);
    message(player, gray("You took the player's achievement ").append(aqua(achievement.getName() + " (" + achievement.dataID() + ") ").append(gray("!"))));
  }
}

