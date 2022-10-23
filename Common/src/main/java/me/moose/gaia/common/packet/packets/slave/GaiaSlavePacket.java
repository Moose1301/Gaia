package me.moose.gaia.common.packet.packets.slave;

import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.packet.GaiaPacket;
import me.moose.gaia.common.packet.PacketRegistry;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
public abstract class GaiaSlavePacket implements GaiaPacket<IGaiaMasterPacketHandler> {

    private String sendingId;

    @Override
    public PacketRegistry getRegistry() {
        return PacketRegistry.SLAVE;
    }

    public void setSendingID(String id) {
        this.sendingId = id;
    }
    public String getSendingID() {
        return sendingId;
    }
}
