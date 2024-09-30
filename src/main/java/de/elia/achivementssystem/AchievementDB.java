package de.elia.achivementssystem;

import de.elia.api.achievements.Achievements;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public interface AchievementDB {

  //This methode loads the database
  void loadDatabase() throws SQLException;

  //This methode set the player an achievement
  void setPlayerAchievement(@NotNull Player player, @NotNull Achievements achievement);

  //This methode delete the player an achievement
  void delPlayerAchievement(@NotNull Player player, @NotNull Achievements achievement);

  //This methode gets an achievement of a player
  Boolean getAchievementOfPlayer(@NotNull Player player, @NotNull Achievements achievements);

  //This methode gets the connection to the database
  Connection connection() throws SQLException;

}
