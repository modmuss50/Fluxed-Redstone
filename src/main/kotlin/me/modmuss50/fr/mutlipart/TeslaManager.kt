package me.modmuss50.fr.mutlipart

import net.darkhax.tesla.capability.TeslaCapabilities
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing

/**
 * Created by modmuss50 on 13/09/2016.
 */
class TeslaManager {

    public fun update(pipe: PipeMultipart, tile: TileEntity, face: EnumFacing){
        if (tile!!.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, face.opposite)) {
            var producer = tile.getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, face.opposite);
            var move = producer.takePower(Math.min(pipe.getPipeType().maxRF, (pipe.getPipeType().maxRF * 4) - pipe.power).toLong(), false)
            if (move != 0L) {
                pipe.power += move.toInt();
            }
        } else if (tile.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, face.opposite)) {
            var consumer = tile.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, face.opposite)
            var move = consumer.givePower(Math.min(pipe.getPipeType().maxRF, pipe.power).toLong(), false);
            if (move != 0L) {
                pipe.power -= move.toInt();
            }
        }
    }

    public fun canConnect(tile: TileEntity, face: EnumFacing) : Boolean{
        if (tile.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, face.opposite)) {
            return true
        }
        if (tile.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, face.opposite)) {
            return true
        }
        return false;
    }
}