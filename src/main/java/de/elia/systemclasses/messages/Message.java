package de.elia.systemclasses.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import static de.elia.systemclasses.messages.prefix.Prefix.prefix;

//This class has message methods with the prefix
public class Message {
  private static final MiniMessage miniMessage = MiniMessage.miniMessage();

  public static void broadcastWithPrefix(String message) {
    Bukkit.broadcast(miniMessage.deserialize((prefix() + message)));
  }

  public static void broadcastWithPrefix(Component message) {
    Bukkit.broadcast(prefix().append(message));
  }

  public static void messageWithPrefix(@NotNull Player player, String message) {
    player.sendMessage(miniMessage.deserialize((prefix() + message)));
  }

  public static void messageWithPrefix(@NotNull Player player, Component message) {
    player.sendMessage(prefix().append(message));
  }

  public static void messageWithPrefix(@NotNull CommandSender sender, String message) {
    sender.sendMessage(miniMessage.deserialize((prefix() + message)));
  }
}
