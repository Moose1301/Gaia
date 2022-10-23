package me.moose.gaia.common.packet.packets.master.friend;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterPacket;
import me.moose.gaia.common.profile.friend.CommonFriend;
import me.moose.gaia.common.profile.friend.status.FriendStatus;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaMasterUserFriendUpdatePacket extends GaiaMasterPacket {
    private UUID uuid;

    private CommonFriend friend;
    @Override
    public void read(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());

        JsonObject friendObject = object.getAsJsonObject("friend");
        UUID uuid = UUID.fromString(friendObject.get("uuid").getAsString());
        String name = friendObject.get("name").getAsString();
        String status = friendObject.get("status").getAsString();
        String server = friendObject.get("server").getAsString();
        boolean online = friendObject.get("online").getAsBoolean();
        long offlineSince = friendObject.get("offlineSince").getAsLong();
        FriendStatus onlineStatus = FriendStatus.valueOf(friendObject.get("onlineStatus").getAsString());
        friend = new CommonFriend(uuid, name, status, server, online, offlineSince, onlineStatus);

    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
        JsonObject friendObject = new JsonObject();
        friendObject.addProperty("uuid", friend.getUuid().toString());
        friendObject.addProperty("name", friend.getName());
        friendObject.addProperty("status", friend.getStatus());
        friendObject.addProperty("server", friend.getServer());
        friendObject.addProperty("online", friend.isOnline());
        friendObject.addProperty("offlineSince", friend.getOfflineSince());
        friendObject.addProperty("onlineStatus", friend.getOnlineStatus().name());
        object.add("friend", friendObject);
    }

    @Override
    public void handle(IGaiaSlavePacketHandler handler) {
        handler.handle(this);
    }
}
