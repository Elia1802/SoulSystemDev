package de.elia.bossfightcreator.arena;

import de.elia.api.logging.error.SaveError;

import de.elia.Main;
import de.elia.bossfightcreator.BossFightCreatorMain;
import de.elia.bossfightcreator.arena.utils.SchematicBuilder;

import com.sk89q.worldedit.WorldEditException;

import org.jetbrains.annotations.NotNull;

/**
 * This class rebuilds an arena
 */
public class ArenaReBuilder {

  /**
   * Rebuild a Arena.
   * @param arena the arena to reload
   */
  public static void reBuildArena(@NotNull Arena arena) {
    BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo(arena.getName() + " is being build at " + arena.getLocation() + "!");
    try {
      arena.setState(ArenaState.LOADING);
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("Paste the schematic of the arena");
      new SchematicBuilder().pasteSchematic(arena.getLocation(), new SchematicBuilder().schematic(ArenaHandler.FILE_PATH, arena.getName()));
      arena.setState(ArenaState.FREE);
      BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo("The arena " + arena.getArenaID() + " is free!");
    } catch (WorldEditException exception) {
      SaveError.saveError(Main.main(), exception, "Arena-buildArena-An_error_occurred_by_build_a_arena=WorldEditException");
      exception.printStackTrace();
      arena.setState(ArenaState.ERROR_BUILDING);
    }
    BossFightCreatorMain.bossFightCreator().bossFightCreatorLogger().logInfo(arena.getName() + " was build at " + arena.getLocation() + "! And now the State is " + arena.getState());
  }

}
