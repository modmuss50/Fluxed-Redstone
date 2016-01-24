package me.modmuss50.fr;

import mcmultipart.multipart.MultipartRegistry;
import me.modmuss50.fr.client.PipeModelBakery;
import me.modmuss50.fr.mutlipart.ItemMultipartPipe;
import me.modmuss50.fr.mutlipart.MultipartPipe;
import me.modmuss50.fr.mutlipart.PipeStateHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import reborncore.RebornCore;
import reborncore.common.util.CraftingHelper;

import java.util.HashMap;

@Mod(modid = "fluxedredstone", name = "FluxedRedstone", version = "@MODVERSION@", dependencies = "required-after:reborncore;required-after:mcmultipart")
public class FluxedRedstone {

    public static HashMap<PipeTypeEnum, Item> itemMultiPipe = new HashMap<>();

    public static PipeStateHelper stateHelper;

    public static FluxedRedstoneCreativeTab creativeTab;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        stateHelper = new PipeStateHelper();
        creativeTab = new FluxedRedstoneCreativeTab();


        for(PipeTypeEnum typeEnum : PipeTypeEnum.values()){
            MultipartRegistry.registerPart(typeEnum.getClassType(), "fluxedredstone:fluxedPipe." + typeEnum.getFriendlyName());
            itemMultiPipe.put(typeEnum, new ItemMultipartPipe(typeEnum).setCreativeTab(creativeTab).setUnlocalizedName("fluxedredstone.itemFluxedPipe." + typeEnum.getFriendlyName()));
            GameRegistry.registerItem(itemMultiPipe.get(typeEnum), "itemFluxedPipe." + typeEnum.getFriendlyName());
            RebornCore.jsonDestroyer.registerObject(itemMultiPipe.get(typeEnum));
        }

        if(FMLCommonHandler.instance().getSide() == Side.CLIENT){ //Lazy man's proxy
            MinecraftForge.EVENT_BUS.register(new PipeModelBakery());
        }

        CraftingHelper.addShapedOreRecipe(new ItemStack(itemMultiPipe.get(PipeTypeEnum.REDSTONE), 6),
                "GRG", "RSR", "GRG",
                'G', new ItemStack(Blocks.glass_pane),
                'R', new ItemStack(Items.redstone),
                'S', new ItemStack(Blocks.stone));

        CraftingHelper.addShapedOreRecipe(new ItemStack(itemMultiPipe.get(PipeTypeEnum.GOLD), 6),
                "GRG", "RSR", "GRG",
                'G', new ItemStack(Blocks.glass_pane),
                'R', new ItemStack(Items.gold_ingot),
                'S', new ItemStack(itemMultiPipe.get(PipeTypeEnum.REDSTONE)));

        CraftingHelper.addShapedOreRecipe(new ItemStack(itemMultiPipe.get(PipeTypeEnum.BALZE), 4),
                "GRG", "RSR", "GRG",
                'G', new ItemStack(Blocks.iron_bars),
                'R', new ItemStack(Items.blaze_rod),
                'S', new ItemStack(itemMultiPipe.get(PipeTypeEnum.GOLD)));

        CraftingHelper.addShapedOreRecipe(new ItemStack(itemMultiPipe.get(PipeTypeEnum.ENDER), 4),
                "GRG", "RSR", "GRG",
                'G', new ItemStack(Items.gold_ingot),
                'R', new ItemStack(Items.ender_pearl),
                'S', new ItemStack(Items.ghast_tear));


    }

    public static class FluxedRedstoneCreativeTab extends CreativeTabs {

        public FluxedRedstoneCreativeTab() {
            super("fluxedredstone");
        }

        @Override
        public Item getTabIconItem() {
            return itemMultiPipe.get(PipeTypeEnum.ENDER);
        }
    }

}
