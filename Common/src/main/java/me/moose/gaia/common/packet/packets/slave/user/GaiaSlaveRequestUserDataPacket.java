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
public class GaiaSlaveRequestUserDataPacket extends GaiaSlavePacket {
    private UUID uuid;
    private DataType type;
    private String username;
    public GaiaSlaveRequestUserDataPacket(UUID uuid, DataType type) {
        this.uuid = uuid;
        this.type = type;
        if(type == DataType.LOAD) {
            throw new RuntimeException("Cannot Create new Instance of GaiaSlaveRequestUserDataPacket without a username while Data Type is LOAD");
        }
    }

    @Override
    public void read(JsonObject object) {
      uuid = UUID.fromString(object.get("uuid").getAsString());
      type = DataType.valueOf(object.get("type").getAsString());
      if(object.has("username")) {
          this.username = object.get("username").getAsString();
      }
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        object.addProperty("type", type.name());
        if(this.username != null) {
            object.addProperty("username", username);
        }
    }

    @Override
    public void handle(IGaiaMasterPacketHandler handler) {
        handler.handle(this);
    }


    public enum DataType {
        LOAD,
        DATA,
        COSMETICS,
        FRIENDS
    }
}
