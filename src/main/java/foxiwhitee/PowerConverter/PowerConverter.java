package foxiwhitee.PowerConverter;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxiwhitee.PowerConverter.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import static foxiwhitee.PowerConverter.PowerConverter.*;

@Mod(modid = MODID, name = MODNAME, version = VERSION, dependencies = DEPEND)
public class PowerConverter {
    public static final String
        MODID = "powerconverter",
        MODNAME = "Power Converter",
        VERSION = "1.0.0",
        DEPEND = "required-after:IC2;required-after:CoFHCore;required-after:foxlib;";

    public static final CreativeTabs TAB = new CreativeTabs("POWER_CONVERTER_TAB") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(CommonProxy.powerConverter);
        }
    };

    @Mod.Instance(MODID)
    public static PowerConverter instance;

    @SidedProxy(clientSide = "foxiwhitee.PowerConverter.proxy.ClientProxy", serverSide = "foxiwhitee.PowerConverter.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
