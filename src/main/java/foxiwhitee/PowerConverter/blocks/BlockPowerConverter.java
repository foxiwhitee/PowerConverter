package foxiwhitee.PowerConverter.blocks;

import foxiwhitee.FoxLib.block.FoxTileBlock;
import foxiwhitee.FoxLib.utils.helpers.LocalizationUtils;
import foxiwhitee.PowerConverter.PowerConverter;
import foxiwhitee.PowerConverter.config.ModConfig;
import foxiwhitee.PowerConverter.tile.TilePowerConverter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlockPowerConverter extends FoxTileBlock {
    public BlockPowerConverter() {
        super(PowerConverter.MODID, "powerConverter", TilePowerConverter.class);
        setCreativeTab(PowerConverter.TAB);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
        if (ModConfig.enableTooltips) {
            list.add(LocalizationUtils.localize("tooltip.powerConverter.description", ModConfig.rfInEu));
            list.add(LocalizationUtils.localizeF("tooltip.powerConverter.storageEU", ModConfig.powerConverterEUStorage));
            list.add(LocalizationUtils.localizeF("tooltip.powerConverter.storageRF", ModConfig.powerConverterRFStorage));
            list.add(LocalizationUtils.localizeF("tooltip.powerConverter.outputEU", ModConfig.powerConverterEUPerTick));
            list.add(LocalizationUtils.localizeF("tooltip.powerConverter.outputRF", ModConfig.powerConverterRFPerTick));
        }
    }
}
