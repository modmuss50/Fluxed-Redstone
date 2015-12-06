package me.modmuss50.fr;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import org.lwjgl.util.vector.Vector3f;
import reborncore.common.misc.vecmath.Vecs3dCube;

import java.util.ArrayList;
import java.util.List;

public class PipeModel implements ISmartBlockModel {

    static FaceBakery faceBakery = new FaceBakery();
    TextureAtlasSprite texture = null;

    public PipeModel() {
        texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
    }

    public PipeModel(IBlockState blockState) {
        String textureName = blockState.getValue(BlockPipe.TYPE_PROP).textureName;
        texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(textureName);
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        return new PipeModel(state);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
        return new ArrayList<>();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        ArrayList<BakedQuad> list = new ArrayList();
        BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
        BlockPartFace face = new BlockPartFace(null, 0, "", uv);
        addCubeToList(new Vecs3dCube(4, 4, 4, 12, 12, 12), list, face, ModelRotation.X0_Y0);
        return list;
    }

    public void addCubeToList(Vecs3dCube cube, ArrayList<BakedQuad> list, BlockPartFace face, ModelRotation modelRotation){
        list.add(faceBakery.makeBakedQuad(new Vector3f((float)cube.getMinX(), (float)cube.getMinY(), (float)cube.getMinZ()), new Vector3f((float)cube.getMaxX(), (float)cube.getMinY(), (float)cube.getMaxZ()), face, texture, EnumFacing.DOWN, modelRotation, null, true, true));//down
        list.add(faceBakery.makeBakedQuad(new Vector3f((float)cube.getMinX(), (float)cube.getMaxY(), (float)cube.getMinZ()), new Vector3f((float)cube.getMaxX(), (float)cube.getMaxY(), (float)cube.getMaxZ()), face, texture, EnumFacing.UP, modelRotation, null, true, true));//up
        list.add(faceBakery.makeBakedQuad(new Vector3f((float)cube.getMinX(), (float)cube.getMinY(), (float)cube.getMinZ()), new Vector3f((float)cube.getMaxX(), (float)cube.getMaxY(), (float)cube.getMaxZ()), face, texture, EnumFacing.NORTH, modelRotation, null, true, true));//north
        list.add(faceBakery.makeBakedQuad(new Vector3f((float)cube.getMinX(), (float)cube.getMinY(), (float)cube.getMaxZ()), new Vector3f((float)cube.getMaxZ(), (float)cube.getMaxY(), (float)cube.getMaxZ()), face, texture, EnumFacing.SOUTH, modelRotation, null, true, true));//south
        list.add(faceBakery.makeBakedQuad(new Vector3f((float)cube.getMaxX(), (float)cube.getMinY(), (float)cube.getMinZ()), new Vector3f((float)cube.getMaxX(), (float)cube.getMaxY(), (float)cube.getMaxZ()), face, texture, EnumFacing.EAST, modelRotation, null, true, true));//east
        list.add(faceBakery.makeBakedQuad(new Vector3f((float)cube.getMinX(), (float)cube.getMinY(), (float)cube.getMinZ()), new Vector3f((float)cube.getMinX(), (float)cube.getMaxY(), (float)cube.getMaxZ()), face, texture, EnumFacing.WEST, modelRotation, null, true, true));//west
    }


    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return texture;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }
}
