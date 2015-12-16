package me.modmuss50.fr.client

import me.modmuss50.fr.client.item.CustomItemRenderer
import me.modmuss50.fr.tile.TilePipe
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

class PipeTESR : TileEntitySpecialRenderer<TilePipe>() {

    val itemRenderer = CustomItemRenderer(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem())

    public override fun renderTileEntityAt(te: TilePipe, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        val eItem = EntityItem(te.getWorld(), te.getPos().getX().toDouble(), te.getPos().getY().toDouble(), te.getPos().getZ().toDouble(), ItemStack(Items.apple))
        eItem.hoverStart = 0f

        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5, y + 0.25, z + 0.5)

        itemRenderer.doRender(eItem, 0.0, 0.0, 0.0, 0.0f, 0.0f)

        GlStateManager.popMatrix()
    }
}