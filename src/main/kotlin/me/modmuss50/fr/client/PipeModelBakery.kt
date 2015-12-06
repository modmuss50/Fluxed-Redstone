package me.modmuss50.fr.client

import me.modmuss50.fr.FluxedRedstone
import me.modmuss50.fr.PipeTypeEnum
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import reborncore.api.TextureRegistry

class PipeModelBakery {

    @SubscribeEvent
    fun onBakeModel(event: ModelBakeEvent) {
        for(typeEnum in PipeTypeEnum.values()){
            event.modelRegistry.putObject(TextureRegistry.getModelResourceLocation(FluxedRedstone.pipeTypeEnumBlockHashMap.get(typeEnum)?.defaultState), PipeModel())
        }
    }

}