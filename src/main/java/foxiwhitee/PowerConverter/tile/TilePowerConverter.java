package foxiwhitee.PowerConverter.tile;

import cofh.api.energy.IEnergyHandler;
import foxiwhitee.FoxLib.api.energy.IDoubleEnergyHandler;
import foxiwhitee.FoxLib.tile.FoxBaseInvTile;
import foxiwhitee.FoxLib.tile.event.TileEvent;
import foxiwhitee.FoxLib.tile.event.TileEventType;
import foxiwhitee.FoxLib.tile.inventory.FoxInternalInventory;
import foxiwhitee.FoxLib.tile.inventory.InvOperation;
import foxiwhitee.FoxLib.utils.helpers.EnergyUtility;
import foxiwhitee.PowerConverter.api.IPowerConverterUpgradeItem;
import foxiwhitee.PowerConverter.config.ModConfig;
import foxiwhitee.PowerConverter.util.ButtonConverterMode;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TilePowerConverter extends FoxBaseInvTile implements IEnergyHandler, IDoubleEnergyHandler, IEnergySource, IEnergySink {
    private final FoxInternalInventory inventory = new FoxInternalInventory(this, 3);
    private double maxEnergyEU, energyEU, energyRF, maxEnergyRF, outputEU, outputRF;
    private ButtonConverterMode mode = ButtonConverterMode.EU;
    public boolean loaded, addedToEnergyNet, initialized;

    public TilePowerConverter() {
        this.maxEnergyEU = ModConfig.powerConverterEUStorage;
        this.maxEnergyRF = ModConfig.powerConverterRFStorage;
        this.outputEU = ModConfig.powerConverterEUPerTick;
        this.outputRF = ModConfig.powerConverterRFPerTick;
    }

    @TileEvent(TileEventType.TICK)
    public void tick() {
        if (!this.initialized && super.worldObj != null) {
            this.initialize();
        }
        if (worldObj.isRemote) {
            return;
        }
        if (mode == ButtonConverterMode.EU) {
            double needEnergy = maxEnergyEU - energyEU;
            if (needEnergy > 0 && energyRF > 0 && energyRF % ModConfig.rfInEu == 0) {
                double canGetEnergy = energyRF / ModConfig.rfInEu;
                energyRF -= Math.min(needEnergy, canGetEnergy) * ModConfig.rfInEu;
                energyEU += Math.min(needEnergy, canGetEnergy);
                markForUpdate();
            }
        } else if (mode == ButtonConverterMode.RF) {
            pushEnergy();
            double needEnergy = maxEnergyRF - energyRF;
            if (needEnergy > 0 && energyEU > 0) {
                if (needEnergy % ModConfig.rfInEu != 0) {
                    needEnergy -= needEnergy % ModConfig.rfInEu;
                }
                double canGetEnergy = energyEU * ModConfig.rfInEu;
                energyEU -= Math.min(needEnergy, canGetEnergy) / ModConfig.rfInEu;
                energyRF += Math.min(needEnergy, canGetEnergy);
                markForUpdate();
            }
        }
    }

    private void pushEnergy() {
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            double pushedEnergy = EnergyUtility.pushEnergy(ModConfig.rfInEu, side, energyRF, outputRF, this, true, false);
            this.energyRF -= pushedEnergy;
        }
    }

    @TileEvent(TileEventType.SERVER_NBT_WRITE)
    public void writeToNbt_(NBTTagCompound data) {
        data.setDouble("energyEU", energyEU);
        data.setDouble("maxEnergyEU", maxEnergyEU);
        data.setDouble("energyRF", energyRF);
        data.setDouble("maxEnergyRF", maxEnergyRF);
        data.setDouble("outputEU", outputEU);
        data.setDouble("outputRF", outputRF);
        data.setByte("mode", (byte) mode.ordinal());
    }

    @TileEvent(TileEventType.SERVER_NBT_READ)
    public void readFromNbt_(NBTTagCompound data) {
        this.energyEU = data.getDouble("energyEU");
        this.maxEnergyEU = data.getDouble("maxEnergyEU");
        this.energyRF = data.getDouble("energyRF");
        this.maxEnergyRF = data.getDouble("maxEnergyRF");
        this.outputEU = data.getDouble("outputEU");
        this.outputRF = data.getDouble("outputRF");
        this.mode = ButtonConverterMode.values()[data.getByte("mode")];
    }

    @TileEvent(TileEventType.CLIENT_NBT_WRITE)
    public void writeToStream(ByteBuf data) {
        data.writeDouble(energyEU);
        data.writeDouble(maxEnergyEU);
        data.writeDouble(energyRF);
        data.writeDouble(maxEnergyRF);
        data.writeDouble(outputEU);
        data.writeDouble(outputRF);
        data.writeByte((byte) mode.ordinal());
    }

    @TileEvent(TileEventType.CLIENT_NBT_READ)
    public boolean readFromStream(ByteBuf data) {
        double oldEnergyEU = energyEU;
        double oldMaxEnergyEU = maxEnergyEU;
        double oldEnergyRF = energyRF;
        double oldMaxEnergyRF = maxEnergyRF;
        double oldOutputEU = outputEU;
        double oldOutputRF = outputRF;
        ButtonConverterMode oldMode = mode;
        this.energyEU = data.readDouble();
        this.maxEnergyEU = data.readDouble();
        this.energyRF = data.readDouble();
        this.maxEnergyRF = data.readDouble();
        this.outputEU = data.readDouble();
        this.outputRF = data.readDouble();
        this.mode = ButtonConverterMode.values()[data.readByte()];
        return oldEnergyEU != energyEU || oldMaxEnergyEU != maxEnergyEU || oldEnergyRF != energyRF || oldMaxEnergyRF != maxEnergyRF || oldMode != mode || oldOutputEU != outputEU || oldOutputRF != outputRF;
    }

    public void validate() {
        super.validate();
        if (!this.isInvalid() && super.worldObj.blockExists(super.xCoord, super.yCoord, super.zCoord)) {
            this.onLoaded();
        }
    }

    public void invalidate() {
        if (this.loaded) {
            this.onUnloaded();
        }

        super.invalidate();
    }

    public void onLoaded() {
        if (!super.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }

        this.loaded = true;
    }

    public void onChunkUnload() {
        if (this.loaded) {
            this.onUnloaded();
        }

        super.onChunkUnload();
    }

    public void onUnloaded() {
        if (!super.worldObj.isRemote && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        this.loaded = false;
    }

    public void initialize() {
        this.initialized = true;
        if (!this.addedToEnergyNet) {
            this.onLoaded();
        }
    }
    @Override
    public int getSinkTier() {
        return 1;
    }

    @Override
    public FoxInternalInventory getInternalInventory() {
        return inventory;
    }

    @Override
    public int[] getAccessibleSlotsBySide(ForgeDirection var1) {
        return new int[0];
    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {
        if (iInventory == inventory) {
            this.maxEnergyEU = ModConfig.powerConverterEUStorage;
            this.maxEnergyRF = ModConfig.powerConverterRFStorage;
            this.outputEU = ModConfig.powerConverterEUPerTick;
            this.outputRF = ModConfig.powerConverterRFPerTick;
            for (int j = 0; j < inventory.getSizeInventory(); j++) {
                ItemStack stack = inventory.getStackInSlot(j);
                if (stack != null) {
                    if (stack.getItem() instanceof IPowerConverterUpgradeItem item) {
                        this.maxEnergyEU = safeMultiply(maxEnergyEU, item.getStorageEnergyEUMultiplier(stack));
                        this.maxEnergyRF = safeMultiply(maxEnergyRF, item.getStorageEnergyRFMultiplier(stack));
                        this.outputEU *= item.getOutputEnergyEUMultiplier(stack);
                        this.outputRF *= item.getOutputEnergyRFMultiplier(stack);
                    }
                }
            }
            if (this.maxEnergyEU < 0) {
                this.maxEnergyEU = Double.MAX_VALUE;
            }
            if (this.maxEnergyRF < 0) {
                this.maxEnergyRF = Double.MAX_VALUE;
            }
            this.energyEU = Math.min(this.energyEU, this.maxEnergyEU);
            this.energyRF = Math.min(this.energyRF, this.maxEnergyRF);
            markForUpdate();
        }
    }

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return (int) Math.min(Integer.MAX_VALUE, receiveDoubleEnergy(forgeDirection, i, b));
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return (int) Math.min(Integer.MAX_VALUE, receiveDoubleEnergy(forgeDirection, i, b));
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return (int) Math.min(Integer.MAX_VALUE, getDoubleEnergyStored(forgeDirection));
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return (int) Math.min(Integer.MAX_VALUE, getMaxDoubleEnergyStored(forgeDirection));
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return true;
    }

    @Override
    public double extractDoubleEnergy(ForgeDirection direction, double maxExtract, boolean simulate) {
        if (mode == ButtonConverterMode.EU) {
            return 0;
        }
        double energyExtracted = Math.min(energyRF, Math.min(outputRF, maxExtract));

        if (!simulate) {
            energyRF -= energyExtracted;
            markForUpdate();
        }
        return energyExtracted;
    }

    @Override
    public double receiveDoubleEnergy(ForgeDirection direction, double maxReceive, boolean simulate) {
        if (mode == ButtonConverterMode.RF) {
            return 0;
        }
        double energyReceived = Math.min(maxEnergyRF - energyRF, outputRF);

        if (!simulate) {
            energyRF += energyReceived;
            markForUpdate();
        }
        return energyReceived;
    }

    @Override
    public double getDoubleEnergyStored(ForgeDirection direction) {
        return energyRF;
    }

    @Override
    public double getMaxDoubleEnergyStored(ForgeDirection direction) {
        return maxEnergyRF;
    }

    @Override
    public boolean canConnectDoubleEnergy(ForgeDirection direction) {
        return true;
    }

    @Override
    public double getOfferedEnergy() {
        if (this.mode == ButtonConverterMode.EU)
            return Math.min(this.energyRF, this.outputEU);
        return 0.0D;
    }

    @Override
    public double getDemandedEnergy() {
        if (this.mode == ButtonConverterMode.RF)
            return Math.max(this.maxEnergyEU - this.energyEU, 0.0D);
        return 0.0D;
    }

    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double amount, double v1) {
        if (this.mode == ButtonConverterMode.RF) {
            if (this.energyEU >= this.maxEnergyEU) {
                return amount;
            }
            double add = Math.min(amount, maxEnergyEU - energyEU);
            this.energyEU += add;
            markForUpdate();
            return 0.0D;
        }
        return amount;
    }

    @Override
    public void drawEnergy(double v) {
        this.energyEU -= v;
        if (this.energyEU > this.maxEnergyEU) {
            this.energyEU = this.maxEnergyEU;
        }
        markForUpdate();
    }

    @Override
    public int getSourceTier() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return mode == ButtonConverterMode.EU;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return mode == ButtonConverterMode.RF;
    }

    public void setMode(ButtonConverterMode mode) {
        this.mode = mode;
        onUnloaded();
        initialize();
        markForUpdate();
    }

    public ButtonConverterMode getMode() {
        return mode;
    }

    public double getEnergyEU() {
        return energyEU;
    }

    public double getMaxEnergyEU() {
        return maxEnergyEU;
    }

    public double getEnergyRF() {
        return energyRF;
    }

    public double getMaxEnergyRF() {
        return maxEnergyRF;
    }

    public double getOutputEU() {
        return outputEU;
    }

    public double getOutputRF() {
        return outputRF;
    }

    private static double safeMultiply(double a, double b) {
        if (a == 0 || b == 0) return 0;
        if (Double.isNaN(a) || Double.isNaN(b)) return Double.NaN;
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            return (a > 0 == b > 0) ? Double.MAX_VALUE : -Double.MAX_VALUE;
        }

        if (Math.abs(a) > Double.MAX_VALUE / Math.abs(b)) {
            return (a > 0 == b > 0) ? Double.MAX_VALUE : -Double.MAX_VALUE;
        }

        return a * b;
    }
}
