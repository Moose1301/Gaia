package me.moose.gaia.common.packet.packets.slave.friend;

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
public class GaiaSlaveUserRequestStateUpdatePacket extends GaiaSlavePacket {
    private UUID uuid;
    private boolean friendRequests;
    @Override
    public void read(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
        friendRequests = object.get("friendRequests").getAsBoolean();
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        object.addProperty("friendRequests", friendRequests);
    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }
}
