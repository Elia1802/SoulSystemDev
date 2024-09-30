package de.elia.dungeons.utils.dungeons;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import de.elia.api.logging.PluginLogger;
import de.elia.api.logging.error.SaveError;
import de.elia.dungeons.DungeonsMain;
import de.elia.dungeons.utils.sys.Dungeons;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static de.elia.dungeons.utils.sys.DungeonState.ERROR;
import static de.elia.dungeons.utils.sys.DungeonState.LOADED;
import static de.elia.dungeons.utils.sys.DungeonState.LOADING;

public class DungeonBuilder {

  private final PluginLogger logger = DungeonsMain.dungeonsMain().dungeonLogger();

  private final @NotNull Location location;
  private final @NotNull File path;
  private final @NotNull Dungeons dungeon;

  public DungeonBuilder(@NotNull Location location, @NotNull File path, @NotNull Dungeons dungeon){
    this.location = location;
    this.path = path;
    this.dungeon = dungeon;
  }

  public void build(){
    this.dungeon.state(LOADING);
    final Clipboard clipboard;
    File buildFile = new File(this.path, this.dungeon.dungeonName() + ".schem");
    if (!buildFile.exists()) {
      this.logger.logError("buildFile (Path:" + this.path + ", Name: " + this.dungeon.dungeonName() + ".schem) is null!");
      this.dungeon.state(ERROR);
      return;
    }
    ClipboardFormat format = ClipboardFormats.findByFile(buildFile);
    try {
      ClipboardReader reader = format.getReader(new FileInputStream(buildFile));
      clipboard = reader.read();
      reader.close();
    }catch (IOException exception) {
      SaveError.saveError(DungeonsMain.dungeonsMain().main(), exception, "DungeonBuilder-build-failed_to_reading__buildFile=IOException");
      exception.printStackTrace();
      this.dungeon.state(ERROR);
      return;
    }
    if (clipboard == null) {
      this.logger.logError("Clipboard is null!");
      this.dungeon.state(ERROR);
      return;
    }
    EditSession buildSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(this.location.getWorld()));
    Operation buildOperation =
      new ClipboardHolder(clipboard)
        .createPaste(buildSession)
        .to(BlockVector3
          .at(this.location.x(), this.location.y(), this.location.z()))
        .ignoreAirBlocks(false)
        .copyEntities(false)
        .build();
    Operations.complete(buildOperation);
    buildSession.close();
    this.dungeon.state(LOADED);
  }
}
