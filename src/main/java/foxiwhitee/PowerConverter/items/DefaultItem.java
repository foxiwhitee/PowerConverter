package foxiwhitee.PowerConverter.items;

import foxiwhitee.PowerConverter.PowerConverter;
import net.minecraft.item.Item;

public class DefaultItem extends Item {
    public DefaultItem(String name) {
        this.setUnlocalizedName(name);
        this.setTextureName(PowerConverter.MODID + ":" + name);
        this.setCreativeTab(PowerConverter.TAB);
    }
}
