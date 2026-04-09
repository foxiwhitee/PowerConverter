package foxiwhitee.PowerConverter.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxiwhitee.FoxLib.api.FoxLibApi;
import foxiwhitee.FoxLib.block.FoxTileBlock;
import foxiwhitee.FoxLib.container.slots.SlotFiltered;
import foxiwhitee.FoxLib.items.ModItemBlock;
import foxiwhitee.FoxLib.recipes.json.annotations.RecipesLocation;
import foxiwhitee.FoxLib.registries.RegisterUtils;
import foxiwhitee.PowerConverter.PowerConverter;
import foxiwhitee.PowerConverter.api.IPowerConverterUpgradeItem;
import foxiwhitee.PowerConverter.blocks.BlockPowerConverter;
import foxiwhitee.PowerConverter.container.ContainerPowerConverter;
import foxiwhitee.PowerConverter.items.DefaultItem;
import foxiwhitee.PowerConverter.items.ItemPowerConverterUpgrade;
import foxiwhitee.PowerConverter.network.packets.C2SSetPowerConverterMode;
import foxiwhitee.PowerConverter.tile.TilePowerConverter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CommonProxy {
    @RecipesLocation(modId = PowerConverter.MODID)
    public static final String[] recipes = {"recipes"};

    public static final String FILTER_POWER_CONVERTER = "powerConverter";

    public static final Item powerConverterUpgrade = new ItemPowerConverterUpgrade();
    public static final Item baseUpgrade = new DefaultItem("baseUpgrade");

    public static final Block powerConverter = new BlockPowerConverter();

    public void preInit(FMLPreInitializationEvent event) {
        RegisterUtils.registerItems(baseUpgrade, powerConverterUpgrade);
        RegisterUtils.registerBlock(powerConverter, ModItemBlock.class);
        RegisterUtils.registerTile(TilePowerConverter.class);
    }

    public void init(FMLInitializationEvent event) {
        FoxLibApi.instance.registries().registerPacket().register(C2SSetPowerConverterMode.class);
        FoxLibApi.instance.registries().registerGui().register(BlockPowerConverter.class, TilePowerConverter.class, ContainerPowerConverter.class);
    }

    public void postInit(FMLPostInitializationEvent event) {
        SlotFiltered.filters.put(FILTER_POWER_CONVERTER, stack -> stack.getItem() instanceof IPowerConverterUpgradeItem);
    }
}
