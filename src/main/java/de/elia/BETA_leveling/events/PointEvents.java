package de.elia.BETA_leveling.events;

import de.elia.api.achievements.Achievements;
import de.elia.api.events.achievement.AchievementGiveEvent;
import de.elia.api.messages.builder.MessageBuilder;
import de.elia.BETA_leveling.level.Point;
import de.elia.BETA_leveling.level.LevelUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.jetbrains.annotations.NotNull;

import static de.elia.api.messages.builder.MessageBuilder.gray;

public class PointEvents implements Listener {

    private final MessageBuilder message = new MessageBuilder();

    @EventHandler
    private void onAchievement(@NotNull AchievementGiveEvent event){
        Player player = event.getPlayer();
        Achievements triggeredAchievement = event.getAchievement();
        var achievement = Point.SOUL_BOSSFIGHT_ACHIEVEMMENT;
        if (triggeredAchievement == achievement.getObject()) {
            MessageBuilder.messageWithPrefix(player, gray(achievement.getCategory().getName()));
            LevelUtils.addPoints(player, achievement.getPoints());
        }

    }

    @EventHandler
    private void onStone(@NotNull EntityPickupItemEvent event){
        Entity entity = event.getEntity();
        Item item = event.getItem();
        if (entity instanceof Player player) {
            if (item.getItemStack().getType() == Material.STONE) {
                var stone = Point.STONE;
                message.messageWithPrefix(player, gray(stone.getCategory().getName()));
                LevelUtils.addPoints(player, stone.getPoints());
            }
        }
    }

    @EventHandler
    private void onDiamondOre(@NotNull EntityPickupItemEvent event){
        Entity entity = event.getEntity();
        Item item = event.getItem();
        if (entity instanceof Player player) {
            if (item.getItemStack().getType() == Material.DIAMOND_ORE) {
                var diamond_ore = Point.DIAMOND_ORE;
                message.messageWithPrefix(player, gray(diamond_ore.getCategory().getName()));
                LevelUtils.addPoints(player, diamond_ore.getPoints());
            }
        }
    }

    @EventHandler
    private void onGranite(@NotNull EntityPickupItemEvent event){
        Entity entity = event.getEntity();
        Item item = event.getItem();
        if (entity instanceof Player player) {
            if (item.getItemStack().getType() == Material.GRASS_BLOCK) {
                var granite = Point.GRANITE;
                message.messageWithPrefix(player, gray(granite.getCategory().getName()));
                LevelUtils.addPoints(player, granite.getPoints());
            }
        }
    }

    @EventHandler
    private void onWood(@NotNull EntityPickupItemEvent event){
        Entity entity = event.getEntity();
        Item item = event.getItem();
        if (entity instanceof Player player) {
            if (item.getItemStack().getType() == Material.BIRCH_WOOD) {
                var birch_wood = Point.DIORITE;
                message.messageWithPrefix(player, gray(birch_wood.getCategory().getName()));
                LevelUtils.addPoints(player, birch_wood.getPoints());
            }
        }
    }
}
