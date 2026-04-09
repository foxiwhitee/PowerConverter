package foxiwhitee.PowerConverter.items;

import foxiwhitee.FoxLib.utils.helpers.EnergyUtility;
import foxiwhitee.FoxLib.utils.helpers.LocalizationUtils;
import foxiwhitee.PowerConverter.api.IPowerConverterUpgradeItem;
import foxiwhitee.PowerConverter.config.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemPowerConverterUpgrade extends ItemWithMeta implements IPowerConverterUpgradeItem {
    public ItemPowerConverterUpgrade() {
        super("powerConverterUpgrade", "EU", "RF");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List<String> list, boolean p_77624_4_) {
        if (ModConfig.enableTooltips) {
            if (stack.getItemDamage() == 0) {
                list.add(LocalizationUtils.localizeF("tooltip.upgrade.pc.storageMult", "§3", "EU", getStorageEnergyEUMultiplier(stack)));
                list.add(LocalizationUtils.localizeF("tooltip.upgrade.pc.outputMult", "§3", "EU", getOutputEnergyEUMultiplier(stack)));
            } else if (stack.getItemDamage() == 1) {
                list.add(LocalizationUtils.localizeF("tooltip.upgrade.pc.storageMult", "§4", "RF", getStorageEnergyRFMultiplier(stack)));
                list.add(LocalizationUtils.localizeF("tooltip.upgrade.pc.outputMult", "§4", "RF", getOutputEnergyRFMultiplier(stack)));
            }
        }
    }

    @Override
    public double getStorageEnergyEUMultiplier(ItemStack stack) {
        if (stack.getItemDamage() == 0) {
            return Math.pow(ModConfig.powerConverterUpgradeEUStorageMultiplier, stack.stackSize);
        } else {
            return 1;
        }
    }

    @Override
    public double getStorageEnergyRFMultiplier(ItemStack stack) {
        if (stack.getItemDamage() == 1) {
            return Math.pow(ModConfig.powerConverterUpgradeRFStorageMultiplier, stack.stackSize);
        } else {
            return 1;
        }
    }

    @Override
    public double getOutputEnergyEUMultiplier(ItemStack stack) {
        if (stack.getItemDamage() == 0) {
            return Math.pow(ModConfig.powerConverterUpgradeEUOutputMultiplier, stack.stackSize);
        } else {
            return 1;
        }
    }

    @Override
    public double getOutputEnergyRFMultiplier(ItemStack stack) {
        if (stack.getItemDamage() == 1) {
            return Math.pow(ModConfig.powerConverterUpgradeRFOutputMultiplier, stack.stackSize);
        } else {
            return 1;
        }
    }
}
