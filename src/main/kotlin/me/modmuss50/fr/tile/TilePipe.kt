package me.modmuss50.fr.tile

import cofh.api.energy.IEnergyConnection
import cofh.api.energy.IEnergyProvider
import cofh.api.energy.IEnergyReceiver
import me.modmuss50.fr.powernet.PowerNetwork
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.world.World

class TilePipe : TileEntity(), ITickable {

    public var powerNetwork = PowerNetwork(false)

    init {
    }

    override fun update() {
        if(worldObj.isRemote)
            return

        if (powerNetwork == null || !powerNetwork.isValid) {
            findAndJoinNetwork(worldObj, getPos().x, getPos().y, getPos().z)
        } else {
            for(face in EnumFacing.values){
                if(connects(face)){
                    val RFTick = 128
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
    }

    fun connects(facing: EnumFacing): Boolean {
        val newPos = getPos().add(facing.frontOffsetX, facing.frontOffsetY, facing.frontOffsetZ)
        val entity = worldObj.getTileEntity(newPos)
        return entity is IEnergyConnection || entity is TilePipe;
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

}