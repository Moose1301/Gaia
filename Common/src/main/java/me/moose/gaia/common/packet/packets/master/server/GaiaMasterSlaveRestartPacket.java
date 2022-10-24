package me.moose.gaia.common.packet.packets.master.server;

import com.google.gson.JsonObject;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterPacket;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
public class GaiaMasterSlaveRestartPacket extends GaiaMasterPacket {
    @Override
    public void read(JsonObject object) {

    }

    @Override
    public void write(JsonObject object) {

    }

    @Override
    public void handle(IGaiaSlavePacketHandler handler) {
        handler.handle(this);
    }
}
