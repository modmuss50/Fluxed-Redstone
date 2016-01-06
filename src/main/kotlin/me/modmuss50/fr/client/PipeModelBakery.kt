package me.modmuss50.fr.client

import me.modmuss50.fr.PipeTypeEnum
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class PipeModelBakery {

    @SubscribeEvent
    fun onBakeModel(event: ModelBakeEvent) {
        for(type in PipeTypeEnum.values){
            event.modelRegistry.putObject(ModelResourceLocation("fluxedredstone:FRPipe#variant=" + type.friendlyName.toLowerCase()), PipeModel(type))
        }
    }

    @SubscribeEvent
    fun onStitch(event: TextureStitchEvent.Pre) {
        for(type in PipeTypeEnum.values){
            event.map.registerSprite(ResourceLocation(type.textureName))
        }
    }

}