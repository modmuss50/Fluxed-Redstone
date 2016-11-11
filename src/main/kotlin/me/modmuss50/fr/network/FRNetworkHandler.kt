package me.modmuss50.fr.network

import ic2.api.energy.EnergyNet
import ic2.api.energy.tile.IEnergyAcceptor
import ic2.api.energy.tile.IEnergyEmitter
import io.netty.buffer.ByteBuf
import me.modmuss50.fr.FluxedRedstone
import me.modmuss50.fr.mutlipart.IC2Interface
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

class FRNetworkHandler {
    companion object {
        val instance = NetworkRegistry.INSTANCE.newSimpleChannel("fluxedredstone")
    }

    fun registerPackets() {
        instance.registerMessage(RequestIC2MessageHandler::class.java, MsgRequestIC2Map::class.java, 0, Side.SERVER)
        instance.registerMessage(RespIC2MapHandler::class.java, MsgRespIC2Map::class.java, 1, Side.CLIENT)
    }

    class MsgRequestIC2Map() : IMessage {
        var id = 0
        lateinit var origin: BlockPos

        constructor(id: Int, origin: BlockPos) : this() {
            this.id = id
            this.origin = origin
        }

        override fun fromBytes(buf: ByteBuf?) {
            id = buf!!.readInt()
            origin = BlockPos(buf.readInt(), buf.readInt(), buf.readInt())
        }

        override fun toBytes(buf: ByteBuf?) {
            buf!!.writeInt(id)

            buf.writeInt(origin.x)
            buf.writeInt(origin.y)
            buf.writeInt(origin.z)
        }
    }

    // Sent to designate the block to the given face of the origin should be connected to.
    // Server --> Client
    class MsgRespIC2Map() : IMessage {
        var id = 0
        lateinit var machine: EnumFacing // The face relative to the origin on which the machine resides

        constructor(id: Int, machine: EnumFacing) : this() {
            this.id = id
            this.machine = machine
        }

        override fun fromBytes(buf: ByteBuf?) {
            id = buf!!.readInt()
            machine = EnumFacing.VALUES[buf.readUnsignedByte().toInt()]
        }

        override fun toBytes(buf: ByteBuf?) {
            buf!!.writeInt(id)
            buf.writeByte(machine.ordinal)
        }
    }

    class RequestIC2MessageHandler : IMessageHandler<MsgRequestIC2Map, IMessage> {
        override fun onMessage(message: MsgRequestIC2Map?, ctx: MessageContext?): IMessage? {
            val world = ctx!!.serverHandler.playerEntity.serverWorld
            world.addScheduledTask {
                for (rel in EnumFacing.VALUES) {
                    val tile = world.getTileEntity(message!!.origin.offset(rel))
                    if (tile != null) {
                        val etile = EnergyNet.instance.getTile(world, message.origin.offset(rel))
                        if (FluxedRedstone.ic2Interface.connectable(etile, rel.opposite)) {
                            instance.sendTo(MsgRespIC2Map(message.id, rel), ctx.serverHandler.playerEntity)
                        }
                    }
                }
            }
            return null
        }
    }
    class RespIC2MapHandler : IMessageHandler<MsgRespIC2Map, IMessage> {
        override fun onMessage(message: MsgRespIC2Map?, ctx: MessageContext?): IMessage? {
            Minecraft.getMinecraft().addScheduledTask {
                val pipe = FluxedRedstone.ic2Interface.waiting[message!!.id]
                if (pipe != null) {
                    if (!pipe.ic2ConnectionCache.contains(message.machine)) {
                        pipe.ic2ConnectionCache.add(message.machine)
                        pipe.checkConnections() // Don't cause an infinite loop
                    }
                }
            }
            return null
        }
    }
}