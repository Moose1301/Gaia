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
@Getter   @NoArgsConstructor
public abstract class GaiaMasterUserMessagePacket extends GaiaMasterPacket {
    private UUID uuid;
    public GaiaMasterUserMessagePacket(UUID uuid) {
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
    @NoArgsConstructor @Getter
    public static class ConsoleMessage extends GaiaMasterUserMessagePacket {
        private String message;
        public ConsoleMessage(UUID uuid, String message) {
            super(uuid);
            this.message = message;
        }
        @Override
        public void read(JsonObject object) {
            super.read(object);
            message = object.get("message").getAsString();
        }

        @Override
        public void write(JsonObject object) {
            super.write(object);
            object.addProperty("message", message);
        }
        @Override
        public void handle(IGaiaSlavePacketHandler handler) {
            handler.handle(this);
        }
    }
    @NoArgsConstructor @Getter
    public static class Notification extends GaiaMasterUserMessagePacket {
        private String title;
        private String message;
        public Notification(UUID uuid, String title, String message) {
            super(uuid);
            this.title = title;
            this.message = message;
        }
        @Override
        public void read(JsonObject object) {
            super.read(object);
            title = object.get("title").getAsString();
            message = object.get("message").getAsString();
        }

        @Override
        public void write(JsonObject object) {
            super.write(object);
            object.addProperty("title", title);
            object.addProperty("message", message);
        }
        @Override
        public void handle(IGaiaSlavePacketHandler handler) {
            handler.handle(this);
        }
    }
}
