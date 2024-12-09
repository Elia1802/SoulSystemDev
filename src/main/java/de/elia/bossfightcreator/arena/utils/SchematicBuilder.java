package de.elia.bossfightcreator.arena.utils;

import de.elia.api.logging.error.SaveError;

import de.elia.bossfightcreator.BossFightCreatorMain;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import org.bukkit.Location;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//Coding by Sinan and Elia
//Load and paste the schematics in the Bossfightworld

/**
 * @author Sinan, Elia
 * Load and paste the schematics in the Bossfightworld
 */
public class SchematicBuilder {

  /**
   * Gets the schematic file from the given path
   * @author Sinan, Elia
   * @param path Requires the path of the schematic file
   * @param schematicName Requires the name of the schematic file
   * @return The schematic file (if it exists)
   */
  @NotNull
  public Clipboard schematic(@NotNull File path, @NotNull String schematicName) {
    Clipboard clipboard;
    File arenaFile = new File(path, schematicName + ".schem");
    ClipboardFormat format = ClipboardFormats.findByFile(arenaFile);
    try (ClipboardReader reader = format.getReader(new FileInputStream(arenaFile))) {
      return clipboard = reader.read();
    }catch (IOException exception) {
      //Start Elia's code
      //Save the error in a .txt file
      SaveError.saveError(BossFightCreatorMain.bossFightCreator().main(), exception, "SchematicBuilder-schematic-An_error_occurred_by_reading_a_schematic_file=IOException");
      exception.printStackTrace();
      return null;
      //End Elia's code
    }
  }

  /**
   * Load the schemetic in the world
   * @author Sinan, Elia
   * @param location Needs the location where the schematic should be loaded
   * @param clipboard Needs the clipboard where the schematic is loaded
   * @throws WorldEditException
   */
  public void pasteSchematic(@NotNull Location location, @NotNull Clipboard clipboard) throws WorldEditException {
    EditSession session = WorldEdit.getInstance().newEditSession(new BukkitWorld(location.getWorld()));
    Operation operation =
      new ClipboardHolder(clipboard)
        .createPaste(session)
        .to(BlockVector3
          .at(location
            .x(), location.y(), location.z())).ignoreAirBlocks(false).copyEntities(false)
        .build();
    Operations.complete(operation);
    session.close();
    //Remove the clipboard (coded of Elia)
    clipboard.close();
  }
}
