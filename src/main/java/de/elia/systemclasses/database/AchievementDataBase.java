package de.elia.systemclasses.database;

import de.elia.Main;
import de.elia.achivementssystem.AchievementDB;
import de.elia.api.achievements.Achievements;
import de.elia.api.annotation.Beta;
import de.elia.api.logging.PluginLogger;
import de.elia.api.logging.error.SaveError;
import de.elia.bossfightcreator.BossFightCreatorMain;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.elia.api.logging.error.SaveError.saveError;
import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.message;

@Beta("WARNING! DATA WILL NOW BE STORED IN THE DATABASE! Errors can still occur when saving to the database!")
public class AchievementDataBase implements AchievementDB {

  private final PluginLogger logger = new PluginLogger("SoulBossSystem -> Achievement-Database");
  private final Integer version = Main.main().getDatabaseConfiguration().getInt("version");
  private final String table = "achievements";
  private final String host = Main.main().getDatabaseConfiguration().getString("host");
  private final Integer port = Main.main().getDatabaseConfiguration().getInt("port");
  private final String user = Main.main().getDatabaseConfiguration().getString("user");
  private final String password = Main.main().getDatabaseConfiguration().getString("password");
  private final String database = Main.main().getDatabaseConfiguration().getString("database");

  @NotNull
  private DataSource newDataSource(){
    MysqlDataSource source = new MysqlDataSource();
    source.setURL("jdbc:mysql://" + this.host + ":" + this.port + "/");
    source.setDatabaseName(this.database);
    source.setUser(this.user);
    source.setPassword(this.password);
    return source;
  }

  private final DataSource dataSource = this.newDataSource();

  public void loadDatabase() throws SQLException {
    if (this.version == 1) {
      Connection connection = this.dataSource.getConnection();
      PreparedStatement statement =
              connection.prepareStatement("CREATE TABLE " + this.table + "(uuid VARCHAR(50) PRIMARY KEY, achievement_id VARCHAR(50))");
      statement.executeUpdate();
      Main.main().getDatabaseConfiguration().addDefault("version", 1);
    }
  }

  public void setPlayerAchievement(@NotNull Player player, @NotNull Achievements achievement){
    new BukkitRunnable() {
      @Override
      public void run() {
        try {
          Connection connection = AchievementDataBase.this.dataSource.getConnection();
          PreparedStatement statement =
                  connection.prepareStatement("INSERT INTO " + AchievementDataBase.this.table + "(uuid, achievement_id) VALUES (?,?)");
          statement.setString(1, AchievementDataBase.this.convertUUID(player.getUniqueId()));
          statement.setString(2, achievement.dataID());
          statement.execute();
        }catch (SQLException firstException) {
          firstException.printStackTrace();
          saveError(BossFightCreatorMain.bossFightCreator().main(), firstException, "AchievementDataBase-setPlayerAchievement-An_error_occurred_to_save_an_achievement_for_a_player=SQLException");
          try {
            Connection connection = AchievementDataBase.this.dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE " + AchievementDataBase.this.table + " SET achievement_id = CONCAT(achievement_id, ',' ?) WHERE uuid = ?");
            statement.setString(1, achievement.dataID());
            statement.setString(2, AchievementDataBase.this.convertUUID(player.getUniqueId()));
            statement.execute();
          }catch (SQLException secondException) {
            secondException.printStackTrace();
            saveError(BossFightCreatorMain.bossFightCreator().main(), secondException, "AchievementDataBase-setPlayerAchievement-An_error_occurred_to_save_an_achievement_for_a_player=SQLException");
            try {
              Connection connection = AchievementDataBase.this.dataSource.getConnection();
              PreparedStatement statement =
                      connection.prepareStatement("UPDATE " + AchievementDataBase.this.table + " SET achievement_id = ? WHERE uuid = ?");
              statement.setString(1, achievement.dataID());
              statement.setString(2, AchievementDataBase.this.convertUUID(player.getUniqueId()));
              statement.executeUpdate();
            }catch (SQLException  thirdException) {
              thirdException.printStackTrace();
              saveError(BossFightCreatorMain.bossFightCreator().main(), thirdException, "AchievementDataBase-setPlayerAchievement-An_error_occurred_to_save_an_achievement_for_a_player=SQLException");
            }
          }
        }
      }
    }.runTaskAsynchronously(Main.main());
  }

  public void delPlayerAchievement(@NotNull Player player, @NotNull Achievements achievement){
    DeleteUtils deleteUtils = new DeleteUtils();
    if (deleteUtils.requestData(player, achievement)) {
      if (DeleteUtils.VIRTUAL_DATA_SAVER.containsKey(this.convertUUID(player.getUniqueId()))) {
        deleteUtils.updateDataValue(player, DeleteUtils.VIRTUAL_DATA_SAVER.get(this.convertUUID(player.getUniqueId())));
        DeleteUtils.VIRTUAL_DATA_SAVER.remove(this.convertUUID(player.getUniqueId()));
      }
    }
  }

  @NotNull
  public Boolean getAchievementOfPlayer(@NotNull Player player, @NotNull Achievements achievements){
    new BukkitRunnable() {
      @Override
      public void run() {
        try {
          Connection connection = AchievementDataBase.this.dataSource.getConnection();
          PreparedStatement statement =
            connection.prepareStatement("SELECT uuid, achievement_id FROM " + AchievementDataBase.this.table + " WHERE uuid = ?");
          statement.setString(1, AchievementDataBase.this.convertUUID(player.getUniqueId()));
          ResultSet result = statement.executeQuery();
          if (result.next()) {
            if (result.getString("achievement_id").contains(achievements.dataID())) {
              message(player, gray("Du hast das Achievement ").append(aqua(achievements.getName())).append(gray("!")));
              logger.logInfo("The player " + player.getName() + " has the achievement " + achievements.getName() + "!");
            } else {
              message(player, gray("Du hast das Achievement ").append(aqua(achievements.getName())).append(gray(" nicht!")));
              logger.logWarning("The player " + player.getName() + " hasn't the achievement " + achievements.getName() + "!");
            }
          } else {
            message(player, gray("Der Spieler, mit der uuid ").append(aqua(player.getUniqueId().toString())).append(gray(", wurde nicht gefunden!")));
            logger.logWarning("The player with the uuid " + player.getName() + " is not exist!");
          }
        }catch (SQLException exception) {
          exception.printStackTrace();
          SaveError.saveError(Main.main(), exception, "AchievementDataBase-getAchievementOfPlayer-An_error_occurred_to_get_an_achievement_of_a_player");
          return;
        }
      }
    }.runTaskAsynchronously(Main.main());
    return true;
  }

  @NotNull
  private String convertUUID(@NotNull UUID uuid) {
    return uuid.toString();
  }

  public class DeleteUtils {

    private static final Map<String, String> VIRTUAL_DATA_SAVER = new HashMap<>();

    public boolean requestData(@NotNull Player player, @NotNull Achievements achievement){
      new BukkitRunnable() {
        @Override
        public void run() {
          try {
            Connection connection = AchievementDataBase.this.dataSource.getConnection();
            PreparedStatement statement =
              connection.prepareStatement("SELECT uuid, achievement_id FROM " + AchievementDataBase.this.table + " WHERE uuid = ?");
            statement.setString(1, AchievementDataBase.this.convertUUID(player.getUniqueId()));
            ResultSet result = statement.executeQuery();
            if (result.next());
            if (result.getString("achievement_id").contains(achievement.dataID() + ", ")) {
              String value = result.getString("achievement_id");
              String oldValueToDelete = achievement.dataID() + ", ";
              if (value.equals(oldValueToDelete)) {
                value.replaceAll(oldValueToDelete, "");
                VIRTUAL_DATA_SAVER.put(AchievementDataBase.this.convertUUID(player.getUniqueId()), "");
              }
              Pattern pattern = Pattern.compile("\\b" + Pattern.quote(oldValueToDelete) + "\\b");
              Matcher matcher = pattern.matcher(value);
              if (matcher.find()) {
                String restValue = value.substring(0, matcher.start()) + value.substring(matcher.end());
                VIRTUAL_DATA_SAVER.put(AchievementDataBase.this.convertUUID(player.getUniqueId()), restValue);
              }
            }
            if (result.getString("achievement_id").contains(achievement.dataID())) {
              String value = result.getString("achievement_id");
              String oldValueToDelete  = achievement.dataID();
              if (value.equals(oldValueToDelete)) {
                value.replaceAll(oldValueToDelete, "");
                VIRTUAL_DATA_SAVER.put(AchievementDataBase.this.convertUUID(player.getUniqueId()), "");
              }
              Pattern pattern = Pattern.compile("\\b" + Pattern.quote(oldValueToDelete) + "\\b");
              Matcher matcher = pattern.matcher(value);
              if (matcher.find()) {
                String restValue = value.substring(0, matcher.start()) + value.substring(matcher.end());
                VIRTUAL_DATA_SAVER.put(AchievementDataBase.this.convertUUID(player.getUniqueId()), restValue);
              }
            }else return;
          }catch (SQLException exception) {
            exception.printStackTrace();
            SaveError.saveError(Main.main(), exception, "AchievementDataBase-DeleteUtils-requestData-An_error_occurred_by_deleting_the_achievement_id=SQLException");
            return;
          }
        }
      }.runTaskAsynchronously(Main.main());
      return true;
    }


    public void updateDataValue(@NotNull Player player, String newValues){
      new BukkitRunnable(){
        @Override
        public void run() {
          try {
            Connection connection = AchievementDataBase.this.dataSource.getConnection();
            PreparedStatement statement =
              connection.prepareStatement("UPDATE " + AchievementDataBase.this.table + " SET achievement_id = ? WHERE uuid = ?");
            statement.setString(1, newValues);
            statement.setString(2, AchievementDataBase.this.convertUUID(player.getUniqueId()));
            statement.executeUpdate();
          }catch (SQLException exception) {
            exception.printStackTrace();
            SaveError.saveError(Main.main(), exception, "AchievementDataBase-DeleteUtils-updateDataValue-An_error_occurred_by_updating_the_achievement_ids=SQLException");
          }
        }
      }.runTaskAsynchronously(Main.main());
    }
  }

  @Nullable
  public Connection connection() throws SQLException {
    return dataSource.getConnection();
  }

}
