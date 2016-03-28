package me.modmuss50.fr.client

import ClientStatics
import me.modmuss50.fr.FluxedRedstone
import me.modmuss50.fr.PipeTypeEnum
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.*
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.Attributes
import net.minecraftforge.client.model.IPerspectiveAwareModel
import net.minecraftforge.common.property.IExtendedBlockState
import org.apache.commons.lang3.tuple.Pair
import org.lwjgl.util.vector.Vector3f
import reborncore.common.misc.vecmath.Vecs3dCube
import java.util.*
import javax.vecmath.Matrix4f


class PipeModel(val type: PipeTypeEnum) : IBakedModel, IPerspectiveAwareModel {


    internal var faceBakery = FaceBakery()
    internal var texture: TextureAtlasSprite? = null

    var state: IExtendedBlockState? = null

    init {
        texture = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(type.textureName)
    }

    constructor(state: IExtendedBlockState, PType: PipeTypeEnum) : this(PType) {
        this.state = state
    }


    override fun getQuads(p0: IBlockState?, p1: EnumFacing?, p2: Long): MutableList<BakedQuad>? {
        val list = ArrayList<BakedQuad>()
        val uv = BlockFaceUV(floatArrayOf(0.0f, 0.0f, 16.0f, 16.0f), 0)
        val face = BlockPartFace(null, 0, "", uv)
        val thickness = type.thickness
        val lastThickness = 16 - type.thickness
        addCubeToList(Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!)
        if (state != null) {
            if (state!!.getValue(FluxedRedstone.stateHelper.UP)) {
                addCubeToList(Vecs3dCube(thickness, lastThickness, thickness, lastThickness, 16.0, lastThickness), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.DOWN)) {
                addCubeToList(Vecs3dCube(thickness, 0.0, thickness, lastThickness, thickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.NORTH)) {
                addCubeToList(Vecs3dCube(thickness, thickness, 0.0, lastThickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.SOUTH)) {
                addCubeToList(Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, 16.0), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.EAST)) {
                addCubeToList(Vecs3dCube(thickness, thickness, thickness, 16.0, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.WEST)) {
                addCubeToList(Vecs3dCube(0.0, thickness, thickness, lastThickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!)
            }
        }
        return list
    }


    fun addCubeToList(cube: Vecs3dCube, list: ArrayList<BakedQuad>, face: BlockPartFace, modelRotation: ModelRotation, cubeTexture: TextureAtlasSprite) {
        list.add(faceBakery.makeBakedQuad(Vector3f(cube.minX.toFloat(), cube.minY.toFloat(), cube.minZ.toFloat()), Vector3f(cube.maxX.toFloat(), cube.minY.toFloat(), cube.maxZ.toFloat()), face, cubeTexture, EnumFacing.DOWN, modelRotation, null, true, true))//down
        list.add(faceBakery.makeBakedQuad(Vector3f(cube.minX.toFloat(), cube.maxY.toFloat(), cube.minZ.toFloat()), Vector3f(cube.maxX.toFloat(), cube.maxY.toFloat(), cube.maxZ.toFloat()), face, cubeTexture, EnumFacing.UP, modelRotation, null, true, true))//up
        list.add(faceBakery.makeBakedQuad(Vector3f(cube.minX.toFloat(), cube.minY.toFloat(), cube.minZ.toFloat()), Vector3f(cube.maxX.toFloat(), cube.maxY.toFloat(), cube.maxZ.toFloat()), face, cubeTexture, EnumFacing.NORTH, modelRotation, null, true, true))//north
        list.add(faceBakery.makeBakedQuad(Vector3f(cube.minX.toFloat(), cube.minY.toFloat(), cube.maxZ.toFloat()), Vector3f(cube.maxX.toFloat(), cube.maxY.toFloat(), cube.maxZ.toFloat()), face, cubeTexture, EnumFacing.SOUTH, modelRotation, null, true, true))//south
        list.add(faceBakery.makeBakedQuad(Vector3f(cube.maxX.toFloat(), cube.minY.toFloat(), cube.minZ.toFloat()), Vector3f(cube.maxX.toFloat(), cube.maxY.toFloat(), cube.maxZ.toFloat()), face, cubeTexture, EnumFacing.EAST, modelRotation, null, true, true))//east
        list.add(faceBakery.makeBakedQuad(Vector3f(cube.minX.toFloat(), cube.minY.toFloat(), cube.minZ.toFloat()), Vector3f(cube.minX.toFloat(), cube.maxY.toFloat(), cube.maxZ.toFloat()), face, cubeTexture, EnumFacing.WEST, modelRotation, null, true, true))//west
    }

    override fun isAmbientOcclusion(): Boolean {
        return false
    }

    override fun isGui3d(): Boolean {
        return true
    }

    override fun isBuiltInRenderer(): Boolean {
        return false
    }

    override fun getParticleTexture(): TextureAtlasSprite {
        return texture!!
    }

    override fun getItemCameraTransforms(): ItemCameraTransforms? {
        return ItemCameraTransforms.DEFAULT
    }


//    override fun handlePerspective(p0: ItemCameraTransforms.TransformType?): Pair<out IFlexibleBakedModel, javax.vecmath.Matrix4f>? {
//        if (p0 == ItemCameraTransforms.TransformType.FIRST_PERSON || p0 == ItemCameraTransforms.TransformType.GUI) {
//            return Pair.of(IFlexibleBakedModel::class.java.cast(this), ClientStatics.matrix)
//        }
//        return Pair.of(IFlexibleBakedModel::class.java.cast(this), null);
//    }
//

    override fun getOverrides(): ItemOverrideList? {
        return ItemOverrideList.NONE
    }

    override fun handlePerspective(p0: ItemCameraTransforms.TransformType?): Pair<out IBakedModel, Matrix4f>? {
        return Pair.of(IBakedModel::class.java.cast(this), null);
    }
}