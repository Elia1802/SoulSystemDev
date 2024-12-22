package de.elia;

import org.bukkit.Server;
import org.bukkit.command.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegister {

    private static final Map<String, Command> COMMANDS = new HashMap<>();

    public static void registerCommands(Server server) {
        COMMANDS.forEach((s, command) -> server.getCommandMap().register(s, "SoulLeveling", command));
    }

    static {
        COMMANDS.put("points", new PointCommand());
    }

}
