package me.modmuss50.fr.tile

import me.modmuss50.fr.powernet.PowerNetwork
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.world.World

class TilePipe : TileEntity(), ITickable {

    public var powerNetwork = PowerNetwork()

    override fun update() {
        if (powerNetwork == null) {
            findAndJoinNetwork(worldObj, getPos().x, getPos().y, getPos().z)
        } else {
            println(powerNetwork.pipes.size)
        }
    }

    fun connects(facing: EnumFacing): Boolean {
        val newPos = getPos().add(facing.frontOffsetX, facing.frontOffsetY, facing.frontOffsetZ)
        val entity = worldObj.getTileEntity(newPos)
        return entity is TilePipe
    }


    fun findAndJoinNetwork(world: World, x: Int, y: Int, z: Int) {
        powerNetwork = PowerNetwork()
        powerNetwork.addElement(this)
        for (direction in EnumFacing.values()) {
            if (world.getTileEntity(BlockPos(x + direction.frontOffsetX, y + direction.frontOffsetY, z + direction.frontOffsetZ)) is TilePipe) {
                val pipe = world.getTileEntity(BlockPos(x + direction.frontOffsetX, y + direction.frontOffsetY, z + direction.frontOffsetZ)) as TilePipe
                if (pipe.powerNetwork != null) {
                    pipe.powerNetwork.merge(powerNetwork)
                }
            }
        }
    }

    fun setNetwork(n: PowerNetwork?) {
        if (n == null) {
        } else {
            powerNetwork = n
            powerNetwork.addElement(this)
        }
    }

    fun resetNetwork() {
        powerNetwork = null!!
    }

    fun removeFromNetwork() {
        if (powerNetwork == null) {
        } else
            powerNetwork.removeElement(this)
    }

    fun rebuildNetwork() {
        this.removeFromNetwork()
        this.resetNetwork()
        this.findAndJoinNetwork(worldObj, getPos().x, getPos().y, getPos().z)
    }
}