package me.modmuss50.fr.mutlipart

import mcmultipart.item.ItemMultiPart
import mcmultipart.multipart.IMultipart
import me.modmuss50.fr.PipeTypeEnum
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.EnumFacing
import net.minecraft.util.Vec3
import net.minecraft.world.World
import reborncore.api.IItemTexture
import kotlin.text.replace

class ItemMultipartPipe(val type : PipeTypeEnum) : ItemMultiPart() , IItemTexture {

    override fun getMaxMeta(): Int {
        return 1
    }

    override fun getModID(): String? {
        return "fluxedredstone"
    }

    override fun getTextureName(damage: Int): String? {
        return type.textureName.replace("blocks", "items")
    }

    override fun createPart(p0: World?, p1: BlockPos?, p2: EnumFacing?, p3: Vec3?, p4: ItemStack?, p5: EntityPlayer?): IMultipart? {
        return type.classType.newInstance()
    }

    override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: MutableList<String>?, advanced: Boolean) {
        super.addInformation(stack, playerIn, tooltip, advanced)
        tooltip!!.add("${EnumChatFormatting.GREEN}${type.maxRF} RF / tick")
    }
}