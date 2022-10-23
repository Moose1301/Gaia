package me.moose.gaia.common.packet.packets.master;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;

/**
 * @author Moose1301
 * @date 10/21/2022
 */

/**
 * Response to the Slave Server sending {@link me.moose.gaia.common.packet.packets.slave.GaiaSlaveServerStartPacket}
 * Sends the serverId which is based off the Region and ID sent in {@link me.moose.gaia.common.packet.packets.slave.GaiaSlaveServerStartPacket}
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaMasterSlaveServerInfoPacket extends GaiaMasterPacket {
    /**
     * Unique ID Made from the server's ID and Regions sent in {@link me.moose.gaia.common.packet.packets.slave.GaiaSlaveServerStartPacket}
     */
    private String serverId;

    @Override
    public void read(JsonObject object) {
        serverId = object.get("serverId").getAsString();
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("serverId", serverId);
    }

    @Override
    public void handle(IGaiaSlavePacketHandler handler) {
        handler.handle(this);
    }
}
