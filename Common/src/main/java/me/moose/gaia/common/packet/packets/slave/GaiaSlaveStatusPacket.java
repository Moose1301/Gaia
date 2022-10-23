package me.moose.gaia.common.packet.packets.slave;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterPacket;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public abstract class GaiaSlaveStatusPacket extends GaiaSlavePacket {

    /**
     * Sent by the Slave Server when it startup to get the correct ID from the Master Server.
     * Server should send a response of {@link me.moose.gaia.common.packet.packets.master.GaiaMasterSlaveServerInfoPacket}
     */
    @AllArgsConstructor @NoArgsConstructor @Getter
    public static class Startup extends GaiaSlaveStatusPacket {
        private String name;
        private String region;

        @Override
        public void read(JsonObject object) {
            this.name = object.get("name").getAsString();
            this.region = object.get("region").getAsString();
        }

        @Override
        public void write(JsonObject object) {
            object.addProperty("name", name);
            object.addProperty("region", region);
        }

        @Override
        public void handle(IGaiaMasterPacketHandler handler) {
            handler.handle(this);
        }
    }
    public static class Started extends GaiaSlaveStatusPacket {

        @Override
        public void read(JsonObject object) {

        }

        @Override
        public void write(JsonObject object) {

        }

        @Override
        public void handle(IGaiaMasterPacketHandler handler) {
            handler.handle(this);
        }
    }
    public static class Shutdown extends GaiaSlaveStatusPacket {

        @Override
        public void read(JsonObject object) {

        }

        @Override
        public void write(JsonObject object) {

        }

        @Override
        public void handle(IGaiaMasterPacketHandler handler) {
            handler.handle(this);
        }
    }
}
