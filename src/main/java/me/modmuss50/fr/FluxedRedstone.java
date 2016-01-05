package me.modmuss50.fr;

import mcmultipart.multipart.MultipartRegistry;
import me.modmuss50.fr.client.PipeModelBakery;
import me.modmuss50.fr.mutlipart.ItemMultipartPipe;
import me.modmuss50.fr.mutlipart.MultipartPipe;
import me.modmuss50.fr.mutlipart.PipeStateHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "fluxedredstone", name = "FluxedRedstone", version = "@MODVERSION@")
public class FluxedRedstone {

    public static Item itemMultiPipe;
    public static PipeStateHelper stateHelper = new PipeStateHelper();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        MultipartRegistry.registerPart(MultipartPipe.class, "fluxedredstone:fluxedPipe");
        itemMultiPipe = new ItemMultipartPipe().setCreativeTab(CreativeTabs.tabRedstone).setUnlocalizedName("fluxedredstone.itemFluxedPipe");
        GameRegistry.registerItem(itemMultiPipe, "itemFluxedPipe");

        MinecraftForge.EVENT_BUS.register(new PipeModelBakery());
    }

}
