package me.modmuss50.fr;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import reborncore.common.itemblock.ItemBlockBase;

public class ItemBlockPipe extends ItemBlockBase {
    public ItemBlockPipe(Block block) {
        super(block, block, getNames());


        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.pipe:" + PipeType.values()[stack.getMetadata()].name();
    }

    public static String[] getNames(){
        String[] names = new String[PipeType.values().length];
        for(PipeType type : PipeType.values()){
            names[type.ordinal()] = type.getName();
        }
        return names;
    }

}
