package me.modmuss50.fr.client

import me.modmuss50.fr.FluxedRedstone
import me.modmuss50.fr.mutlipart.PipeTypeEnum
import me.modmuss50.jsonDestroyer.client.ModelHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.client.model.IPerspectiveAwareModel
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.IOException

class PipeModelBakery {

    @SubscribeEvent
    fun onBakeModel(event: ModelBakeEvent) {
        var largeTransforms: ItemCameraTransforms? = null
        var smallTransforms: ItemCameraTransforms? = null
        try {
            largeTransforms = ModelHelper.loadTransformFromJson(ResourceLocation("fluxedredstone:models/pipe_large"))
            smallTransforms = ModelHelper.loadTransformFromJson(ResourceLocation("fluxedredstone:models/pipe_small"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val largeMap = IPerspectiveAwareModel.MapWrapper.getTransforms(largeTransforms)
        val smallMap = IPerspectiveAwareModel.MapWrapper.getTransforms(smallTransforms)

        for (type in PipeTypeEnum.values()) {
            var map = smallMap
            if(type.thickness!! == 5.0){
                map = largeMap
            }
            event.modelRegistry.putObject(ModelResourceLocation("fluxedredstone:FRPipe#variant=" + type.friendlyName.toLowerCase()), PipeModel(type, map))
            Minecraft.getMinecraft().renderItem.itemModelMesher.register(FluxedRedstone.itemMultiPipe.get(type), 0, ModelResourceLocation("fluxedredstone:itemFluxedPipe.${type.friendlyName}#inventory"))
            event.modelRegistry.putObject(ModelResourceLocation("fluxedredstone:itemFluxedPipe.${type.friendlyName}#inventory"), PipeModel(type, map))
        }
    }

    @SubscribeEvent
    fun onStitch(event: TextureStitchEvent.Pre) {
        for (type in PipeTypeEnum.values()) {
            event.map.registerSprite(ResourceLocation(type.textureName))
        }
    }

}