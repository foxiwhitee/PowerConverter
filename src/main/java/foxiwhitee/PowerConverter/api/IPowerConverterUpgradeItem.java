package foxiwhitee.PowerConverter.api;

import net.minecraft.item.ItemStack;

public interface IPowerConverterUpgradeItem {
    double getStorageEnergyEUMultiplier(ItemStack stack);
    double getStorageEnergyRFMultiplier(ItemStack stack);
    double getOutputEnergyEUMultiplier(ItemStack stack);
    double getOutputEnergyRFMultiplier(ItemStack stack);
}
