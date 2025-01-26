package de.elia.party;

import de.elia.Main;
import de.elia.api.loader.SoulPlugin;
import de.elia.api.loader.exceptions.SoulPluginException;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

//This class is the main class of the party plugin
public class PartyMain implements SoulPlugin {

  private static PartyMain partyMain;

  //This methode loads this plugin.
  @Override
  public void enable(@NotNull JavaPlugin javaPlugin) {
    partyMain = this;
  }

  //This methode unloads this plugin.
  @Override
  public void disable(@NotNull JavaPlugin javaPlugin) {

  }

  //Gets this class.
  @NotNull
  public static PartyMain partyMain(){
    return partyMain;
  }

  //Gets the instance, of the main class, of this system.
  @NotNull
  public Main main(){
    return Main.main();
  }

}
