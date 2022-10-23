package me.moose.gaia.master.packet;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;
import me.moose.gaia.common.packet.packets.master.*;
import me.moose.gaia.common.packet.packets.master.cosmetics.GaiaMasterCosmeticListUpdate;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserKickPacket;
import me.moose.gaia.common.packet.packets.slave.friend.GaiaSlaveUserRequestStateUpdatePacket;
import me.moose.gaia.common.packet.packets.slave.user.GaiaSlaveRequestUserDataPacket;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveStatusPacket;
import me.moose.gaia.common.packet.packets.slave.user.GaiaSlaveUserCrashReportPacket;
import me.moose.gaia.common.packet.packets.slave.user.GaiaSlaveUserJoinPacket;
import me.moose.gaia.common.packet.packets.slave.user.GaiaSlaveUserLeavePacket;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.utils.KickConstants;
import me.moose.gaia.master.profile.Profile;
import me.moose.gaia.master.profile.crash.CrashReport;
import me.moose.gaia.master.server.Server;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class GaiaMasterPacketHandler implements IGaiaMasterPacketHandler {
    @Override
    public void handle(GaiaSlaveStatusPacket.Startup packet) {
        Server server = GaiaMaster.getInstance().getServerHandler().addServer(packet.getName(), packet.getRegion());
        GaiaServer.getRedisHandler().sendPacket(new GaiaMasterSlaveServerInfoPacket(server.getId()), server.getRealId());
    }

    @Override
    public void handle(GaiaSlaveStatusPacket.Started packet) {
        GaiaServer.getRedisHandler().sendPacket(
                new GaiaMasterCosmeticListUpdate(GaiaMaster.getInstance().getCosmeticHandler().getCommonCosmetics()),
                packet.getSendingID()
        );
    }
    @Override
    public void handle(GaiaSlaveStatusPacket.Shutdown packet) {
        GaiaMaster.getInstance().getServerHandler().removeServer(packet.getSendingID());
    }
    @Override
    public void handle(GaiaSlaveUserJoinPacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfileOrLoad(packet.getUuid(), packet.getUsername());
        if(profile.isBanned()) {
            GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserKickPacket(packet.getUuid(), KickConstants.USER_BANNED));
            return;
        }
        profile.setCurrentSlave(packet.getSendingID());
        profile.setVersion(packet.getVersion());
        profile.setCommit(packet.getCommit());
        profile.setServer(packet.getServer());


        GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserDataPacket.Loaded(packet.getUuid(), packet.getUsername()), packet.getSendingID());
        Server server = GaiaMaster.getInstance().getServerHandler().getServer(packet.getSendingID());
        server.getProfiles().add(packet.getUuid());

    }
    @Override
    public void handle(GaiaSlaveUserLeavePacket packet) {
        Server server = GaiaMaster.getInstance().getServerHandler().getServer(packet.getSendingID());
        server.getProfiles().remove(packet.getUuid());

    }
    @Override
    public void handle(GaiaSlaveRequestUserDataPacket packet) {
        if(packet.getType() == GaiaSlaveRequestUserDataPacket.DataType.LOAD) {
            GaiaMaster.getInstance().getProfileHandler().getProfileOrLoad(packet.getUuid(), packet.getUsername());
            GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserDataPacket.Loaded(packet.getUuid(), packet.getUsername()), packet.getSendingID());
            return;
        }
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            GaiaServer.getLogger().error("PacketHandler", packet.getSendingID() + " Requested Profile with UUID \"" +
                    packet.getUuid().toString() + "\" but it wasn't loaded");
            return;
        }
        GaiaMasterPacket sendingPacket = null;
        switch (packet.getType()) {
            case DATA:
                sendingPacket = new GaiaMasterUserDataPacket.Data(
                        profile.getUniqueId(), profile.getRank(),
                        profile.getVersion(), profile.getServer(),
                        profile.getCommit()
                );
                break;
            case COSMETICS:
                sendingPacket = new GaiaMasterUserDataPacket.Cosmetics(profile.getUniqueId(), profile.getCommonCosmetics());
                break;
            case FRIENDS:
                sendingPacket = new GaiaMasterUserDataPacket.Friends(
                        profile.getUniqueId(), profile.getCommonFriends(),
                        profile.getCommonFriendRequests(), profile.isAcceptingRequests(),
                        profile.getStatus()
                );
                break;
        }
        if(sendingPacket == null) {
            return;
        }
        GaiaServer.getRedisHandler().sendPacket(sendingPacket, packet.getSendingID());
    }

    @Override
    public void handle(GaiaSlaveUserCrashReportPacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            return;
        }
        profile.getCrashReports().add(new CrashReport(
                packet.getId(),
                packet.getVersion(),
                packet.getOs(),
                packet.getMemory(),
                packet.getStackTrace()
        ));
        GaiaServer.getLogger().debug("PacketHandler", "Added a Crash Report to " + profile.getUsername() + "'s profile", Logger.DebugType.SUCCESS);
    }

    @Override
    public void handle(GaiaSlaveUserRequestStateUpdatePacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            return;
        }
        profile.setAcceptingRequests(packet.isFriendRequests());
    }
}
