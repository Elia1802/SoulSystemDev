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

/**
 * This class is an Arena and has all important values for the arena.
 */
public class Arena {

  //Location of the arena
  private final Location location;
  //Name of the arena
  private final String name;
  //ID of the arena
  private final @NotNull String arenaID;
  //Mob type of the arena
  private final ArenaMobType mobType;
  //State of the arena
  private ArenaState state = ArenaState.UNINITIALIZED;
  private Game game;

  /**
   * Build the Arena
   * @param name Requires a name of the arena
   * @param location Requires a location of the arena
   * @param arenaID Requires an ID of the arena
   * @param mobType Requires a mob type of the arena
   */
  public Arena(@NotNull String name, @NotNull Location location, @NotNull String arenaID, ArenaMobType mobType) {
    this.name = name;
    this.location = location;
    this.arenaID = arenaID;
    this.mobType = mobType;
    this.buildArena();
  }

  /**
   * Build a new Arena
   */
  private void buildArena() {
    Instances.BOSS_FIGHT_CREATOR_LOGGER.logInfo(this.name + " is being build at " + this.location + "!");
    try {
      //Set state to loading
      this.state = ArenaState.LOADING;
      Instances.BOSS_FIGHT_CREATOR_LOGGER.logInfo("Paste the schematic of the arena");
      //Ne
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

  /**
   * Gets the location of the arena
   * @return a {@link Location} of the arena
   */
  @NotNull
  public Location getLocation() {
    return this.location;
  }

  /**
   * Gets the Name of the arena
   * @return a name as {@link String}
   */
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Gets the Name of the arena
   * @return an ID as {@link String}
   */
  @NotNull
  public String getArenaID() {
    return this.arenaID;
  }

  /**
   * Gets the Game of the Arena
   * @return the {@link Game} of this arena
   */
  @NotNull
  public Game getGame() {
    return this.game;
  }

  /**
   * Gets the state of the Arena
   * @return a {@link ArenaState}
   */
  @NotNull
  public ArenaState getState() {
    return this.state;
  }

  /**
   * Gets the mob type of the Arena
   * @return the mob type
   */
  @NotNull
  public ArenaMobType getMobType() {
    return this.mobType;
  }

  /**
   * Set a Game to this arena
   * @param game a specified {@link Game} for the arena
   */
  public void setGame(@NotNull final Game game) {
    this.game = game;
  }

  /**
   * Set a new state for this arena
   * @param state the new {@link ArenaState} for the arena
   */
  public void setState(@NotNull ArenaState state) {
    this.state = state;
  }

}


