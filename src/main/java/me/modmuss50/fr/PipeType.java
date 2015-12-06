package me.modmuss50.fr;

import net.minecraft.util.IStringSerializable;

public enum PipeType implements IStringSerializable {

    STONE(64, "Stone", "minecraft:blocks/stone"),
    IRON(128, "Iron", "minecraft:blocks/iron_block"),
    GOLD(256, "Gold", "minecraft:blocks/gold_block"),
    DIAMOND(1024, "Diamond", "minecraft:blocks/diamond_block");

    PipeType(int maxRF, String friendlyName, String textureName) {
        this.maxRF = maxRF;
        this.friendlyName = friendlyName;
        this.textureName = textureName;
    }

    int maxRF;
    public String friendlyName;
    public String textureName;

    @Override
    public String getName() {
        return friendlyName;
    }

    public int getMaxRF() {
        return maxRF;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getTextureName() {
        return textureName;
    }
}
