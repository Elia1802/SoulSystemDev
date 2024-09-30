package de.elia.bossfightcreator.arena;

import de.elia.api.game.Game;
import de.elia.api.logging.error.SaveError;

import de.elia.Main;
import de.elia.bossfightcreator.arena.utils.SchematicBuilder;
import de.elia.systemclasses.Instances;

import com.sk89q.worldedit.WorldEditException;

import org.bukkit.Location;

import org.jetbrains.annotations.NotNull;

import java.io.File;

//This class is an Arena and has all important values for the arena.
public class Arena {

  private final Location location;
  private final String name;
  private final @NotNull String arenaID;
  private final ArenaMobType mobType;
  private ArenaState state = ArenaState.UNINITIALIZED;
  private Game zombieGame;

  //Build the Arena
  public Arena(@NotNull String name, @NotNull Location location, @NotNull String arenaID, ArenaMobType mobType) {
    this.name = name;
    this.location = location;
    this.arenaID = arenaID;
    this.mobType = mobType;
    this.buildArena();
  }

  //Build a new Arena
  private void buildArena() {
    Instances.BOSS_FIGHT_CREATOR_LOGGER.logInfo(this.name + " is being build at " + this.location + "!");
    try {
      this.state = ArenaState.LOADING;
      Instances.BOSS_FIGHT_CREATOR_LOGGER.logInfo("Paste the schematic of the arena");
      new SchematicBuilder().pasteSchematic(this.location, new SchematicBuilder().schematic(ArenaHandler.FILE_PATH, this.name));
      File arenaFile = new File(ArenaHandler.FILE_PATH, this.name + ".schem");
      Instances.BOSS_FIGHT_CREATOR_LOGGER.logInfo(arenaFile + "  !  " + arenaFile.exists());
      this.state = ArenaState.FREE;
      Instances.BOSS_FIGHT_CREATOR_LOGGER.logInfo("The arena " + this.arenaID + " is " + this.state);
    } catch (WorldEditException exception) {
      SaveError.saveError(Main.main(), exception, "Arena-buildArena-An_error_occurred_by_build_a_arena=WorldEditException");
      exception.printStackTrace();
      this.state = ArenaState.ERROR_BUILDING;
    }
    Instances.BOSS_FIGHT_CREATOR_LOGGER.logInfo(this.name + " was build at " + this.location + "! And now the State is " + this.state);
  }

  //Gets the location of the arena
  @NotNull
  public Location getLocation() {
    return this.location;
  }

  //Gets the Name of the arena
  @NotNull
  public String getName() {
    return this.name;
  }

  //Gets the ID of the Arena
  @NotNull
  public String getArenaID() {
    return this.arenaID;
  }

  //Gets the Game of the Arena
  @NotNull
  public Game getGame() {
    return this.zombieGame;
  }

  //Gets the state of the Arena
  @NotNull
  public ArenaState getState() {
    return this.state;
  }

  //Gets the mob type of the Arena
  @NotNull
  public ArenaMobType getMobType() {
    return mobType;
  }

  //Set a Game to this arena
  public void setGame(@NotNull final Game zombieGame) {
    this.zombieGame = zombieGame;
  }

  //Set a new state for this arena
  public void setState(@NotNull ArenaState state) {
    this.state = state;
  }

}


