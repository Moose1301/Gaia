package me.moose.gaia.slave.packet;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.GaiaMasterStatusPacket;
import me.moose.gaia.common.packet.packets.master.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveServerStartPacket;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.slave.GaiaSlave;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class GaiaSlavePacketHandler implements IGaiaSlavePacketHandler {


    @Override
    public void handle(GaiaMasterSlaveServerInfoPacket packet) {
        GaiaServer.getRedisHandler().setServerId(packet.getServerId());
        GaiaServer.getLogger().debug("Packet Handler", "Set Server ID to: " + packet.getServerId(), Logger.DebugType.SUCCESS);
    }


    @Override
    public void handle(GaiaMasterStatusPacket.Startup packet) {
        GaiaServer.getLogger().debug("Packet Handler", "Master Server Startup");
        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveServerStartPacket(
                GaiaSlave.getInstance().getGaiaConfig().getId(),
                GaiaSlave.getInstance().getGaiaConfig().getRegion())
        );
        //TODO ENABLE JOINS
    }

    @Override
    public void handle(GaiaMasterStatusPacket.Shutdown packet) {
        GaiaServer.getLogger().debug("Packet Handler", "Master Server Shutdown");
        //TODO KICK ALL PLAYERS AND DISABLE JOINS
    }

    @Override
    public void handle(GaiaMasterUserDataPacket.Loaded packet) {

    }

    @Override
    public void handle(GaiaMasterUserDataPacket.Friends packet) {

    }

    @Override
    public void handle(GaiaMasterUserDataPacket.Cosmetics packet) {

    }

    @Override
    public void handle(GaiaMasterUserDataPacket.Data packet) {

    }

}
