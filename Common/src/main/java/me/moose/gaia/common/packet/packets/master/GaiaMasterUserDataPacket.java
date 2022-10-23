package me.moose.gaia.common.packet.packets.master;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.profile.friend.CommonFriend;
import me.moose.gaia.common.profile.friend.FriendRequest;
import me.moose.gaia.common.profile.friend.status.FriendStatus;

import java.util.List;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter @NoArgsConstructor
public abstract class GaiaMasterUserDataPacket extends GaiaMasterPacket {
    private UUID uuid;
    public GaiaMasterUserDataPacket(UUID uuid) {
        this.uuid = uuid;
    }
    public void read(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
    }
    public void write(JsonObject object) {
        object.addProperty("uuid", uuid.toString());
    }
    public static class Loaded extends GaiaMasterUserDataPacket {
        public Loaded(UUID uuid) {
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
    public static class Data extends GaiaMasterUserDataPacket {
        public Data(UUID uuid) {
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
    @AllArgsConstructor @NoArgsConstructor @Getter
    public static class Friends extends GaiaMasterUserDataPacket {
        public Friends(UUID uuid) {
            super(uuid);
        }
        private List<CommonFriend> friends;
        private List<FriendRequest> requests;
        private boolean requestsEnabled;
        @Override
        public void read(JsonObject object) {
            super.read(object);
            requestsEnabled = object.get("requestsEnabled").getAsBoolean();
            JsonArray friends = object.getAsJsonArray("friends");
            for (JsonElement element : friends) {
                JsonObject friendObject = element.getAsJsonObject();
                UUID uuid = UUID.fromString(friendObject.get("uuid").getAsString());
                String name = friendObject.get("name").getAsString();
                String status = friendObject.get("status").getAsString();
                String server = friendObject.get("server").getAsString();
                boolean online = friendObject.get("online").getAsBoolean();
                long offlineSince = friendObject.get("offlineSince").getAsLong();
                FriendStatus onlineStatus = FriendStatus.valueOf(friendObject.get("onlineStatus").getAsString());
                this.friends.add(new CommonFriend(uuid, name, status, server, online, offlineSince, onlineStatus));
            }
            JsonArray requests = object.getAsJsonArray("requests");
            for (JsonElement element : requests) {
                JsonObject requestObject = element.getAsJsonObject();
                UUID uuid = UUID.fromString(requestObject.get("uuid").getAsString());
                String name = requestObject.get("name").getAsString();
                boolean outgoing = requestObject.get("outgoing").getAsBoolean();
                this.requests.add(new FriendRequest(uuid, name, outgoing));
            }
        }

        @Override
        public void write(JsonObject object) {
            super.write(object);
            object.addProperty("requestsEnabled", requestsEnabled);
            JsonArray friends = new JsonArray();
            for (CommonFriend friend : this.friends) {
                JsonObject friendObject = new JsonObject();
                friendObject.addProperty("uuid", friend.getUuid().toString());
                friendObject.addProperty("name", friend.getName());
                friendObject.addProperty("status", friend.getStatus());
                friendObject.addProperty("server", friend.getServer());
                friendObject.addProperty("online", friend.isOnline());
                friendObject.addProperty("offlineSince", friend.getOfflineSince());
                friendObject.addProperty("onlineStatus", friend.getOnlineStatus().name());
                friends.add(friendObject);
            }
            object.add("friends", friends);
            JsonArray requests = new JsonArray();
            for (FriendRequest request : this.requests) {
                JsonObject requestObject = new JsonObject();
                requestObject.addProperty("uuid", request.getFriendId().toString());
                requestObject.addProperty("name", request.getUsername());
                requestObject.addProperty("outgoing", request.isOutgoing());
                requests.add(requestObject);
            }
            object.add("requests", requests);
        }

        @Override
        public void handle(IGaiaSlavePacketHandler handler) {
            handler.handle(this);
        }
    }
    public static class Cosmetics extends GaiaMasterUserDataPacket {
        public Cosmetics(UUID uuid) {
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
