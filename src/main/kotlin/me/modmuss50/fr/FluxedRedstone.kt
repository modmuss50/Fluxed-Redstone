package me.modmuss50.fr

import me.modmuss50.fr.client.PipeModelBakery
import me.modmuss50.fr.mutlipart.IC2Interface
import me.modmuss50.fr.mutlipart.ItemMultipartPipe
import me.modmuss50.fr.mutlipart.PipeTypeEnum
import me.modmuss50.fr.mutlipart.TeslaManager
import me.modmuss50.fr.network.FRNetworkHandler
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import reborncore.RebornCore
import reborncore.RebornRegistry
import reborncore.common.util.CraftingHelper
import reborncore.mcmultipart.multipart.IMultipart
import reborncore.mcmultipart.multipart.MultipartRegistry
import java.util.*

@Mod(modid = "fluxedredstone", name = "FluxedRedstone", version = "@MODVERSION@", dependencies = "required-after:reborncore;required-after:reborncore-mcmultipart;required-after:forge@[12.18.1.2080,);")
class FluxedRedstone {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        config = Config(event.suggestedConfigurationFile)
        config.preInit(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        if (!Loader.isModLoaded("Tesla")) {
            teslaSupport = false
        } else if (teslaSupport) {
            teslaManager = TeslaManager()
        }

        if (!Loader.isModLoaded("IC2")) {
            ic2Support = false
        } else {
            ic2Interface = IC2Interface()
            network = FRNetworkHandler()
            network.registerPackets()
        }

        for (typeEnum in PipeTypeEnum.values()) {
            MultipartRegistry.registerPart(typeEnum.getJavaClass() as Class<out IMultipart>?, "fluxedredstone:fluxedPipe." + typeEnum.friendlyName)
            itemMultiPipe.put(typeEnum, ItemMultipartPipe(typeEnum).setCreativeTab(creativeTab).setUnlocalizedName("fluxedredstone.itemFluxedPipe." + typeEnum.friendlyName))
            RebornRegistry.registerItem(itemMultiPipe[typeEnum], ResourceLocation("fluxedredstone", "itemFluxedPipe." + typeEnum.friendlyName))
            RebornCore.jsonDestroyer.registerObject(itemMultiPipe[typeEnum])
        }

        if (FMLCommonHandler.instance().side == Side.CLIENT) { //Lazy man's proxy
            MinecraftForge.EVENT_BUS.register(PipeModelBakery())
        }

        CraftingHelper.addShapedOreRecipe(ItemStack(itemMultiPipe[PipeTypeEnum.REDSTONE], 6),
                "GRG", "RSR", "GRG",
                'G', ItemStack(Blocks.GLASS_PANE),
                'R', ItemStack(Items.REDSTONE),
                'S', ItemStack(Blocks.STONE))

        CraftingHelper.addShapedOreRecipe(ItemStack(itemMultiPipe[PipeTypeEnum.GOLD], 6),
                "GRG", "RSR", "GRG",
                'G', ItemStack(Blocks.GLASS_PANE),
                'R', ItemStack(Items.GOLD_INGOT),
                'S', ItemStack(itemMultiPipe[PipeTypeEnum.REDSTONE]))

        CraftingHelper.addShapedOreRecipe(ItemStack(itemMultiPipe[PipeTypeEnum.BALZE], 4),
                "GRG", "RSR", "GRG",
                'G', ItemStack(Blocks.IRON_BARS),
                'R', ItemStack(Items.BLAZE_ROD),
                'S', ItemStack(itemMultiPipe[PipeTypeEnum.GOLD]))

        CraftingHelper.addShapedOreRecipe(ItemStack(itemMultiPipe[PipeTypeEnum.ENDER], 4),
                "GRG", "RSR", "GRG",
                'G', ItemStack(Items.GOLD_INGOT),
                'R', ItemStack(Items.ENDER_PEARL),
                'S', ItemStack(Items.GHAST_TEAR))
    }

    @Mod.EventHandler
    fun remap(event: FMLMissingMappingsEvent){
        event.all
                .filter { it.type == GameRegistry.Type.ITEM }
                .forEach {
                    for (typeEnum in PipeTypeEnum.values()) {
                        if(it.resourceLocation.resourceDomain == "minecraft" && it.resourceLocation.resourcePath == "itemfluxedpipe." + typeEnum.friendlyName.toLowerCase()){
                            println("Remapping " + it.resourceLocation.resourcePath)
                            it.remap(itemMultiPipe.get(typeEnum))
                        }
                    }
                }
    }


    class FluxedRedstoneCreativeTab : CreativeTabs("fluxedredstone") {

        override fun getTabIconItem(): ItemStack {
            return ItemStack(FluxedRedstone.itemMultiPipe[PipeTypeEnum.ENDER]!!)
        }
    }


    companion object {
        var creativeTab = FluxedRedstoneCreativeTab()

        lateinit var config : Config

        lateinit var teslaManager : TeslaManager

        var itemMultiPipe = HashMap<PipeTypeEnum, Item>()

        var teslaSupport: Boolean = false

        var RFSupport: Boolean = false

        var ic2Support: Boolean = false

        var rfPerEU: Double = 0.0

        lateinit var ic2Interface: IC2Interface

        lateinit var network: FRNetworkHandler
    }

}
