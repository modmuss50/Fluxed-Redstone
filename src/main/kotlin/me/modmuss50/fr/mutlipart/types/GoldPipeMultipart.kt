package me.modmuss50.fr.mutlipart.types

import me.modmuss50.fr.mutlipart.PipeMultipart
import me.modmuss50.fr.mutlipart.PipeTypeEnum

/**
 * Created by mark on 06/01/2016.
 */
class GoldPipeMultipart : PipeMultipart() {

    override fun getPipeType(): PipeTypeEnum {
        return PipeTypeEnum.GOLD
    }

}