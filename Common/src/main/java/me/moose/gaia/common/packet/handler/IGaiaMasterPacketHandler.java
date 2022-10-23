package me.moose.gaia.common.packet.handler;

import me.moose.gaia.common.packet.packets.slave.*;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
public interface IGaiaMasterPacketHandler extends IGaiaPacketHandler {
    void handle(GaiaSlaveServerStartPacket packet);
    void handle(GaiaSlaveUserJoinPacket packet);
    void handle(GaiaSlaveRequestUserDataPacket packet);
}
