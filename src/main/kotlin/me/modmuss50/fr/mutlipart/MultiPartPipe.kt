package me.modmuss50.fr.mutlipart

import cofh.api.energy.IEnergyConnection
import mcmultipart.MCMultiPartMod
import mcmultipart.microblock.IMicroblock
import mcmultipart.multipart.*
import me.modmuss50.fr.FluxedRedstone
import me.modmuss50.fr.WorldState
import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockState
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.world.World
import net.minecraftforge.common.property.ExtendedBlockState
import net.minecraftforge.common.property.IExtendedBlockState
import net.minecraftforge.common.property.IUnlistedProperty
import reborncore.common.misc.Functions
import reborncore.common.misc.vecmath.Vecs3dCube
import java.util.*

/**
 * Created by mark on 05/01/2016.
 */
class MultipartPipe() : Multipart(), IOccludingPart, ISlottedPart {

    override fun getSlotMask(): EnumSet<PartSlot>? {
        return EnumSet.of(PartSlot.CENTER);
    }

    var boundingBoxes = arrayOfNulls<Vecs3dCube>(14)

    var center = 0.6F
    var offset = 0.1F

    var connectedSides = HashMap<EnumFacing, BlockPos>()

    init {
        refreshBounding()
    }

    fun refreshBounding() {
        val centerFirst = (center - offset).toDouble()
        val w = 0.3F
        boundingBoxes[6] = Vecs3dCube(centerFirst.toDouble() - w - 0.03, centerFirst.toDouble() - w - 0.08, centerFirst.toDouble() - w - 0.03, centerFirst.toDouble() + w + 0.08,
                centerFirst.toDouble() + w + 0.04, centerFirst.toDouble() + w + 0.08)

        boundingBoxes[6] = Vecs3dCube(centerFirst - w, centerFirst - w,
                centerFirst - w, centerFirst + w, centerFirst + w, centerFirst + w)

        var i = 0
        for (dir in EnumFacing.values) {
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
        for(facing in EnumFacing.values){
            if(connectedSides.containsKey(facing)){
                list!!.add(boundingBoxes[Functions.getIntDirFromDirection(facing)]!!.toAABB())
            }
        }
        list!!.add(boundingBoxes[6]!!.toAABB())
    }

    override fun addSelectionBoxes(list: MutableList<AxisAlignedBB>?) {
        for(facing in EnumFacing.values){
            if(connectedSides.containsKey(facing)){
                list!!.add(boundingBoxes[Functions.getIntDirFromDirection(facing)]!!.toAABB())
            }
        }
        list!!.add(boundingBoxes[6]!!.toAABB())
    }

    override fun addOcclusionBoxes(list: MutableList<AxisAlignedBB>?) {
        list!!.add(boundingBoxes[6]!!.toAABB())
    }

    override fun getModelPath(): String? {
        return "fluxedredstone:FRPipe"
    }

    fun checkConnections(){
        connectedSides.clear()
        for(facing in EnumFacing.values){
            if(shouldConnectTo(pos, facing)){
                connectedSides.put(facing, pos)
            }
        }
    }

    fun shouldConnectTo(pos: BlockPos?, dir: EnumFacing?): Boolean {
        for(p in container.parts){
            if(p != this && p is IOccludingPart){
                var mask = boundingBoxes[Functions.getIntDirFromDirection(dir)]!!.toAABB()
                var boxes = ArrayList<AxisAlignedBB>()
                var part = p as IOccludingPart
                part.addOcclusionBoxes(boxes)
                for(box in boxes){
                    if(mask.intersectsWith(box)){
                        return false;
                    }
                }
            }
        }

        if(getPipe(world, pos!!, dir) != null){
            return true
        }

        var tile = world.getTileEntity(pos.offset(dir))
        if(tile is IEnergyConnection){
            return true
        }

        return false
    }

    fun getPipe(world: World, blockPos: BlockPos, side: EnumFacing?): MultipartPipe? {
        val container = MultipartHelper.getPartContainer(world, blockPos) ?: return null

        if (side != null) {
            val part = container.getPartInSlot(PartSlot.getFaceSlot(side))
            if (part is IMicroblock.IFaceMicroblock && !(part as IMicroblock.IFaceMicroblock).isFaceHollow()) {
                return null
            }
        }

        val part = container.getPartInSlot(PartSlot.CENTER)
        if (part is MultipartPipe) {
            return part as MultipartPipe
        } else {
            return null
        }
    }

    override fun getExtendedState(state: IBlockState?): IBlockState? {
        var extState = state as IExtendedBlockState;

        return extState.withProperty(FluxedRedstone.stateHelper.UP, shouldConnectTo(pos, EnumFacing.UP))!!.withProperty(FluxedRedstone.stateHelper.DOWN, shouldConnectTo(pos, EnumFacing.DOWN))!!.withProperty(FluxedRedstone.stateHelper.NORTH, shouldConnectTo(pos, EnumFacing.NORTH))!!.withProperty(FluxedRedstone.stateHelper.EAST, shouldConnectTo(pos, EnumFacing.EAST))!!.withProperty(FluxedRedstone.stateHelper.WEST, shouldConnectTo(pos, EnumFacing.WEST))!!.withProperty(FluxedRedstone.stateHelper.SOUTH, shouldConnectTo(pos, EnumFacing.SOUTH))
    }

    override fun onAdded() {
        super.onAdded()
    }

    override fun onLoaded() {
        super.onLoaded()
    }

    override fun onNeighborBlockChange(block: Block?) {
        super.onNeighborBlockChange(block)
    }

    override fun createBlockState(): BlockState? {
        //return BlockState(MCMultiPartMod.multipart, FluxedRedstone.stateHelper.UP, FluxedRedstone.stateHelper.DOWN, FluxedRedstone.stateHelper.NORTH, FluxedRedstone.stateHelper.EAST, FluxedRedstone.stateHelper.WEST, FluxedRedstone.stateHelper.SOUTH)
        return ExtendedBlockState(MCMultiPartMod.multipart, arrayOfNulls(0), arrayOf(FluxedRedstone.stateHelper.UP, FluxedRedstone.stateHelper.DOWN, FluxedRedstone.stateHelper.NORTH, FluxedRedstone.stateHelper.EAST, FluxedRedstone.stateHelper.WEST, FluxedRedstone.stateHelper.SOUTH))
    }
}
