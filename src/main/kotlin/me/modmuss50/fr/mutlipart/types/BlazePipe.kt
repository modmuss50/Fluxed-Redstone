package me.modmuss50.fr.mutlipart.types

import me.modmuss50.fr.PipeTypeEnum
import me.modmuss50.fr.mutlipart.MultipartPipe

/**
 * Created by mark on 06/01/2016.
 */
class BlazePipe : MultipartPipe() {

    override fun getPipeType(): PipeTypeEnum {
        return PipeTypeEnum.BALZE
    }

}