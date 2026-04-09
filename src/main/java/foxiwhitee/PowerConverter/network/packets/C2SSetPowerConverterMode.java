package foxiwhitee.PowerConverter.network.packets;

import foxiwhitee.FoxLib.network.BasePacket;
import foxiwhitee.FoxLib.network.IInfoPacket;
import foxiwhitee.PowerConverter.tile.TilePowerConverter;
import foxiwhitee.PowerConverter.util.ButtonConverterMode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class C2SSetPowerConverterMode extends BasePacket {
    private final int xCoord, yCoord, zCoord;
    private final ButtonConverterMode mode;

    public C2SSetPowerConverterMode(ByteBuf data) {
        super(data);
        mode = ButtonConverterMode.values()[data.readByte()];
        xCoord = data.readInt();
        yCoord = data.readInt();
        zCoord = data.readInt();
    }

    public C2SSetPowerConverterMode(int xCoord, int yCoord, int zCoord, ButtonConverterMode mode) {
        super();
        this.mode = mode;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        ByteBuf data = Unpooled.buffer();
        data.writeInt(getId());
        data.writeByte(mode.ordinal());
        data.writeInt(xCoord);
        data.writeInt(yCoord);
        data.writeInt(zCoord);
        setPacketData(data);
    }

    @Override
    public void handleServerSide(IInfoPacket network, BasePacket packet, EntityPlayer player) {
        if (player != null && player.worldObj != null) {
            TileEntity te = player.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord);
            if (te instanceof TilePowerConverter tile) {
                tile.setMode(mode);
            }
        }
    }
}
