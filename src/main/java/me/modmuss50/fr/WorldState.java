package me.modmuss50.fr;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;


public class WorldState extends BlockState {

    public IBlockAccess blockAccess;

    public BlockPos pos;

    public WorldState(IBlockAccess w, BlockPos p, Block blockIn, IProperty... properties) {
        super(blockIn, properties);
        this.blockAccess = w;
        this.pos = p;
    }

}