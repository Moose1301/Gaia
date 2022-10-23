package me.moose.gaia.common.packet.packets.slave;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */

/**
 * THIS SHOULD ONLY BE SENT WHEN THE USER IS AUTHED AS REAL
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaSlaveUserJoinPacket extends GaiaSlavePacket {
    private UUID uuid;
    private String username;
    @Override
    public void read(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.username = object.get("username").getAsString();
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        object.addProperty("username", username);
    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }
}
