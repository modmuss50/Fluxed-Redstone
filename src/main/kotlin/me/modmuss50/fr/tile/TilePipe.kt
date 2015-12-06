package me.modmuss50.fr.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable

class TilePipe : TileEntity(), ITickable {
    override fun update() {

    }

    fun connects(facing: EnumFacing): Boolean {
        val newPos = getPos().add(facing.frontOffsetX, facing.frontOffsetY, facing.frontOffsetZ)
        val entity = worldObj.getTileEntity(newPos)
        return entity is TilePipe
    }
}