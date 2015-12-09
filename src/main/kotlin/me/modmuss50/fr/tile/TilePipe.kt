package me.modmuss50.fr.tile

import cofh.api.energy.IEnergyConnection
import cofh.api.energy.IEnergyProvider
import cofh.api.energy.IEnergyReceiver
import me.modmuss50.fr.api.ICap
import me.modmuss50.fr.block.BlockPipe
import me.modmuss50.fr.powernet.PowerNetwork
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.world.World
import java.util.*

class TilePipe : TileEntity(), ITickable {

    public var powerNetwork = PowerNetwork(false)
    public var capMap = HashMap<EnumFacing, ICap>()

    init {
    }

    override fun update() {
        if(worldObj.isRemote)
            return

        if (powerNetwork == null || !powerNetwork.isValid) {
            findAndJoinNetwork(worldObj, getPos().x, getPos().y, getPos().z)
        } else {
            val block = worldObj.getBlockState(pos).block as BlockPipe
            val RFTick = block.type.maxRF
            for(face in capMap.keys.toArrayList()){
                var tile = world.getTileEntity(pos.offset(face))
                if (tile is IEnergyConnection) {
                    if(tile.canConnectEnergy(face)){
                        if (tile is IEnergyProvider) {
                            var insert = tile.extractEnergy(face, Math.min(RFTick, powerNetwork.pipes.size * powerNetwork.RFPerPipe), false)
                            powerNetwork.networkRF += insert
                        }
                        if (powerNetwork.networkRF > 0) {
                            if (tile is IEnergyReceiver) {
                                var extract = tile.receiveEnergy(face, Math.min(RFTick, (powerNetwork.pipes.size * powerNetwork.RFPerPipe) - powerNetwork.networkRF), false)
                                powerNetwork.networkRF -= extract
                            }
                        }
                    }

                }

            }
        }
    }

    fun connects(facing: EnumFacing): Boolean {
        val newPos = getPos().add(facing.frontOffsetX, facing.frontOffsetY, facing.frontOffsetZ)
        val entity = worldObj.getTileEntity(newPos)
        if (entity is TilePipe) {
            val block = worldObj.getBlockState(newPos).block as BlockPipe
            val type = block.type;
            val ourBlock = worldObj.getBlockState(pos).block as BlockPipe
            val ourType = ourBlock.type;
            if (type != ourType) {
                return false
            }
        }
        if (entity is TilePipe) {
            val tilePipe = entity
            if (!hasCap(facing) && !tilePipe.hasCap(facing.opposite)) {
                return true
            }
        }
        return false
    }

    fun findAndJoinNetwork(world: World, x: Int, y: Int, z: Int) {
        powerNetwork = PowerNetwork()
        powerNetwork.addElement(this)
        for (direction in EnumFacing.values()) {
            if (connects(direction)) {
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