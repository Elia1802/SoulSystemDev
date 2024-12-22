package de.elia.level;

import de.elia.api.achievements.Achievements;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum Point {

    //Miner
    STONE(Material.STONE, Category.MINER, 0.25, Argument.ARG_MINER),
    GRANITE(Material.GRANITE, Category.MINER, 0.1, Argument.ARG_MINER),
    DIORITE(Material.DIORITE, Category.MINER, 0.11, Argument.ARG_MINER),
    ANDESITE(Material.ANDESITE, Category.MINER, 0.12, Argument.ARG_MINER),
    DEEPSLATE(Material.DEEPSLATE, Category.MINER, 0.5, Argument.ARG_MINER),
    TUFF(Material.TUFF, Category.MINER, 0.13, Argument.ARG_MINER),
    COAL_ORE(Material.COAL_ORE, Category.MINER, 1.0, Argument.ARG_MINER),
    COPPER_ORE(Material.COPPER_ORE, Category.MINER, 1.25, Argument.ARG_MINER),
    IRON_ORE(Material.IRON_ORE, Category.MINER, 2.0, Argument.ARG_MINER),
    GOLD_ORE(Material.GOLD_ORE, Category.MINER, 1.5, Argument.ARG_MINER),
    REDSTONE_ORE(Material.REDSTONE_ORE, Category.MINER, 1.75, Argument.ARG_MINER),
    LAPIS_LAZULI_ORE(Material.LAPIS_ORE, Category.MINER, 2.25, Argument.ARG_MINER),
    DIAMOND_ORE(Material.DIAMOND_ORE, Category.MINER, 2.5, Argument.ARG_MINER),
    EMERALD_ORE(Material.EMERALD_ORE, Category.MINER, 3.0, Argument.ARG_MINER),
    DEEPSLATE_COAL_ORE(Material.DEEPSLATE_COAL_ORE, Category.MINER, 1.5, Argument.ARG_MINER),
    DEEPSLATE_COPPER_ORE(Material.DEEPSLATE_COPPER_ORE, Category.MINER, 2.5, Argument.ARG_MINER),
    DEEPSLATE_IRON_ORE(Material.DEEPSLATE_IRON_ORE, Category.MINER, 4.0, Argument.ARG_MINER),
    DEEPSLATE_GOLD_ORE(Material.DEEPSLATE_GOLD_ORE, Category.MINER, 3.0, Argument.ARG_MINER),
    DEEPSLATE_REDSTONE_ORE(Material.DEEPSLATE_REDSTONE_ORE, Category.MINER, 3.5, Argument.ARG_MINER),
    DEEPSLATE_LAPIS_LAZULI_ORE(Material.DEEPSLATE_LAPIS_ORE, Category.MINER, 4.5, Argument.ARG_MINER),
    DEEPSLATE_DIAMOND_ORE(Material.DEEPSLATE_DIAMOND_ORE, Category.MINER, 5, Argument.ARG_MINER),
    DEEPSLATE_EMERALD_ORE(Material.DEEPSLATE_EMERALD_ORE, Category.MINER, 100.0, Argument.ARG_MINER),
    //Explorer
    PLAYER_EXPLORE(OtherObjects.MOVE, Category.EXPLORER, 0.05, Argument.ARG_EXPLORER),
    //Hunter
    PLAYER_HIT_PLAYER(EntityType.PLAYER, Category.HUNTER, 0.1, Argument.ARG_HUNTER),
    PLAYER_HIT_ZOMBIER_VILLAGER(EntityType.ZOMBIE_VILLAGER, Category.HUNTER, 0.5, Argument.ARG_HUNTER),
    PLAYER_HIT_SKELETON_HORSE(EntityType.SKELETON_HORSE, Category.HUNTER, 2.0, Argument.ARG_HUNTER),
    PLAYER_HIT_EVOKER(EntityType.EVOKER, Category.HUNTER, 4.5, Argument.ARG_HUNTER),
    PLAYER_HIT_VEX(EntityType.VEX, Category.HUNTER, 5.0, Argument.ARG_HUNTER),
    PLAYER_HIT_VINDICATOR(EntityType.VINDICATOR, Category.HUNTER, 4.0, Argument.ARG_HUNTER),
    PLAYER_HIT_CREEPER(EntityType.CREEPER, Category.HUNTER, 1.5, Argument.ARG_HUNTER),
    PLAYER_HIT_SKELETON(EntityType.SKELETON, Category.HUNTER, 1.0, Argument.ARG_HUNTER),
    PLAYER_HIT_SPIDER(EntityType.SPIDER, Category.HUNTER, 0.75, Argument.ARG_HUNTER),
    PLAYER_HIT_SLIME(EntityType.SLIME, Category.HUNTER, 2.0, Argument.ARG_HUNTER),
    PLAYER_HIT_CAVE_SPIDER(EntityType.CAVE_SPIDER, Category.HUNTER, 1.25, Argument.ARG_HUNTER),
    PLAYER_HIT_SILVERFISH(EntityType.SILVERFISH, Category.HUNTER, 0.2, Argument.ARG_HUNTER),
    PLAYER_HIT_WITHER(EntityType.WITHER, Category.HUNTER, 20.0, Argument.ARG_HUNTER),
    PLAYER_HIT_WITCH(EntityType.WITCH, Category.HUNTER, 2.5, Argument.ARG_HUNTER),
    PLAYER_HIT_GUARDIAN(EntityType.GUARDIAN, Category.HUNTER, 1.25, Argument.ARG_HUNTER),
    PLAYER_HIT_PIG(EntityType.PIG, Category.HUNTER, 0.06, Argument.ARG_HUNTER),
    PLAYER_HIT_SHEEP(EntityType.SHEEP, Category.HUNTER, 0.05, Argument.ARG_HUNTER),
    PLAYER_HIT_COW(EntityType.COW, Category.HUNTER, 0.05, Argument.ARG_HUNTER),
    PLAYER_HIT_CHICKEN(EntityType.CHICKEN, Category.HUNTER, 0.05, Argument.ARG_HUNTER),
    PLAYER_HIT_SQUID(EntityType.SQUID, Category.HUNTER, 0.05, Argument.ARG_HUNTER),
    PLAYER_HIT_WOLF(EntityType.WOLF, Category.HUNTER, 0.01, Argument.ARG_HUNTER),
    //Achievement
    SOUL_BOSSFIGHT_ACHIEVEMMENT(Achievements.BOSSFIGHT, Category.ACHIEVEMENT, 2, Argument.ARG_ACHIEVEMENT),
    SOUL_BOSSFIGHT_ZOMBIE(Achievements.BOSSFIGHT_ZOMBIE, Category.ACHIEVEMENT, 15, Argument.ARG_ACHIEVEMENT),
    SOUL_BOSSFIGHT_CREEPER(Achievements.BOSSFIGHT_ZOMBIE_END, Category.ACHIEVEMENT, 20, Argument.ARG_ACHIEVEMENT);

    private final Object object;
    private final Category category;
    private final double points;
    private final String arg;
    private static final List<Point> ALL_POINTS = Arrays.stream(Point.values()).toList();
    private static final List<Point> MINER = Arrays.stream(Point.values()).filter(point -> Objects.equals(point.getArg(), Argument.ARG_MINER)).toList();

    Point(Object object, Category category, double points, String arg){
        this.object = object;
        this.category = category;
        this.points = points;
        this.arg = arg;
    }

    @Nullable
    public Object getObject() {
        return this.object;
    }

    @NotNull
    public Category getCategory(){
        return this.category;
    }

    @NotNull
    public Double getPoints(){
        return this.points;
    }

    public String getArg() {
        return this.arg;
    }

    public enum OtherObjects {
        MOVE
    }

    public static class Argument {
        public static final String ARG_MINER = "arg_miner";
        public static final String ARG_EXPLORER = "arg_explorer";
        public static final String ARG_HUNTER = "arg_hunter";
        public static final String ARG_ACHIEVEMENT = "arg_achievement";
    }
}
