package de.elia.systemclasses;

import de.elia.api.logging.PluginLogger;

import de.elia.achivementssystem.AchievementMain;
import de.elia.bossfightcreator.BossFightCreatorMain;
import de.elia.dungeons.DungeonsMain;
import de.elia.items.ItemMain;
import de.elia.party.PartyMain;
import de.elia.soulboss.SoulBoss;
import de.elia.systemclasses.plugin.SoulBossSystemMain;

//Here all important instances saved
public class Instances {

  public static final AchievementMain ACHIEVEMENT_MAIN = new AchievementMain();
  public static final BossFightCreatorMain BOSS_FIGHT_CREATOR = new BossFightCreatorMain();
  public static final DungeonsMain DUNGEONS_MAIN = new DungeonsMain();
  public static final ItemMain ITEM_MAIN = new ItemMain();
  public static final SoulBoss SOUL_BOSS = new SoulBoss();
  public static final SoulBossSystemMain SOUL_BOSS_SYSTEM = new SoulBossSystemMain();
  public static final PartyMain PARTY_MAIN = new PartyMain();
  public static final PluginLogger MAIN_LOGGER = new PluginLogger("SoulBossSystem -> Main");
  public static final PluginLogger ACHIEVEMENT_LOGGER = new PluginLogger("SoulBossSystem -> Achievement");
  public static final PluginLogger THE_ZEPSER_API_LOGGER = new PluginLogger("SoulBossSystem -> TheZepserAPI");
  public static final PluginLogger BOSS_FIGHT_CREATOR_LOGGER = new PluginLogger("SoulBossSystem -> BossFightCreator");
  public static final PluginLogger DUNGEONS_LOGGER = new PluginLogger("SoulBossSystem -> Dungeons");
  public static final PluginLogger DUNGEONS_LOGGER_WORLD = new PluginLogger("SoulBossSystem -> Dungeons -> World");
  public static final PluginLogger ITEM_LOGGER = new PluginLogger("SoulBossSystem -> Item");
  public static final PluginLogger SOUL_BOSS_LOGGER = new PluginLogger("SoulBossSystem -> SoulBoss");
  public static final PluginLogger SOUL_BOSS_SYSTEM_LOGGER = new PluginLogger("SoulBossSystem -> SoulBossSystem");
  public static final PluginLogger PARTY_LOGGER = new PluginLogger("SoulBossSystem -> Party");
}
