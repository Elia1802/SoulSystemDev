package de.elia.systemclasses.plugin.commands.plugin;

import de.elia.api.logging.PluginLogger;

import de.elia.systemclasses.plugin.SoulBossSystemMain;
import de.elia.systemclasses.plugin.configuation.SoulBossSystemConfigurationLoader;
import de.elia.systemclasses.keys.NameSpacedKeys;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.elia.api.messages.builder.MessageBuilder.aqua;
import static de.elia.api.messages.builder.MessageBuilder.gold;
import static de.elia.api.messages.builder.MessageBuilder.gray;
import static de.elia.api.messages.builder.MessageBuilder.red;
import static de.elia.systemclasses.messages.Message.broadcastWithPrefix;
import static de.elia.systemclasses.messages.Message.messageWithPrefix;

//This is a command for the help of the soulboss system plugin
public class SoulBossSystemCommand extends Command {

  private int time = 10;

  public SoulBossSystemCommand() {
    this("soulbosssystem", "All information about this Plugin and all functions to reload or restart this plugin", "Use /soulbosssystem", Arrays.asList("sbs", "soulbosssys"));
  }

  public SoulBossSystemCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }

  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    PluginLogger logger = SoulBossSystemMain.soulBossSystemMain().soulBossSystemLogger();
    if (sender instanceof Player player) {
      if (player.hasPermission("soulbosssystem.plugin")) {
        if (args.length == 1) {
          if (!args[0].equalsIgnoreCase("help")) {
            return false;
          }
          messageWithPrefix(player, gray("Mögliche Commands sind: "));
          messageWithPrefix(player, aqua("/soulbosssystem help"));
          messageWithPrefix(player, aqua("/soulbosssystem controls stop plugin/fights"));
          messageWithPrefix(player, aqua("/soulbosssystem controls reload plugin/files"));
          messageWithPrefix(player, aqua("/soulbosssystem information version plugin"));
          messageWithPrefix(player, aqua("/soulbosssystem information version api worldguard/soullibrary"));
          messageWithPrefix(player, aqua("/soulbosssystem information description/authors"));
          return true;
        }
        if (args.length == 2) {
          if (!args[0].equalsIgnoreCase("information")) {
            return false;
          }
          if (args[1].equalsIgnoreCase("description")) {
            messageWithPrefix(player, gray(SoulBossSystemMain.soulBossSystemMain().main().getPluginMeta().getDescription()));
            return true;
          }
          if (!args[1].equalsIgnoreCase("authors")) {
            return false;
          }
          messageWithPrefix(player, gray("Die Authoren sind "));
          SoulBossSystemMain.soulBossSystemMain().main().getPluginMeta().getAuthors().forEach(author -> messageWithPrefix(player, gray("- ").append(aqua(author))));
          return true;
        }
        if (args.length == 3) {
          if (args[0].equalsIgnoreCase("controls")) {
            if (args[1].equalsIgnoreCase("stop")) {
              if (args[2].equalsIgnoreCase("fights")) {
                broadcastWithPrefix(red("Das Teammitglied ").append(aqua(player.getName())).append(gray(" löscht alle BossFights!")));
                new BukkitRunnable(){
                  @Override
                  public void run() {
                    --SoulBossSystemCommand.this.time;
                    if (SoulBossSystemCommand.this.time == 0) {
                      Bukkit.getWorld("world_bossfight").getEntities().forEach(boss -> {
                        if (boss.getPersistentDataContainer().has(NameSpacedKeys.ZOMBIE_KEY.key())) {
                          boss.remove();
                        }
                      });
                    }
                  }
                }.runTaskTimer(SoulBossSystemMain.soulBossSystemMain().main(), 0L, 200L);
                return true;
              }
              if (args[2].equalsIgnoreCase("plugin")) {
                messageWithPrefix(player, gold("Das Plugin wird beendet"));
                Bukkit.getWorld("world_bossfight").getPlayers().forEach(playerInWorld -> playerInWorld.teleport(Bukkit.getWorld("world").getSpawnLocation()));
                Bukkit.getWorld("world_bossfight").getEntities().forEach(boss -> {
                  if (boss.getPersistentDataContainer().has(NameSpacedKeys.ZOMBIE_KEY.key())) {
                    boss.remove();
                    logger.logInfo("The Boss " + boss.getName() + "/" + boss.getUniqueId() + " was removed!");
                  }
                });
                SoulBossSystemMain.soulBossSystemMain().main().disable();
                return true;
              }
              messageWithPrefix(player, red("/soulbosssystem controls stop plugin/fights"));
              return false;
            }
            if (args[1].equalsIgnoreCase("reload")) {
              if (args[2].equalsIgnoreCase("files")) {
                SoulBossSystemConfigurationLoader.reload(SoulBossSystemMain.soulBossSystemMain().main());
                return true;
              }
              if (args[2].equalsIgnoreCase("plugin")) {
                SoulBossSystemMain.soulBossSystemMain().main().onReload();
                return true;
              }
              messageWithPrefix(player, red("/soulbosssystem controls reload plugin/config"));
              return false;
            }
            messageWithPrefix(player, red("/soulbosssystem controls stop"));
            messageWithPrefix(player, red("/soulbosssystem controls reload"));
            return false;
          }
          if (!(args[0].equalsIgnoreCase("information") && args[1].equalsIgnoreCase("version") && args[2].equalsIgnoreCase("plugin"))) {
            return false;
          }
          messageWithPrefix(player, gray("Die Version ist ").append(aqua(SoulBossSystemMain.soulBossSystemMain().main().getPluginMeta().getVersion())));
          return true;
        }
        if (args.length != 4) {
          return false;
        }
        if (args[0].equalsIgnoreCase("information")) {
          if (args[1].equalsIgnoreCase("version")) {
            if (args[2].equalsIgnoreCase("api")) {
              if (args[3].equalsIgnoreCase("worlguard")) {
                messageWithPrefix(player, gray("Die Version ist ").append(aqua(SoulBossSystemMain.soulBossSystemMain().main().worldEditPlugin().getPluginMeta().getVersion())));
                return true;
              }
              if (args[3].equalsIgnoreCase("soullibrary")) {
                messageWithPrefix(player, gray("Die Version ist ").append(aqua(SoulBossSystemMain.soulBossSystemMain().main().soulLibraryMain().getPluginMeta().getVersion())));
                return true;
              }
              messageWithPrefix(player, red("/soulbosssystem information version api thezepserapi"));
              messageWithPrefix(player, red("/soulbosssystem information version api worlguard"));
              messageWithPrefix(player, red("/soulbosssystem information version api soullibrary"));
              return false;
            }
            messageWithPrefix(player, red("/soulbosssystem information version api"));
            messageWithPrefix(player, red("/soulbosssystem information version plugin"));
            return false;
          }
          messageWithPrefix(player, red("/soulbosssystem information authors"));
          messageWithPrefix(player, red("/soulbosssystem information version"));
          messageWithPrefix(player, red("/soulbosssystem information description"));
          return false;
        }
        messageWithPrefix(player, red("/soulbosssystem help"));
        messageWithPrefix(player, red("/soulbosssystem controls"));
        messageWithPrefix(player, red("/soulbosssystem information"));
        return false;
      }
      messageWithPrefix(player, red("Du hast keine Berechtigung für diesen Command!"));
      return false;
    }
    logger.logWarning("You have to be a Player!");
    return false;
  }

  @NotNull
  public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
    ArrayList<String> tab1 = new ArrayList<>();
    ArrayList<String> tab2 = new ArrayList<>();
    ArrayList<String> tab3 = new ArrayList<>();
    ArrayList<String> tab4 = new ArrayList<>();
    if (args.length == 1) {
      tab1.add("help");
      tab1.add("controls");
      tab1.add("information");
      return tab1;
    }
    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("controls")) {
        tab2.add("stop");
        tab2.add("reload");
        return tab2;
      }
      if (args[0].equalsIgnoreCase("information")) {
        tab2.add("version");
        tab2.add("description");
        tab2.add("authors");
        return tab2;
      }
    } else if (args.length == 3) {
      if (args[1].equalsIgnoreCase("stop")) {
        tab3.add("fights");
        tab3.add("plugin");
        return tab3;
      }
      if (args[1].equalsIgnoreCase("reload")) {
        tab3.add("files");
        tab3.add("plugin");
        return tab3;
      }
      if (args[1].equalsIgnoreCase("version")) {
        tab3.add("api");
        tab3.add("plugin");
        return tab3;
      }
    } else if (args.length == 4 && args[2].equalsIgnoreCase("api")) {
      tab4.add("worlguard");
      tab4.add("soullibrary");
      return tab4;
    }
    return null;
  }
}
