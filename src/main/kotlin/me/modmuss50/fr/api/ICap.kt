package me.modmuss50.fr.api

import me.modmuss50.fr.PipeTypeEnum


interface ICap {

    fun getTextureName() : String

    fun getType() : PipeTypeEnum

}