package me.modmuss50.fr.mutlipart

import mcmultipart.item.ItemMultiPart
import mcmultipart.multipart.IMultipart
import me.modmuss50.fr.PipeTypeEnum
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World

class ItemMultipartPipe(val type: PipeTypeEnum) : ItemMultiPart() {

    override fun createPart(p0: World?, p1: BlockPos?, p2: EnumFacing?, p3: Vec3d?, p4: ItemStack?, p5: EntityPlayer?): IMultipart? {
        return type.classType.newInstance()
    }

    override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: MutableList<String>?, advanced: Boolean) {
        super.addInformation(stack, playerIn, tooltip, advanced)
        tooltip!!.add("${TextFormatting.GREEN}${type.maxRF} RF / tick")
    }
}