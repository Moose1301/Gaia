package me.moose.gaia.common.packet.packets.master;

import com.google.gson.JsonObject;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public abstract class GaiaMasterStatusPacket extends GaiaMasterPacket {


    public static class Startup extends GaiaMasterStatusPacket {

        @Override
        public void read(JsonObject object) {

        }

        @Override
        public void write(JsonObject object) {

        }

        @Override
        public void handle(IGaiaSlavePacketHandler handler) {
            handler.handle(this);
        }
    }
    public static class Shutdown extends GaiaMasterStatusPacket {

        @Override
        public void read(JsonObject object) {

        }

        @Override
        public void write(JsonObject object) {

        }

        @Override
        public void handle(IGaiaSlavePacketHandler handler) {
            handler.handle(this);
        }
    }
}
