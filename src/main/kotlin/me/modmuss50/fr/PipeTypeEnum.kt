package me.modmuss50.fr

enum class PipeTypeEnum(val maxRF: Int, val friendlyName: String, val textureName: String) {
    STONE(64, "Stone", "minecraft:blocks/stone"),
    IRON(128, "Iron", "minecraft:blocks/iron_block"),
    GOLD(256, "Gold", "minecraft:blocks/gold_block"),
    DIAMOND(1024, "Diamond", "minecraft:blocks/diamond_block")
}