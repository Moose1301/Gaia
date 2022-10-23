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
public class GaiaSlaveUserCrashReportPacket extends GaiaSlavePacket {
    private UUID uuid;

    private String id;
    private String version;
    private String os;
    private String memory;
    private String stackTrace;

    @Override
    public void read(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.id = object.get("id").getAsString();
        this.version = object.get("version").getAsString();
        this.os = object.get("os").getAsString();
        this.memory = object.get("memory").getAsString();
        this.stackTrace = object.get("stackTrace").getAsString();

    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        object.addProperty("id", id);
        object.addProperty("version", version);
        object.addProperty("os", os);
        object.addProperty("memory", memory);
        object.addProperty("stackTrace", stackTrace);

    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }
}
