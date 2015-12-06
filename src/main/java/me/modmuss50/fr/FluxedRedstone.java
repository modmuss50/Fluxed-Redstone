package me.modmuss50.fr;

import me.modmuss50.fr.block.BlockPipe;
import me.modmuss50.fr.client.PipeModelBakery;
import me.modmuss50.fr.tile.TilePipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.api.TextureRegistry;

@Mod(modid = "fluxedredstone", name = "FluxedRedstone", version = "@MODVERSION@")
public class FluxedRedstone {

    public static Block blockPipe;


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        blockPipe = new BlockPipe();
        GameRegistry.registerBlock(blockPipe, "FRPipe").setUnlocalizedName("FRPipe");
        GameRegistry.registerTileEntity(TilePipe.class, "FRTilePipe");
        MinecraftForge.EVENT_BUS.register(new PipeModelBakery());
    }

}
