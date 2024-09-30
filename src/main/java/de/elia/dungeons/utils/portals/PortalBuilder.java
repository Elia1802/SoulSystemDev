package de.elia.dungeons.utils.portals;

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
import de.elia.dungeons.utils.sys.Portals;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import static de.elia.dungeons.utils.sys.PortalState.ERROR;
import static de.elia.dungeons.utils.sys.PortalState.LOADED;
import static de.elia.dungeons.utils.sys.PortalState.LOADING;

public class PortalBuilder {

  private final PluginLogger logger = DungeonsMain.dungeonsMain().dungeonLogger();
  public final Random random = new Random();

  public final File path;
  private final Portals portal;
  private final World world;

  //min/max cords
  private final double minX;
  private final double maxX;
  private final double minY;
  private final double maxY;
  private final double minZ;
  private final double maxZ;

  private BlockVector3 lastVector;

  public PortalBuilder(@NotNull File path, @NotNull Portals portal, @NotNull World world, double minX, double maxX, double minY, double maxY, double minZ, double maxZ){
    this.path = path;
    this.portal = portal;
    this.world = world;
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
    this.minZ = minZ;
    this.maxZ = maxZ;
  }

  public void build(){
    this.portal.state(LOADING);
    final Clipboard clipboard;
    File buildFile = new File(this.path, this.portal.name() + ".schem");
    if (!buildFile.exists()) {
      this.logger.logError("buildFile (Path:" + this.path + ", Name: " + this.portal.portalName() + ".schem) is null!");
      this.portal.state(ERROR);
      return;
    }
    ClipboardFormat format = ClipboardFormats.findByFile(buildFile);
    try {
      ClipboardReader reader = format.getReader(new FileInputStream(buildFile));
      clipboard = reader.read();
      reader.close();
    }catch (IOException exception) {
      exception.printStackTrace();
      SaveError.saveError(DungeonsMain.dungeonsMain().main(), exception, "PortalBuilder-build-failed_to_reading__buildFile=IOException");
      this.portal.state(ERROR);
      return;
    }
    if (clipboard == null) {
      this.logger.logError("Clipboard is null!");
      this.portal.state(ERROR);
      return;
    }
    EditSession buildSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(this.world));
    double randomX = random.nextDouble(this.minX, this.maxX);
    double randomY = random.nextDouble(this.minY, this.maxY);
    double randomZ = random.nextDouble(this.minZ, this.maxZ);
    var vector = BlockVector3.at(randomX, randomY, randomZ);
    this.lastVector = vector;
    Operation buildOperaion =
      new ClipboardHolder(clipboard)
        .createPaste(buildSession)
        .to(vector)
        .ignoreAirBlocks(false)
        .copyEntities(false)
        .build();
    Operations.complete(buildOperaion);
    buildSession.close();
    this.portal.state(LOADED);
  }

  public void reBuild() {

  }

}
