package me.modmuss50.fr.client

import me.modmuss50.fr.FluxedRedstone
import me.modmuss50.fr.PipeTypeEnum
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.*
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.IPerspectiveAwareModel
import net.minecraftforge.common.property.IExtendedBlockState
import org.apache.commons.lang3.tuple.Pair
import reborncore.client.models.BakedModelUtils
import reborncore.common.misc.vecmath.Vecs3dCube
import java.util.*
import javax.vecmath.Matrix4f


class PipeModel(var type: PipeTypeEnum) : IBakedModel, IPerspectiveAwareModel {


    internal var faceBakery = FaceBakery()
    internal var texture: TextureAtlasSprite? = null

    init {
        texture = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(type.textureName)
    }

    constructor(state: IExtendedBlockState, PType: PipeTypeEnum) : this(PType) {

    }


    override fun getQuads(blockState: IBlockState?, p1: EnumFacing?, p2: Long): MutableList<BakedQuad>? {
        if (blockState != null) {
            type = blockState!!.getValue(FluxedRedstone.stateHelper.typeProp)
            texture = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(type.textureName)
        }
        val list = ArrayList<BakedQuad>()
        val thickness = type.thickness
        val lastThickness = 16 - type.thickness
        val uv = BlockFaceUV(floatArrayOf(0.0f, 0.0f, 16.0f, 16.0f), 0)
        val face = BlockPartFace(null, 0, "", uv)
        BakedModelUtils.addCubeToList(Vecs3dCube(thickness, thickness, thickness, lastThickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!, EnumFacing.DOWN, faceBakery)
        if (blockState is IExtendedBlockState && p1 != null) {
            val state = blockState as IExtendedBlockState
            if (state!!.getValue(FluxedRedstone.stateHelper.UP)) {
                BakedModelUtils.addCubeToList(Vecs3dCube(thickness, lastThickness, thickness, lastThickness, 16.0, lastThickness), list, face, ModelRotation.X0_Y0, texture!!, p1!!, faceBakery)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.DOWN)) {
                BakedModelUtils.addCubeToList(Vecs3dCube(thickness, 0.0, thickness, lastThickness, thickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!, p1!!, faceBakery)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.NORTH)) {
                BakedModelUtils.addCubeToList(Vecs3dCube(0.0, thickness, thickness, thickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y90, texture!!, p1!!, faceBakery)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.SOUTH)) {
                BakedModelUtils.addCubeToList(Vecs3dCube(0.0, thickness, thickness, thickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y270, texture!!, p1!!, faceBakery)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.EAST)) {
                BakedModelUtils.addCubeToList(Vecs3dCube(lastThickness, thickness, thickness, 16.0, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!, p1!!, faceBakery)
            }
            if (state!!.getValue(FluxedRedstone.stateHelper.WEST)) {
                BakedModelUtils.addCubeToList(Vecs3dCube(0.0, thickness, thickness, thickness, lastThickness, lastThickness), list, face, ModelRotation.X0_Y0, texture!!, p1!!, faceBakery)
            }
        }
        return list
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

    override fun getOverrides(): ItemOverrideList? {
        return ItemOverrideList.NONE
    }

    override fun handlePerspective(p0: ItemCameraTransforms.TransformType?): Pair<out IBakedModel, Matrix4f>? {
        return Pair.of(IBakedModel::class.java.cast(this), null);
    }
}