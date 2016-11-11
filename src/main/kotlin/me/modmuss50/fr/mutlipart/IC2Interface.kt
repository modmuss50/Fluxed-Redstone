package me.modmuss50.fr.mutlipart

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.Maps
import ic2.api.energy.EnergyNet
import ic2.api.energy.tile.IEnergyAcceptor
import ic2.api.energy.tile.IEnergyEmitter
import ic2.api.energy.tile.IEnergyTile
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import java.util.*

class IC2Interface {
    val waiting: BiMap<Int, PipeMultipart> = HashBiMap.create()

    /**
     * Gets the <i>main</i> tile entity associated with this energy component
     */
    fun getMainTile(energyTile: IEnergyTile): IEnergyTile {
        val pos = EnergyNet.instance.getPos(energyTile)
        val world = EnergyNet.instance.getWorld(energyTile)
        return EnergyNet.instance.getTile(world, pos)
    }

    /**
     * Gets a tile entity from an energy component's <i>main</i> tile entity
     */
    fun getTile(energyTile: IEnergyTile): TileEntity {
        return EnergyNet.instance.getWorld(energyTile).getTileEntity(EnergyNet.instance.getPos(energyTile))!!
    }

    fun getTileFromIC2(energyTile: IEnergyTile): TileEntity {
        return getTile(getMainTile(energyTile))
    }

    fun connectable(tile: IEnergyTile?, face: EnumFacing): Boolean {
        return (tile is IEnergyAcceptor && tile.acceptsEnergyFrom(DUMMY_EMITTER, face)) || (tile is IEnergyEmitter && tile.emitsEnergyTo(DUMMY_ACCEPTOR, face))
    }

    companion object {
        val DUMMY_EMITTER = IEnergyEmitter { receiver, side -> true }
        val DUMMY_ACCEPTOR = IEnergyAcceptor { emitter, side ->  true}
    }
}