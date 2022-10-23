package me.moose.gaia.master.packet;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterCosmeticListUpdate;
import me.moose.gaia.common.packet.packets.master.GaiaMasterPacket;
import me.moose.gaia.common.packet.packets.master.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveRequestUserDataPacket;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveStatusPacket;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveUserJoinPacket;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.profile.Profile;
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
        GaiaMaster.getInstance().getProfileHandler().getProfileOrLoad(packet.getUuid(), packet.getUsername());
        GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserDataPacket.Loaded(packet.getUuid(), packet.getUsername()), packet.getSendingID());

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
                sendingPacket = new GaiaMasterUserDataPacket.Data(profile.getUniqueId(), profile.getRank());
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
        GaiaServer.getRedisHandler().sendPacket(sendingPacket, packet.getSendingID());
    }
}
