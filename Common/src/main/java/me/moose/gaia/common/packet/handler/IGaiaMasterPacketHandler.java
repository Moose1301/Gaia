package me.moose.gaia.common.packet.handler;

import me.moose.gaia.common.packet.packets.slave.friend.GaiaSlaveUserFriendStatusChangePacket;
import me.moose.gaia.common.packet.packets.slave.friend.GaiaSlaveUserRequestStateUpdatePacket;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveHeartbeatPacket;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveStatusPacket;
import me.moose.gaia.common.packet.packets.slave.user.*;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
public interface IGaiaMasterPacketHandler extends IGaiaPacketHandler {
    void handle(GaiaSlaveStatusPacket.Startup  packet);
    void handle(GaiaSlaveStatusPacket.Started packet);
    void handle(GaiaSlaveStatusPacket.Shutdown packet);
    void handle(GaiaSlaveHeartbeatPacket packet);
    void handle(GaiaSlaveUserJoinPacket packet);
    void handle(GaiaSlaveUserLeavePacket packet);
    void handle(GaiaSlaveRequestUserDataPacket packet);

    void handle(GaiaSlaveUserCrashReportPacket packet);

    void handle(GaiaSlaveUserRequestStateUpdatePacket packet);

    void handle(GaiaSlaveUserFriendStatusChangePacket packet);
    void handle(GaiaSlaveUserConsoleMessagePacket packet);
}
