package me.moose.gaia.common.packet.handler;

import me.moose.gaia.common.packet.packets.master.cosmetics.GaiaMasterCosmeticListUpdate;
import me.moose.gaia.common.packet.packets.master.friend.GaiaMasterUserFriendUpdatePacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterSlaveRestartPacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterStatusPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserInteractionPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserKickPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserMessagePacket;

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

    void handle(GaiaMasterUserKickPacket packet);

    void handle(GaiaMasterUserInteractionPacket.Crash packet);
    void handle(GaiaMasterUserMessagePacket.ConsoleMessage packet);
    void handle(GaiaMasterUserMessagePacket.Notification packet);

    void handle(GaiaMasterUserFriendUpdatePacket packet);
    void handle(GaiaMasterSlaveRestartPacket packet);




}
