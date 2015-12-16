package me.modmuss50.fr.client.item

import net.minecraft.client.renderer.entity.RenderEntityItem
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.entity.RenderManager

class CustomItemRenderer(renderManagerIn: RenderManager?, renderItem: RenderItem?) : RenderEntityItem(renderManagerIn, renderItem) {

    override fun shouldBob(): Boolean {
        return false
    }

    override fun shouldSpreadItems(): Boolean {
        return false
    }
}