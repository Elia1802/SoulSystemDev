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

/**
 * This class give and remove the Player an Achievement.
 * @author Elia
 * @since 1.0.0
 */
@Beta("WARNING! DATA WILL NOW BE STORED IN THE DATABASE! Errors can still occur when saving to the database!")
public class Achievement {

  private static final AchievementDB ACHIEVEMENT_DB = AchievementMain.achievementMain().getAchievementDataBase();

  /**
   * This methode checks if the {@link Player} has the {@link Achievement}
   * @param player Needs the {@link Player} to check if the player has the {@link Achievement}
   * @param achievement Needs the {@link Achievement} to check whether the {@link Player} has the achievement
   * @return If the {@link Player} has the {@link Achievement}, it is true otherwise false
   */
  public static boolean hasAchievement(@NotNull Player player, @NotNull Achievements achievement){
    return ACHIEVEMENT_DB.getAchievementOfPlayer(player, achievement);
  }

  /**
   * This methode gives the {@link Player} an {@link Achievement}
   * @param player Needs the {@link Player} to give him the {@link Achievement}
   * @param achievement Needs the {@link Achievement} for the {@link Player}.
   */
  public static void giveAchievement(@NotNull Player player, @NotNull Achievements achievement){
    if (hasAchievement(player, achievement)){return;}
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
    //Call the achievement event
    AchievementMain.achievementMain().main().getServer().getPluginManager().callEvent(event);
  }

  /**
   * This methode removes the {@link Achievement} of a {@link Player}
   * @param player Needs the {@link Player} to remove his {@link Achievement}
   * @param achievement Needs the {@link Achievement} to remove of the {@link Player}.
   */
  public static void removeAchievement(@NotNull Player player, @NotNull Achievements achievement){
    //Delete the achievement in the database of the player
    ACHIEVEMENT_DB.delPlayerAchievement(player, achievement);
    message(player, gray("You took the player's achievement ").append(aqua(achievement.getName() + " (" + achievement.dataID() + ") ").append(gray("!"))));
  }
}

