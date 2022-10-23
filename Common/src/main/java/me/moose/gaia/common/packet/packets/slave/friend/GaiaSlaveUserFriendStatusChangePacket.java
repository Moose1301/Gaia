package me.moose.gaia.common.packet.packets.slave.friend;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;
import me.moose.gaia.common.packet.packets.slave.GaiaSlavePacket;
import me.moose.gaia.common.profile.friend.status.FriendStatus;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaSlaveUserFriendStatusChangePacket extends GaiaSlavePacket {
    private UUID uuid;
    private FriendStatus status;

    @Override
    public void read(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
        status = FriendStatus.valueOf(object.get("status").getAsString());
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        object.addProperty("status", status.name());
    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }
}
