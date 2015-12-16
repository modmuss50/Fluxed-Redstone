package me.modmuss50.fr.tile

import me.modmuss50.fr.api.ICap
import net.minecraft.inventory.IInventory
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import java.util.*

class TilePipe : TileEntity(), ITickable {

    public var capMap = HashMap<EnumFacing, ICap>()

    override fun update() {
        if(worldObj.isRemote)
            return
    }

    fun connects(facing: EnumFacing): Boolean {
        val newPos = getPos().add(facing.frontOffsetX, facing.frontOffsetY, facing.frontOffsetZ)
        val entity = worldObj.getTileEntity(newPos)
        if (entity is TilePipe) {
            val tilePipe = entity
            if (!hasCap(facing) && !tilePipe.hasCap(facing.opposite)) {
                return true
            }
        }
        if(entity is IInventory){
            return true
        }
        return false
    }

    fun addCap(side: EnumFacing, cap: ICap): Boolean {
        if (capMap.containsKey(side)) {
            return false
        }
        capMap.put(side, cap)
        worldObj.markBlockForUpdate(pos)
        worldObj.markBlockRangeForRenderUpdate(pos, pos)
        return true
    }

    fun getCapForSide(side: EnumFacing): ICap? {
        if (capMap.contains(side)) {
            return capMap.get(side)
        } else {
            return null
        }
    }

    fun hasCap(side: EnumFacing): Boolean {
        return capMap.containsKey(side)
    }

    fun removeCap(side: EnumFacing): Boolean {
        if (hasCap(side)) {
            capMap.remove(side)
            worldObj.markBlockForUpdate(pos)
            worldObj.markBlockRangeForRenderUpdate(pos, pos)
            return true
        }
        return false
    }

}