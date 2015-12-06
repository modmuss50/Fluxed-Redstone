package me.modmuss50.fr;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockPipe extends BlockContainer {

    public static PropertyEnum<PipeType> TYPE_PROP = PropertyEnum.create("type", PipeType.class);

    public static PropertyBool connectedNorth = PropertyBool.create("connectedNorth");
    public static PropertyBool connectedSouth = PropertyBool.create("connectedSouth");
    public static PropertyBool connectedEast = PropertyBool.create("connectedEast");
    public static PropertyBool connectedWest = PropertyBool.create("connectedWest");
    public static PropertyBool connectedUp = PropertyBool.create("connectedUp");
    public static PropertyBool connectedDown = PropertyBool.create("connectedDown");

    protected BlockPipe() {
        super(Material.glass);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE_PROP, PipeType.IRON).withProperty(connectedNorth, false).withProperty(connectedSouth, false).withProperty(connectedEast, false).withProperty(connectedWest, false).withProperty(connectedUp, false).withProperty(connectedDown, false));
        this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
    }

    @Override
    protected BlockState createBlockState() {
        TYPE_PROP = PropertyEnum.create("type", PipeType.class);
        connectedNorth = PropertyBool.create("connectedNorth");
        connectedSouth = PropertyBool.create("connectedSouth");
        connectedEast = PropertyBool.create("connectedEast");
        connectedWest = PropertyBool.create("connectedWest");
        connectedUp = PropertyBool.create("connectedUp");
        connectedDown = PropertyBool.create("connectedDown");

        return new BlockState(this, TYPE_PROP, connectedNorth, connectedSouth, connectedEast, connectedWest, connectedUp, connectedDown);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePipe();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE_PROP, PipeType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE_PROP).ordinal();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (PipeType type : PipeType.values()) {
            list.add(new ItemStack(itemIn, 1, type.ordinal()));
        }
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
