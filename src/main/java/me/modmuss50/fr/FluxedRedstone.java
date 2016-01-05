package me.modmuss50.fr;

import mcmultipart.multipart.MultipartRegistry;
import me.modmuss50.fr.block.BlockPipe;
import me.modmuss50.fr.client.PipeModelBakery;
import me.modmuss50.fr.mutlipart.ItemMultipartPipe;
import me.modmuss50.fr.mutlipart.MultipartPipe;
import me.modmuss50.fr.mutlipart.PipeStateHelper;
import me.modmuss50.fr.tile.TilePipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.api.TextureRegistry;

import java.util.HashMap;

@Mod(modid = "fluxedredstone", name = "FluxedRedstone", version = "@MODVERSION@")
public class FluxedRedstone {

    //public static Block blockPipe;

    public static Item itemMultiPipe;
    public static PipeStateHelper stateHelper = new PipeStateHelper();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
//        blockPipe = new BlockPipe().setUnlocalizedName("FRPipe");
//        GameRegistry.registerBlock(blockPipe, "FRPipe");
//        GameRegistry.registerTileEntity(TilePipe.class, "FRTilePipe");

        MultipartRegistry.registerPart(MultipartPipe.class, "fluxedredstone:fluxedPipe");
        itemMultiPipe = new ItemMultipartPipe().setCreativeTab(CreativeTabs.tabRedstone).setUnlocalizedName("fluxedredstone.itemFluxedPipe");
        GameRegistry.registerItem(itemMultiPipe, "itemFluxedPipe");

        MinecraftForge.EVENT_BUS.register(new PipeModelBakery());
    }

}
