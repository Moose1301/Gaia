package me.moose.gaia.common.packet.handler;

import me.moose.gaia.common.packet.packets.master.*;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
public interface IGaiaSlavePacketHandler extends IGaiaPacketHandler {
    void handle(GaiaMasterStatusPacket.Startup packet);
    void handle(GaiaMasterStatusPacket.Shutdown packet);
    void handle(GaiaMasterSlaveServerInfoPacket packet);
    void handle(GaiaMasterCosmeticListUpdate packet);

    void handle(GaiaMasterUserDataPacket.Loaded packet);
    void handle(GaiaMasterUserDataPacket.Friends packet);
    void handle(GaiaMasterUserDataPacket.Cosmetics packet);
    void handle(GaiaMasterUserDataPacket.Data packet);


}
