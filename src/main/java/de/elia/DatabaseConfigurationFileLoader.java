package de.elia;

import de.elia.api.configuration.SoulConfiguration;
import org.jetbrains.annotations.NotNull;

public class DatabaseConfigurationFileLoader {

  private static final SoulConfiguration DATABASE_CONFIG = new SoulConfiguration(Main.main(), "database/", "database.yml");

  public static void loadDatabaseConfigurationFile(){
    DATABASE_CONFIG.copyDefaults(true);
    DATABASE_CONFIG.addDefault("host", "localhost");
    DATABASE_CONFIG.addDefault("port", 3306);
    DATABASE_CONFIG.addDefault("user", "dev");
    DATABASE_CONFIG.addDefault("password", "dev001");
    DATABASE_CONFIG.addDefault("database", "achievement");
    DATABASE_CONFIG.addDefault("version", 0);
    DATABASE_CONFIG.setComments("version", "NICHT ÄNDERN OHNE ABSPRACHE VON ELIA!!!");
    DATABASE_CONFIG.save();
  }

  @NotNull
  public static SoulConfiguration databaseConfig() {
    return DATABASE_CONFIG;
  }
}
