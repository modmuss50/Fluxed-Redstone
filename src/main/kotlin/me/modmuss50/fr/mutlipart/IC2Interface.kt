package me.modmuss50.fr.mutlipart

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import ic2.api.energy.EnergyNet
import ic2.api.energy.event.EnergyTileLoadEvent
import ic2.api.energy.event.EnergyTileUnloadEvent
import ic2.api.energy.tile.IEnergyAcceptor
import ic2.api.energy.tile.IEnergyEmitter
import ic2.api.energy.tile.IEnergyTile
import net.minecraft.client.Minecraft
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import reborncore.mcmultipart.block.TileMultipartContainer
import java.util.*

class IC2Interface {
    var connectionCache: Table<PipeMultipart, EnumFacing, IEnergyTile>
    var connectionQueue: HashSet<BlockPos>

    init {
        connectionCache = HashBasedTable.create<PipeMultipart, EnumFacing, IEnergyTile>()
        connectionQueue = HashSet()
        MinecraftForge.EVENT_BUS.register(this)
    }

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

    @SubscribeEvent
    fun onEnergyTileAdded(event: EnergyTileLoadEvent) {
        if (!ic2Inititalized) {
            ic2Inititalized = true
        }
        val tile = getTile(event.tile)
        Minecraft.getMinecraft().thePlayer?.sendChatMessage("IC2 tile placed at " + tile.pos)
        for (facing in EnumFacing.values()) {
            val target = event.world.getTileEntity(tile.pos.offset(facing))
            if (target is TileMultipartContainer) {
                connectionQueue.add(tile.pos)
                Minecraft.getMinecraft().thePlayer?.sendChatMessage("Queued tile connection at " + target.getPos())
            }
        }
    }

    @SubscribeEvent
    fun onEnergyTileRemoved(event: EnergyTileUnloadEvent) {
        if (ic2Inititalized) {
            if (connectionCache.containsValue(event.tile)) {
                for (entry in connectionCache.cellSet()) {
                    if (entry.value.equals(event.tile)) {
                        connectionCache.remove(entry.rowKey, entry.columnKey)
                        Minecraft.getMinecraft().thePlayer?.sendChatMessage("Un-cached at " + entry.rowKey.pos.offset(entry.columnKey))
                    }
                }
            }
        }
    }

    companion object {
        var ic2Inititalized = false
        val DUMMY_EMITTER = IEnergyEmitter { receiver, side -> true }
        val DUMMY_ACCEPTOR = IEnergyAcceptor { emitter, side ->  true}
    }
}