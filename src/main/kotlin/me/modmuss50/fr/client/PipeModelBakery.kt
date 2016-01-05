package me.modmuss50.fr.client

import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class PipeModelBakery {

    @SubscribeEvent
    fun onBakeModel(event: ModelBakeEvent) {
        event.modelRegistry.putObject(ModelResourceLocation("fluxedredstone:FRPipe#multipart"), PipeModel())

    }

    @SubscribeEvent
    fun onStitch(event: TextureStitchEvent.Pre) {
        event.map.registerSprite(ResourceLocation("fluxedredstone:blocks/cable"))
    }

}