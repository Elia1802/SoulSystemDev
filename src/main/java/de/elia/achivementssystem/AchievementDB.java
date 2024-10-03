package de.elia.achivementssystem;

import de.elia.achivementssystem.achievement.Achievement;
import de.elia.api.achievements.Achievements;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public interface AchievementDB {

  /**
   * This methode loads the database
   * @throws SQLException Gives an SQL error if something goes wrong when loading
   */
  void loadDatabase() throws SQLException;

  /**
   * This methode gives the {@link Player} an {@link Achievement}
   * @param player Needs the {@link Player} to give him the {@link Achievement}
   * @param achievement Needs the {@link Achievement} for the {@link Player}.
   */
  void setPlayerAchievement(@NotNull Player player, @NotNull Achievements achievement);

  /**
   * This methode removes the {@link Achievement} of a {@link Player}
   * @param player Needs the {@link Player} to remove his {@link Achievement}
   * @param achievement Needs the {@link Achievement} to remove of the {@link Player}.
   */
  void delPlayerAchievement(@NotNull Player player, @NotNull Achievements achievement);

  /**
   * This methode checks if the {@link Player} has the {@link Achievement}
   * @param player Needs the {@link Player} to check if the player has the {@link Achievement}
   * @param achievements Needs the {@link Achievement} to check whether the {@link Player} has the achievement
   * @return If the {@link Player} has the {@link Achievement}, it is true otherwise false
   */
  Boolean getAchievementOfPlayer(@NotNull Player player, @NotNull Achievements achievements);

  /**
   * This methode gets the connection to the database
   * @return The Connection to the Database
   * @throws SQLException For errors of the connection
   */
  Connection connection() throws SQLException;

}
