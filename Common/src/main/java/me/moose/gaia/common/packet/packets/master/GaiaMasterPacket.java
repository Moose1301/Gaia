package me.moose.gaia.common.packet.packets.master;

import me.moose.gaia.common.packet.GaiaPacket;
import me.moose.gaia.common.packet.PacketRegistry;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
public abstract class GaiaMasterPacket implements GaiaPacket<IGaiaSlavePacketHandler> {
    private String sendingId;
    @Override
    public PacketRegistry getRegistry() {
        return PacketRegistry.MASTER;
    }
    public void setSendingID(String id) {
        this.sendingId = id;
    }
    public String getSendingID() {
        return sendingId;
    }
}
