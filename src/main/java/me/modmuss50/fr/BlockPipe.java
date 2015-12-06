package me.modmuss50.fr;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockPipe extends BlockContainer {

    public static PropertyBool connectedNorth = PropertyBool.create("connectedNorth");
    public static PropertyBool connectedSouth = PropertyBool.create("connectedSouth");
    public static PropertyBool connectedEast = PropertyBool.create("connectedEast");
    public static PropertyBool connectedWest = PropertyBool.create("connectedWest");
    public static PropertyBool connectedUp = PropertyBool.create("connectedUp");
    public static PropertyBool connectedDown = PropertyBool.create("connectedDown");

    protected BlockPipe() {
        super(Material.glass);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        setDefaultState(this.blockState.getBaseState());
        this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this,  connectedNorth, connectedSouth, connectedEast, connectedWest, connectedUp, connectedDown);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePipe();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TilePipe pipe = (TilePipe) worldIn.getTileEntity(pos);
        return state
                .withProperty(connectedDown, pipe.connects(EnumFacing.DOWN))
                .withProperty(connectedUp, pipe.connects(EnumFacing.UP))
                .withProperty(connectedNorth, pipe.connects(EnumFacing.NORTH))
                .withProperty(connectedSouth, pipe.connects(EnumFacing.SOUTH))
                .withProperty(connectedWest, pipe.connects(EnumFacing.WEST))
                .withProperty(connectedEast, pipe.connects(EnumFacing.EAST));
    }


    @Override
    public int getRenderType() {
        return 3;
    }

    public boolean isBlockNormalCube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullBlock() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

}
