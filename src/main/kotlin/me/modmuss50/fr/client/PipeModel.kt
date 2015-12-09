package me.modmuss50.fr.client

import me.modmuss50.fr.WorldState
import me.modmuss50.fr.tile.TilePipe
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.*
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.client.resources.model.ModelRotation
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.ISmartBlockModel
import org.lwjgl.util.vector.Vector3f
import reborncore.common.misc.vecmath.Vecs3dCube
import java.util.*


class PipeModel : ISmartBlockModel {

    internal var faceBakery = FaceBakery()
    internal var texture: TextureAtlasSprite? = null
    internal var capTexture: TextureAtlasSprite? = null

    var tile: TilePipe? = null

    init {
        texture = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite("minecraft:blocks/stone")
        capTexture = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite("minecraft:blocks/iron_block")
    }

    constructor(state: WorldState) {
        tile = state.blockAccess.getTileEntity(state.pos) as TilePipe?
        //TODO load cap texture
    }

    constructor() {

    }

    override fun handleBlockState(state: IBlockState): IBakedModel {
        if (state is  WorldState) {
            return PipeModel(state)
        }
        return null!!;
    }

    override fun getFaceQuads(p_177551_1_: EnumFacing): List<BakedQuad> {
        return ArrayList()
    }

    override fun getGeneralQuads(): List<BakedQuad> {
        val list = ArrayList<BakedQuad>()
        val uv = BlockFaceUV(floatArrayOf(0.0f, 0.0f, 16.0f, 16.0f), 0)
        val face = BlockPartFace(null, 0, "", uv)
        addCubeToList(Vecs3dCube(4.0, 4.0, 4.0, 12.0, 12.0, 12.0), list, face, ModelRotation.X0_Y0, texture!!)
        if (tile != null) {
            val pipe = tile
            if (pipe.connects(EnumFacing.UP)) {
                addCubeToList(Vecs3dCube(4.0, 12.0, 4.0, 12.0, 16.0, 12.0), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (pipe.connects(EnumFacing.DOWN)) {
                addCubeToList(Vecs3dCube(4.0, 0.0, 4.0, 12.0, 4.0, 12.0), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (pipe.connects(EnumFacing.NORTH)) {
                addCubeToList(Vecs3dCube(4.0, 4.0, 0.0, 12.0, 12.0, 12.0), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (pipe.connects(EnumFacing.SOUTH)) {
                addCubeToList(Vecs3dCube(4.0, 4.0, 4.0, 12.0, 12.0, 16.0), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (pipe.connects(EnumFacing.EAST)) {
                addCubeToList(Vecs3dCube(4.0, 4.0, 4.0, 16.0, 12.0, 12.0), list, face, ModelRotation.X0_Y0, texture!!)
            }
            if (pipe.connects(EnumFacing.WEST)) {
                addCubeToList(Vecs3dCube(0.0, 4.0, 4.0, 12.0, 12.0, 12.0), list, face, ModelRotation.X0_Y0, texture!!)
            }

            if (pipe.hasCap(EnumFacing.UP)) {
                addCubeToList(Vecs3dCube(4.0, 12.0, 4.0, 12.0, 16.0, 12.0), list, face, ModelRotation.X0_Y0, capTexture!!)
            }
            if (pipe.hasCap(EnumFacing.DOWN)) {
                addCubeToList(Vecs3dCube(4.0, 0.0, 4.0, 12.0, 4.0, 12.0), list, face, ModelRotation.X0_Y0, capTexture!!)
            }
            if (pipe.hasCap(EnumFacing.NORTH)) {
                addCubeToList(Vecs3dCube(4.0, 4.0, 4.0, 12.0, 12.0, 0.0), list, face, ModelRotation.X0_Y0, capTexture!!)
            }
            if (pipe.hasCap(EnumFacing.SOUTH)) {
                addCubeToList(Vecs3dCube(4.0, 4.0, 12.0, 12.0, 12.0, 16.0), list, face, ModelRotation.X0_Y0, capTexture!!)
            }
            if (pipe.hasCap(EnumFacing.EAST)) {
                addCubeToList(Vecs3dCube(12.0, 4.0, 4.0, 16.0, 12.0, 12.0), list, face, ModelRotation.X0_Y0, capTexture!!)
            }
            if (pipe.hasCap(EnumFacing.WEST)) {
                addCubeToList(Vecs3dCube(4.0, 4.0, 4.0, 0.0, 12.0, 12.0), list, face, ModelRotation.X0_Y0, capTexture!!)
            }
        }

        return list
    }

    fun addCubeToList(cube: Vecs3dCube, list: ArrayList<BakedQuad>, face: BlockPartFace, modelRotation: ModelRotation, cubeTexture : TextureAtlasSprite) {
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
        return null
    }
}