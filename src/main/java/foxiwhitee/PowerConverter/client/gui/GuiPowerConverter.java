package foxiwhitee.PowerConverter.client.gui;

import foxiwhitee.FoxLib.client.gui.FoxBaseGui;
import foxiwhitee.FoxLib.client.gui.buttons.NoTextureButton;
import foxiwhitee.FoxLib.network.NetworkManager;
import foxiwhitee.FoxLib.utils.helpers.EnergyUtility;
import foxiwhitee.FoxLib.utils.helpers.LocalizationUtils;
import foxiwhitee.FoxLib.utils.helpers.UtilGui;
import foxiwhitee.PowerConverter.PowerConverter;
import foxiwhitee.PowerConverter.container.ContainerPowerConverter;
import foxiwhitee.PowerConverter.network.packets.C2SSetPowerConverterMode;
import foxiwhitee.PowerConverter.tile.TilePowerConverter;
import foxiwhitee.PowerConverter.util.ButtonConverterMode;
import net.minecraft.client.gui.GuiButton;

public class GuiPowerConverter extends FoxBaseGui {
    private final TilePowerConverter tile;

    public GuiPowerConverter(ContainerPowerConverter container) {
        super(container, 262, 277);
        setModID(PowerConverter.MODID);
        tile = (TilePowerConverter) container.getTileEntity();
    }

    @Override
    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.bindTexture(PowerConverter.MODID, this.getBackground());
        ButtonConverterMode mode = tile.getMode();
        UtilGui.drawTexture(106, 138, mode.getRfX(), mode.getRfY(), 14, 14, 14, 14);
        UtilGui.drawTexture(142, 138, mode.getEuX(), mode.getEuY(), 14, 14, 14, 14);
        double storedEnergy = tile.getEnergyEU();
        if (storedEnergy >= 0.0D) {
            double maxStoredEnergy = tile.getMaxEnergyEU();
            double y = UtilGui.gauge(73, storedEnergy, maxStoredEnergy);
            UtilGui.drawTexture(141, 129 - y, 288, 0, 16, y, 16, y);
            drawIfInMouse(mouseX, mouseY, 139, 54, 20, 77,
                LocalizationUtils.formatNumber(storedEnergy) + " / " + LocalizationUtils.formatNumber(maxStoredEnergy) + " EU\n" +
                    LocalizationUtils.localize("tooltip.output", LocalizationUtils.formatNumber(tile.getOutputEU()) + " EU/t"));
            this.bindTexture(PowerConverter.MODID, this.getBackground());
        }
        storedEnergy = tile.getEnergyRF();
        if (storedEnergy >= 0.0D) {
            double maxStoredEnergy = tile.getMaxEnergyRF();
            double y = UtilGui.gauge(73, storedEnergy, maxStoredEnergy);
            UtilGui.drawTexture(105, 129 - y, 272, 0, 16, y, 16, y);
            drawIfInMouse(mouseX, mouseY, 103, 54, 20, 77,
                LocalizationUtils.formatNumber(storedEnergy) + " / " + LocalizationUtils.formatNumber(maxStoredEnergy) + " RF\n" +
                    LocalizationUtils.localize("tooltip.output", LocalizationUtils.formatNumber(tile.getOutputRF()) + " RF/t"));
            this.bindTexture(PowerConverter.MODID, this.getBackground());
        }
    }

    @Override
    protected String getBackground() {
        return "gui/guiPowerConverter.png";
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new NoTextureButton(0, this.guiLeft + 142, this.guiTop + 138, 14, 14, "tooltip.convert.eu"));
        buttonList.add(new NoTextureButton(1, this.guiLeft + 106, this.guiTop + 138, 14, 14, "tooltip.convert.rf"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if (button instanceof NoTextureButton) {
            if (button.id == 0) {
                NetworkManager.instance.sendToServer(new C2SSetPowerConverterMode(tile.xCoord, tile.yCoord, tile.zCoord, ButtonConverterMode.EU));
            } else if (button.id == 1) {
                NetworkManager.instance.sendToServer(new C2SSetPowerConverterMode(tile.xCoord, tile.yCoord, tile.zCoord, ButtonConverterMode.RF));
            }
        }
    }
}
