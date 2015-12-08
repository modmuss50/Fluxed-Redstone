package me.modmuss50.fr.caps

import me.modmuss50.fr.api.ICap

class TestCap : ICap {
    override fun getTextureName(): String {
        return "minecraft:blocks/iron_block"
    }
}