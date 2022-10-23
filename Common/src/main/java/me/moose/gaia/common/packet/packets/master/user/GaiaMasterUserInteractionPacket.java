package me.moose.gaia.common.packet.packets.master.user;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterPacket;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */

//Will have packets like process list and host files, etc if we ever need
@Getter
public abstract class GaiaMasterUserInteractionPacket extends GaiaMasterPacket {
    private UUID uuid;
    public GaiaMasterUserInteractionPacket(UUID uuid) {
        this.uuid = uuid;
    }
    @Override
    public void read(JsonObject object) {
        this.uuid =  UUID.fromString(object.get("uuid").getAsString());
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", this.uuid.toString());
    }
    public static class Crash extends GaiaMasterUserInteractionPacket {

        public Crash(UUID uuid) {
            super(uuid);
        }

        @Override
        public void read(JsonObject object) {
            super.read(object);
        }

        @Override
        public void write(JsonObject object) {
            super.write(object);
        }

        @Override
        public void handle(IGaiaSlavePacketHandler handler) {
            handler.handle(this);
        }
    }

}
