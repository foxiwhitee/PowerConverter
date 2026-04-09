package foxiwhitee.PowerConverter.items;

import foxiwhitee.FoxLib.utils.helpers.LocalizationUtils;
import foxiwhitee.PowerConverter.PowerConverter;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemWithMeta extends Item {
    private final String[] prefixes;
    private final IIcon[] icons;
    private final String name;

    public ItemWithMeta(String name, String... prefixes) {
        this.hasSubtypes = true;
        this.icons = new IIcon[prefixes.length];
        this.prefixes = prefixes;
        this.name = name;
        this.setUnlocalizedName(name);
        this.setTextureName(PowerConverter.MODID + ":" + name);
        this.setCreativeTab(PowerConverter.TAB);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        meta = Math.max(0, Math.min(meta, prefixes.length - 1));
        return icons[meta];
    }

    @Override
    public void registerIcons(IIconRegister register) {
        for (int i = 0; i < prefixes.length; i++) {
            icons[i] = register.registerIcon(PowerConverter.MODID + ":" + name + prefixes[i]);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int meta = Math.min(prefixes.length - 1, (stack != null) ? stack.getItemDamage() : 0);
        return LocalizationUtils.localize(getUnlocalizedName() + "." + prefixes[meta] + ".name");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < prefixes.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
