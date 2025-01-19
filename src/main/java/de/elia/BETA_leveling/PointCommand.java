package de.elia.BETA_leveling;

import de.elia.BETA_leveling.level.LevelUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PointCommand extends Command {

    public PointCommand(){
        this("points");
    }

    protected PointCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            System.out.println(LevelUtils.getPoints(player));
        }
        return false;
    }
}
