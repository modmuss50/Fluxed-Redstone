package me.modmuss50.fr.client

import me.modmuss50.fr.FluxedRedstone
import me.modmuss50.fr.PipeTypeEnum
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import reborncore.api.TextureRegistry

class PipeModelBakery {

    @SubscribeEvent
    fun onBakeModel(event: ModelBakeEvent) {
        event.modelRegistry.putObject(TextureRegistry.getModelResourceLocation(FluxedRedstone.blockPipe.defaultState), PipeModel())

    }

    @SubscribeEvent
    fun onStith(event: TextureStitchEvent.Pre){
        event.map.registerSprite(ResourceLocation("fluxedredstone:blocks/cable"))
        event.map.registerSprite(ResourceLocation("fluxedredstone:blocks/cap"))
    }

}