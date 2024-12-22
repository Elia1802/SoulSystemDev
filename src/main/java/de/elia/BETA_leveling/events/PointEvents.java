package de.elia.events;

import de.elia.api.achievements.Achievements;
import de.elia.api.events.achievement.AchievementGiveEvent;
import de.elia.api.messages.builder.MessageBuilder;
import de.elia.level.Point;
import de.elia.level.LevelUtils;
import de.elia.overrides.Message;
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

    private final Message message = new Message();

    @EventHandler
    private void onAchievement(@NotNull AchievementGiveEvent event){
        Player player = event.getPlayer();
        Achievements triggeredAchievement = event.getAchievement();
        var achievement = Ac.BOSSFIGHT_ACHIEVEMENT;
        if (triggeredAchievement == achievement.getObject()) {
            message.messageWithPrefix(player, gray(achievement.getCategory().getName()));
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
    private void onGrassBlock(@NotNull EntityPickupItemEvent event){
        Entity entity = event.getEntity();
        Item item = event.getItem();
        if (entity instanceof Player player) {
            if (item.getItemStack().getType() == Material.GRASS_BLOCK) {
                var grass_block = Point.GRASS_BLOCK;
                message.messageWithPrefix(player, gray(grass_block.getCategory().getName()));
                LevelUtils.addPoints(player, grass_block.getPoints());
            }
        }
    }

    @EventHandler
    private void onWood(@NotNull EntityPickupItemEvent event){
        Entity entity = event.getEntity();
        Item item = event.getItem();
        if (entity instanceof Player player) {
            if (item.getItemStack().getType() == Material.BIRCH_WOOD) {
                var birch_wood = Point.BIRCH_WOOD;
                message.messageWithPrefix(player, gray(birch_wood.getCategory().getName()));
                LevelUtils.addPoints(player, birch_wood.getPoints());
            }
        }
    }
}
