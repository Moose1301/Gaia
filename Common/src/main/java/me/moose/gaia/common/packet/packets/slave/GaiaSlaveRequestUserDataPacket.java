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
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaSlaveRequestUserDataPacket extends GaiaSlavePacket {
    private UUID uuid;
    private DataType type;

    @Override
    public void read(JsonObject object) {
      uuid = UUID.fromString(object.get("uuid").getAsString());
      type = DataType.valueOf(object.get("type").getAsString());
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        object.addProperty("type", type.name());
    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }


    public enum DataType {
        DATA,
        COSMETIC,
        FRIENDS
    }
}
