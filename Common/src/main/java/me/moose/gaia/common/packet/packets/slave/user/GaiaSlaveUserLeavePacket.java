package me.moose.gaia.common.packet.packets.slave.user;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;
import me.moose.gaia.common.packet.packets.slave.GaiaSlavePacket;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */


@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaSlaveUserLeavePacket extends GaiaSlavePacket {
    private UUID uuid;

    @Override
    public void read(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());

    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());

    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }
}
