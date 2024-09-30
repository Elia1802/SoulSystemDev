package de.elia.systemclasses.register.utils;

import de.elia.soulboss.SoulBoss;
import de.elia.soulboss.block.BreakBlock;
import de.elia.systemclasses.keys.NameSpacedKeys;

import org.bukkit.plugin.Plugin;

//This is the Register class to register all Utils or other things.
@Deprecated//fix the error, this does nothing
public class UtilsLoader {
  
  public static void loadUtils(Plugin plugin){
    var log = SoulBoss.soulBoss().soulBossLogger();
    log.logInfo("load BreakTask...");
    //new BreakBlock(plugin).breakTask(NameSpacedKeys.ZOMBIE_KEY.key());
    log.logInfo("BreakTask loaded!");
  }

}
