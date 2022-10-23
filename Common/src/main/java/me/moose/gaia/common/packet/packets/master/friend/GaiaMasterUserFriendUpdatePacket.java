package me.moose.gaia.common.packet.packets.master.friend;

import com.google.gson.JsonObject;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterPacket;
import me.moose.gaia.common.profile.friend.CommonFriend;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class GaiaMasterUserFriendUpdatePacket extends GaiaMasterPacket {
    private UUID uuid;

    private CommonFriend friend;
    @Override
    public void read(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
    }

    @Override
    public void handle(IGaiaSlavePacketHandler handler) {
        handler.handle(this);
    }
}
