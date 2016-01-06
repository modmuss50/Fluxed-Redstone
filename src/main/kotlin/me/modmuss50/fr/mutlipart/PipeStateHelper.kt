package me.modmuss50.fr.mutlipart

import me.modmuss50.fr.PipeTypeEnum
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraftforge.common.property.Properties

/**
 * Created by mark on 05/01/2016.
 */
class PipeStateHelper {
    val UP = Properties.toUnlisted(PropertyBool.create("up"))
    val DOWN = Properties.toUnlisted(PropertyBool.create("down"))
    val NORTH = Properties.toUnlisted(PropertyBool.create("north"))
    val EAST = Properties.toUnlisted(PropertyBool.create("east"))
    val SOUTH = Properties.toUnlisted(PropertyBool.create("south"))
    val WEST = Properties.toUnlisted(PropertyBool.create("west"))

    var typeProp = PropertyEnum.create("variant", PipeTypeEnum::class.java)
}