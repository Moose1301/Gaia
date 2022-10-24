package me.moose.gaia.common.packet.packets.slave.server;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;
import me.moose.gaia.common.packet.packets.slave.GaiaSlavePacket;

/**
 * @author Moose1301
 * @date 10/24/2022
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaSlaveHeartbeatPacket extends GaiaSlavePacket {
    private int unauthorizedUsers;

    @Override
    public void read(JsonObject object) {
        unauthorizedUsers = object.get("unauthorizedUsers").getAsInt();
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("unauthorizedUsers", unauthorizedUsers);
    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }
}
