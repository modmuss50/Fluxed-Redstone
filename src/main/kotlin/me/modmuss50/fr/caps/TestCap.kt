package me.modmuss50.fr.caps

import me.modmuss50.fr.PipeTypeEnum
import me.modmuss50.fr.api.ICap

class TestCap : ICap {
    override fun getType(): PipeTypeEnum {
        return PipeTypeEnum.DIAMOND
    }

    override fun getTextureName(): String {
        return "minecraft:blocks/iron_block"
    }
}