package de.elia.overrides;

import de.elia.api.messages.builder.MessageBuilder;
import de.elia.api.messages.prefix.PrefixClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Deprecated//IDK
public class Message extends MessageBuilder {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final PrefixClass prefix = new PrefixClass();

    public void broadcastWithPrefix(Component message) {
        Bukkit.broadcast(this.prefix.prefix().append(message));
    }

    public void broadcastWithPrefix(String message) {
        Component component = this.prefix.prefix();
        Bukkit.broadcast(component.append(miniMessage.deserialize(message)));
    }

    public void broadcastWithPrefix(@NotNull Player player, String permission, Component message) {
        if (player.hasPermission(permission)) {
            Bukkit.broadcast(this.prefix.prefix().append(message));
        }
    }

    public void broadcastWithPrefix(@NotNull Player player, String permission, String message) {
        if (player.hasPermission(permission)) {
            Component component = this.prefix.prefix();
            Bukkit.broadcast(component.append(miniMessage.deserialize(message)));
        }
    }

    public void broadcastWithPrefix(Component message, String permission) {
        Bukkit.broadcast(this.prefix.prefix().append(message), permission);
    }

    public void broadcastWithPrefix(String message, String permission) {
        Component component = this.prefix.prefix();
        Bukkit.broadcast(component.append(miniMessage.deserialize(message)), permission);
    }

    public void messageWithPrefix(@NotNull Player player, Component message) {
        player.sendMessage(this.prefix.prefix().append(message));
    }

    public void messageWithPrefix(@NotNull Player player, String message) {
        Component component = this.prefix.prefix();
        player.sendMessage(component.append(miniMessage.deserialize(message)));
    }

    public void messageWithPrefix(@NotNull CommandSender sender, Component message) {
        sender.sendMessage(this.prefix.prefix().append(message));
    }

    public void messageWithPrefix(@NotNull CommandSender sender, String message) {
        Component component = this.prefix.prefix();
        sender.sendMessage(component.append(miniMessage.deserialize(message)));
    }

}
