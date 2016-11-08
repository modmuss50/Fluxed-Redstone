package me.modmuss50.fr

import me.modmuss50.fr.mutlipart.PipeTypeEnum
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.File

/**
 * Created by modmuss50 on 24/01/2016 for Fluxed-Redstone.
 */
class Config(file: File?) : Configuration(file) {

    fun preInit(event: FMLPreInitializationEvent) {
        this.load()
        for (pipe in PipeTypeEnum.values()) {
            pipe.maxRF = getInt(pipe.friendlyName, "Energy", pipe.defualtRF, 0, Int.MAX_VALUE, "Change the amount of energy the ${pipe.friendlyName} Wire can transfer per tick")
        }
        FluxedRedstone.RFSupport = getBoolean("RF", "PowerNet", true, "Enable RF support")
        FluxedRedstone.teslaSupport = getBoolean("TESLA", "PowerNet", true, "Enable Tesla support")
        FluxedRedstone.ic2Support = getBoolean("IC2", "PowerNet", true, "Enable Ic2 support")
        FluxedRedstone.rfPerEU = getString("euPerRF", "PowerNet", "4", "Number of RF units per each unit of Ic2 EU").toDouble()
        this.save()
    }

}