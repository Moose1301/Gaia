package me.moose.gaia.master.packet;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveServerStartPacket;
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
    public void handle(GaiaSlaveServerStartPacket packet) {
        Server server = GaiaMaster.getInstance().getServerHandler().addServer(packet.getName(), packet.getRegion());
        GaiaServer.getRedisHandler().sendPacket(new GaiaMasterSlaveServerInfoPacket(server.getId()), server.getRealId());
    }

    @Override
    public void handle(GaiaSlaveUserJoinPacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfileOrLoad(packet.getUuid(), packet.getUsername());
        GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserDataPacket.Friends(), packet.getSendingID());

    }
}
