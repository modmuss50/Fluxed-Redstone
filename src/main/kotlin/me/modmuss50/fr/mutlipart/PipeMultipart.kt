package me.modmuss50.fr.mutlipart

import cofh.api.energy.IEnergyConnection
import cofh.api.energy.IEnergyProvider
import cofh.api.energy.IEnergyReceiver
import me.modmuss50.fr.FluxedRedstone
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.property.ExtendedBlockState
import net.minecraftforge.common.property.IExtendedBlockState
import net.minecraftforge.common.property.Properties
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.energy.IEnergyStorage
import reborncore.common.misc.Functions
import reborncore.common.misc.vecmath.Vecs3dCube
import reborncore.mcmultipart.MCMultiPartMod
import reborncore.mcmultipart.microblock.IMicroblock
import reborncore.mcmultipart.multipart.*
import reborncore.mcmultipart.raytrace.PartMOP
import java.util.*

open class PipeMultipart() : Multipart(), ISlottedPart, ITickable {

    override fun getSlotMask(): EnumSet<PartSlot>? {
        return EnumSet.of(PartSlot.CENTER);
    }

    open fun getPipeType(): PipeTypeEnum {
        return PipeTypeEnum.REDSTONE
    }

    var boundingBoxes = arrayOfNulls<Vecs3dCube>(14)

    var center = 0.6F
    var offset = 0.1F

    var connectedSides = HashMap<EnumFacing, BlockPos>()

    var power = 0;

    init {
        refreshBounding()
    }

    fun refreshBounding() {
        val centerFirst = (center - offset).toDouble()
        val thickness = getPipeType().thickness!!
        val w = (thickness / 16) - 0.5
        boundingBoxes[6] = Vecs3dCube(centerFirst.toDouble() - w - 0.03, centerFirst.toDouble() - w - 0.08, centerFirst.toDouble() - w - 0.03, centerFirst.toDouble() + w + 0.08,
                centerFirst.toDouble() + w + 0.04, centerFirst.toDouble() + w + 0.08)

        boundingBoxes[6] = Vecs3dCube(centerFirst - w, centerFirst - w,
                centerFirst - w, centerFirst + w, centerFirst + w, centerFirst + w)

        var i = 0
        for (dir in EnumFacing.values()) {
            val xMin1 = (if (dir.frontOffsetX < 0)
                0.0
            else
                (if (dir.frontOffsetX === 0) centerFirst - w else centerFirst + w))
            val xMax1 = (if (dir.frontOffsetX > 0)
                1.0
            else
                (if (dir.frontOffsetX === 0) centerFirst + w else centerFirst - w))

            val yMin1 = (if (dir.frontOffsetY < 0)
                0.0
            else
                (if (dir.frontOffsetY === 0) centerFirst - w else centerFirst + w))
            val yMax1 = (if (dir.frontOffsetY > 0)
                1.0
            else
                (if (dir.frontOffsetY === 0) centerFirst + w else centerFirst - w))

            val zMin1 = (if (dir.frontOffsetZ < 0)
                0.0
            else
                (if (dir.frontOffsetZ === 0) centerFirst - w else centerFirst + w))
            val zMax1 = (if (dir.frontOffsetZ > 0)
                1.0
            else
                (if (dir.frontOffsetZ === 0) centerFirst + w else centerFirst - w))

            boundingBoxes[i] = Vecs3dCube(xMin1, yMin1, zMin1, xMax1,
                    yMax1, zMax1)
            i++
        }
    }


    override fun addCollisionBoxes(mask: AxisAlignedBB?, list: MutableList<AxisAlignedBB>?, collidingEntity: Entity?) {
        for (facing in EnumFacing.values()) {
            if (connectedSides.containsKey(facing)) {
                if (boundingBoxes[Functions.getIntDirFromDirection(facing)]!!.toAABB().intersectsWith(mask)) {
                    list!!.add(boundingBoxes[Functions.getIntDirFromDirection(facing)]!!.toAABB())
                }
            }
        }
        if (boundingBoxes[6]!!.toAABB().intersectsWith(mask)) {
            list!!.add(boundingBoxes[6]!!.toAABB())
        }
    }


    override fun addSelectionBoxes(list: MutableList<AxisAlignedBB>?) {
        for (facing in EnumFacing.values()) {
            if (connectedSides.containsKey(facing)) {
                list!!.add(boundingBoxes[Functions.getIntDirFromDirection(facing)]!!.toAABB())
            }
        }
        list!!.add(boundingBoxes[6]!!.toAABB())
    }

    override fun getModelPath(): ResourceLocation? {
        return ResourceLocation("fluxedredstone:FRPipe")
    }

    fun checkConnections() {
        connectedSides.clear()
        for (facing in EnumFacing.values()) {
            if (shouldConnectTo(pos, facing)) {
                connectedSides.put(facing, pos)
            }
        }
    }

    fun shouldConnectTo(pos: BlockPos?, dir: EnumFacing?): Boolean {
        if (dir != null) {
            if (internalShouldConnectTo(pos, dir)) {
                var otherPipe = getPipe(world, pos!!.offset(dir), dir);
                if (otherPipe != null && !otherPipe.internalShouldConnectTo(otherPipe.pos, dir.opposite)) {
                    return false
                }
                return true
            }
        }
        return false
    }

    fun internalShouldConnectTo(pos: BlockPos?, dir: EnumFacing?): Boolean {
        var slottedPart = PartSlot.getFaceSlot(dir)
        if (slottedPart != null) {
            var part = container.getPartInSlot(slottedPart)
            if (part != null && part is IMicroblock.IFaceMicroblock) {
                if (!part.isFaceHollow) {
                    return false
                }
            }
        }

//        if (!OcclusionHelper.occlusionTest(container.parts, this, boundingBoxes[Functions.getIntDirFromDirection(dir)]!!.toAABB())) {
//            return false;
//        }

        var otherPipe = getPipe(world, pos!!.offset(dir), dir);
        if (otherPipe != null) {
            return true
        }

        var tile = world.getTileEntity(pos.offset(dir))
        if (tile != null) {
            if(FluxedRedstone.RFSupport){
                if (tile is IEnergyConnection) {
                    if (tile.canConnectEnergy(dir)) {
                        return true
                    }
                }
            }
            if(FluxedRedstone.teslaSupport && FluxedRedstone.teslaManager.canConnect(tile, dir!!)){
                return true
            }

            if (tile.hasCapability(CapabilityEnergy.ENERGY, dir?.opposite)) {
                return true
            }
        }

        return false
    }

    fun getPipe(world: World, blockPos: BlockPos, side: EnumFacing?): PipeMultipart? {
        val container = MultipartHelper.getPartContainer(world, blockPos) ?: return null

        if (side != null) {
            val part = container.getPartInSlot(PartSlot.getFaceSlot(side))
            if (part is IMicroblock.IFaceMicroblock && !part.isFaceHollow()) {
                return null
            }
        }

        val part = container.getPartInSlot(PartSlot.CENTER)
        if (part is PipeMultipart) {
            return part
        } else {
            return null
        }
    }

    override fun getExtendedState(state: IBlockState?): IBlockState? {
        var extState = state as IExtendedBlockState;

        return extState.withProperty(UP, shouldConnectTo(pos, EnumFacing.UP))!!.withProperty(DOWN, shouldConnectTo(pos, EnumFacing.DOWN))!!.withProperty(NORTH, shouldConnectTo(pos, EnumFacing.NORTH))!!.withProperty(EAST, shouldConnectTo(pos, EnumFacing.EAST))!!.withProperty(WEST, shouldConnectTo(pos, EnumFacing.WEST))!!.withProperty(SOUTH, shouldConnectTo(pos, EnumFacing.SOUTH)).withProperty(TYPE, getPipeType())
    }

    override fun onAdded() {
        super.onAdded()
        checkConnections()
    }

    override fun onNeighborBlockChange(block: Block?) {
        super.onNeighborBlockChange(block)
        checkConnections()
    }

    override fun createBlockState(): BlockStateContainer? {
        return ExtendedBlockState(MCMultiPartMod.multipart, arrayOf(TYPE), arrayOf(UP, DOWN, NORTH, EAST, WEST, SOUTH))
    }

    override fun update() {
        if (world != null) {
            if (world.totalWorldTime % 80 == 0.toLong()) {
                checkConnections()
            }
            if (world.isRemote) {
                return
            }

            for (face in EnumFacing.values()) {
                if (connectedSides.containsKey(face)) {
                    var offPos = pos.offset(face)
                    var tile = world.getTileEntity(offPos)!!
                    //Tesla
                    if(FluxedRedstone.teslaSupport){
                        FluxedRedstone.teslaManager.update(this, tile, face)
                    }
                    //Forge
                    if (tile.hasCapability(CapabilityEnergy.ENERGY, face.opposite)) {
                        var energy: IEnergyStorage = tile.getCapability(CapabilityEnergy.ENERGY, face.opposite)
                        var didExtract = false;
                        if (energy.canExtract()) {
                            var move = energy.extractEnergy(Math.min(getPipeType().maxRF, getPipeType().maxRF * 4 - power), false)
                            if (move != 0) {
                                power += move;
                                didExtract = true;
                            }

                        }
                        if (energy.canReceive() && !didExtract) {
                            var move = energy.receiveEnergy(Math.min(getPipeType().maxRF, power), false)
                            if (move != 0) {
                                power -= move;
                            }
                        }
                    }
                    //RF
                    if (FluxedRedstone.RFSupport && tile is IEnergyConnection) {
                        if (tile is IEnergyProvider) {
                            if (tile.canConnectEnergy(face)) {
                                var move = tile.extractEnergy(face.opposite, Math.min(getPipeType().maxRF, getPipeType().maxRF * 4 - power), false)
                                if (move != 0) {
                                    power += move;
                                }
                            }
                        }
                        if (tile is IEnergyReceiver) {
                            if (tile.canConnectEnergy(face)) {
                                var move = tile.receiveEnergy(face.opposite, Math.min(getPipeType().maxRF, power), false)
                                if (move != 0) {
                                    power -= move;
                                }
                            }
                        }
                    }
                    var pipe = getPipe(world, pos.offset(face), face)
                    if (pipe != null) {
                        var averPower = (power + pipe.power) / 2
                        pipe.power = averPower
                        power = averPower
                        if ((power + pipe.power) % 2 != 0) {
                            //This should fix rounding issues that cause power loss.
                            pipe.power + 1;
                        }
                    }
                }
            }
        }
    }


    override fun writeToNBT(tag: NBTTagCompound?): NBTTagCompound {
        super.writeToNBT(tag)
        tag!!.setInteger("power", power)
        return tag
    }

    override fun readFromNBT(tag: NBTTagCompound?) {
        super.readFromNBT(tag)
        power = tag!!.getInteger("power")
    }

    override fun getPickBlock(player: EntityPlayer?, hit: PartMOP?): ItemStack? {
        return ItemStack(FluxedRedstone.itemMultiPipe.get(getPipeType()))
    }

    override fun getDrops(): MutableList<ItemStack>? {
        var list = ArrayList<ItemStack>()
        list.add(ItemStack(FluxedRedstone.itemMultiPipe.get(getPipeType())))
        return list
    }

    override fun getHardness(hit: PartMOP?): Float {
        return 1F
    }

    companion  object  {
        val UP = Properties.toUnlisted(PropertyBool.create("up"))
        val DOWN = Properties.toUnlisted(PropertyBool.create("down"))
        val NORTH = Properties.toUnlisted(PropertyBool.create("north"))
        val EAST = Properties.toUnlisted(PropertyBool.create("east"))
        val SOUTH = Properties.toUnlisted(PropertyBool.create("south"))
        val WEST = Properties.toUnlisted(PropertyBool.create("west"))

        var TYPE = PropertyEnum.create("variant", PipeTypeEnum::class.java)
    }

}
