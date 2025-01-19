package de.elia.systemclasses.register.listener;

import de.elia.achivementssystem.listeners.bossfight.AchievementBossFightListener;
import de.elia.bossfightcreator.listeners.bossfightconnections.join.BossfightJoinListener;
import de.elia.bossfightcreator.listeners.bossfightconnections.quit.BossFightQuitListener;
import de.elia.bossfightcreator.listeners.join.JoinListener;
import de.elia.bossfightcreator.listeners.login.LoginListener;
import de.elia.bossfightcreator.executer.GameExecuter;
import de.elia.items.listener.join.RecipeListener;
import de.elia.items.listener.magicbook.MagicBookListener;
import de.elia.items.listener.magicstick.MagicStickListener;
import de.elia.soulboss.entitys.zombie.listener.DropListener;
import de.elia.soulboss.entitys.zombie.ZombieBoss.ZombieBossListener;
import de.elia.soulboss.entitys.zombie.listener.AttackListener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

//This class registers all listeners
public class ListenerRegister {

  public static final Set<Listener> LISTENERS = new HashSet<>();

  static {
    LISTENERS.add(new AchievementBossFightListener());
    LISTENERS.add(new LoginListener());
    LISTENERS.add(new GameExecuter(Main.main()));
    LISTENERS.add(new RecipeListener());
    LISTENERS.add(new MagicBookListener(Main.main()));
    LISTENERS.add(new DropListener());
    LISTENERS.add(new ZombieBossListener());
    LISTENERS.add(new AttackListener());
    LISTENERS.add(new JoinListener());
    LISTENERS.add(new BossfightJoinListener());
    LISTENERS.add(new BossFightQuitListener());
    LISTENERS.add(new MagicStickListener(Main.main()));
  }

  public static void registerListener(@NotNull PluginManager pluginManager, @NotNull JavaPlugin plugin){
    LISTENERS.forEach(listener -> pluginManager.registerEvents(listener, plugin));
  }

  public static void registerListener(){
    registerListener(Main.main().getServer().getPluginManager(), Main.main());
  }

}
