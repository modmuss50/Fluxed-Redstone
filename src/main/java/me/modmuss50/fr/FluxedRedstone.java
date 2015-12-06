package me.modmuss50.fr;

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
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent
    public void onBakeModel(ModelBakeEvent event) {
        for (PipeType type : PipeType.values()) {
            ResourceLocation resourceLocation = TextureRegistry.getBlockResourceLocation(FluxedRedstone.blockPipe);
                for (int a = 0; a < 2; a++) {
                    for (int b = 0; b < 2; b++) {
                        for (int c = 0; c < 2; c++) {
                            for (int d = 0; d < 2; d++) {
                                for (int e = 0; e < 2; e++) {
                                    for (int f = 0; f < 2; f++) {
                                        ModelResourceLocation resourceLocation1 = new ModelResourceLocation(resourceLocation,
                                                "connecteddown" + (a == 0 ? "true" : "false") +
                                                ",connectedeast" + (b == 0 ? "true" : "false")+
                                                        ",connectednorth" + (c == 0 ? "true" : "false")+
                                                        ",connectedsouth" + (d == 0 ? "true" : "false")+
                                                        ",connectedup" + (e == 0 ? "true" : "false")+
                                                        ",connectedwest" + (f == 0 ? "true" : "false")
                                        );
                                        System.out.println(resourceLocation1.toString());
                                        event.modelRegistry.putObject(resourceLocation1, new PipeModel());
                                    }
                                }
                            }
                        }
                    }
            }



        }

    }


}
