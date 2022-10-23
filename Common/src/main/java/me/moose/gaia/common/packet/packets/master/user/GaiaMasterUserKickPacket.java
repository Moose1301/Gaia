package me.moose.gaia.common.packet.packets.master.user;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterPacket;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaMasterUserKickPacket extends GaiaMasterPacket {
    private UUID uuid;
    private String reason;
    private int code;
    public GaiaMasterUserKickPacket(UUID uuid, String reason) {
        this.uuid = uuid;
        this.code = 1000;
        this.reason = reason;
    }
    public GaiaMasterUserKickPacket(UUID uuid) {
        this.uuid = uuid;
    }
    @Override
    public void read(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
        if(object.has("reason")) {
            reason = object.get("reason").getAsString();
        }
        code = object.get("code").getAsInt();
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        object.addProperty("code", code);
        if(reason != null) {
            object.addProperty("reason", reason);
        }
    }

    @Override
    public void handle(IGaiaSlavePacketHandler handler) {
        handler.handle(this);
    }
}
