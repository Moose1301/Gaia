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
 * @date 10/23/2022
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaSlaveUserConsoleMessagePacket extends GaiaSlavePacket {
    private UUID uuid;
    private String message;
    @Override
    public void read(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
        message = object.get("message").getAsString();
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        object.addProperty("message", message);
    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }
}
