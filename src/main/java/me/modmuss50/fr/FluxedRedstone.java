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

import java.util.HashMap;

@Mod(modid = "fluxedredstone", name = "FluxedRedstone", version = "@MODVERSION@")
public class FluxedRedstone {

    public static HashMap<PipeTypeEnum, Item> itemMultiPipe = new HashMap<>();

    public static PipeStateHelper stateHelper = new PipeStateHelper();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {


        for(PipeTypeEnum typeEnum : PipeTypeEnum.values()){
            MultipartRegistry.registerPart(typeEnum.getClassType(), "fluxedredstone:fluxedPipe." + typeEnum.getFriendlyName());
            itemMultiPipe.put(typeEnum, new ItemMultipartPipe(typeEnum).setCreativeTab(CreativeTabs.tabRedstone).setUnlocalizedName("fluxedredstone.itemFluxedPipe." + typeEnum.getFriendlyName()));
            GameRegistry.registerItem(itemMultiPipe.get(typeEnum), "itemFluxedPipe." + typeEnum.getFriendlyName());
        }


        MinecraftForge.EVENT_BUS.register(new PipeModelBakery());
    }



}
