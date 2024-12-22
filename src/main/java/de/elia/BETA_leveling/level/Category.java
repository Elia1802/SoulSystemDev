package de.elia.level;

import org.jetbrains.annotations.NotNull;

public enum Category {

    MINER("miner"),
    EXPLORER("explorer"),
    HUNTER("hunter"),
    ACHIEVEMENT("achievement"),
    RANGER("ranger"),
    NETHER("nether"),
    END("end"),
    OTHERS("others");

    private final String name;

    Category(final @NotNull String name){
        this.name = name;
    }

    @NotNull
    public final String getName(){
        return this.name;
    }

}
