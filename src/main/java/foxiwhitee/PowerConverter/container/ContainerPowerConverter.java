package foxiwhitee.PowerConverter.container;

import foxiwhitee.FoxLib.container.FoxBaseContainer;
import foxiwhitee.FoxLib.container.slots.SlotFiltered;
import foxiwhitee.PowerConverter.proxy.CommonProxy;
import foxiwhitee.PowerConverter.tile.TilePowerConverter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPowerConverter extends FoxBaseContainer {
    public ContainerPowerConverter(EntityPlayer ip, TilePowerConverter myTile) {
        super(ip, myTile);

        bindPlayerInventory(43, 195);

        for (int l = 0; l < 3; l++) {
            addSlotToContainer(new SlotFiltered(CommonProxy.FILTER_POWER_CONVERTER, myTile.getInternalInventory(), l, 23, 107 + l * 18 + l));
        }
    }
}
