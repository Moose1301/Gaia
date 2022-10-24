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
    private double memoryUsage;
    private double memoryMax;
    private double memoryFree;

    @Override
    public void read(JsonObject object) {
        unauthorizedUsers = object.get("unauthorizedUsers").getAsInt();
        memoryUsage = object.get("memoryUsage").getAsDouble();
        memoryMax = object.get("memoryMax").getAsDouble();
        memoryFree = object.get("memoryFree").getAsDouble();
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("unauthorizedUsers", unauthorizedUsers);
        object.addProperty("memoryUsage", memoryUsage);
        object.addProperty("memoryMax", memoryMax);
        object.addProperty("memoryFree", memoryFree);
    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }
}
