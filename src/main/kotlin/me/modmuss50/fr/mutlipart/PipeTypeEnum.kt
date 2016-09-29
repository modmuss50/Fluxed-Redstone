package me.modmuss50.fr.mutlipart

import me.modmuss50.fr.mutlipart.types.BlazePipeMultipart
import me.modmuss50.fr.mutlipart.types.EnderPipeMultipart
import me.modmuss50.fr.mutlipart.types.GoldPipeMultipart
import net.minecraft.util.IStringSerializable
import kotlin.reflect.KClass

/**
 * Created by modmuss50 on 29/09/2016.
 */
enum class PipeTypeEnum constructor(var maxRF: Int, val friendlyName: String, val textureName: String, val thickness: Double?, multipartPipeClass: KClass<*>) : IStringSerializable {

    REDSTONE(128, "Redstone", "fluxedredstone:blocks/cable_redstone", 6.0, PipeMultipart::class),
    GOLD(128 * 8, "Gold", "fluxedredstone:blocks/cable_gold", 6.0, GoldPipeMultipart::class),
    BALZE(128 * 8 * 8, "Blaze", "fluxedredstone:blocks/cable_blaze", 6.0, BlazePipeMultipart::class),
    ENDER(128 * 8 * 8 * 8, "Ender", "fluxedredstone:blocks/cable_ender", 5.0, EnderPipeMultipart::class);

    val defualtRF: Int
    val classType: KClass<*>

    init {
        this.classType = multipartPipeClass
        this.defualtRF = maxRF
    }

    override fun getName(): String {
        return friendlyName.toLowerCase()
    }

    fun getJavaClass() : Class<*> {
        return classType.java
    }
}
