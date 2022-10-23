package me.moose.gaia.common.packet.packets.master;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.cosmetic.data.CommonCosmetic;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.profile.cosmetic.CommonProfileCosmetic;
import me.moose.gaia.common.profile.friend.CommonFriend;
import me.moose.gaia.common.profile.friend.CommonFriendRequest;
import me.moose.gaia.common.profile.friend.status.FriendStatus;
import me.moose.gaia.common.profile.rank.Rank;

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
        private Rank rank;
        public Data(UUID uuid, Rank rank) {
            super(uuid);
            this.rank = rank;
        }
        @Override
        public void read(JsonObject object) {
            super.read(object);
            rank = Rank.valueOf(object.get("rank").getAsString());
        }

        @Override
        public void write(JsonObject object) {
            super.write(object);
            object.addProperty("rank", rank.name());
        }

        @Override
        public void handle(IGaiaSlavePacketHandler handler) {
            handler.handle(this);
        }
    }
    @NoArgsConstructor @Getter
    public static class Friends extends GaiaMasterUserDataPacket {
        private List<CommonFriend> friends;
        private List<CommonFriendRequest> requests;
        private boolean requestsEnabled;
        private FriendStatus status;
        public Friends(UUID uuid, List<CommonFriend> friends, List<CommonFriendRequest> requests, boolean requestsEnabled, FriendStatus status ) {
            super(uuid);
            this.friends = friends;
            this.requests = requests;
            this.requestsEnabled = requestsEnabled;
            this.status = status;
        }

        @Override
        public void read(JsonObject object) {
            super.read(object);
            requestsEnabled = object.get("requestsEnabled").getAsBoolean();
            status = FriendStatus.valueOf(object.get("status").getAsString());
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
                this.requests.add(new CommonFriendRequest(uuid, name, outgoing));
            }
        }

        @Override
        public void write(JsonObject object) {
            super.write(object);
            object.addProperty("requestsEnabled", requestsEnabled);
            object.addProperty("status", status.name());
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
            for (CommonFriendRequest request : this.requests) {
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
        private List<CommonProfileCosmetic> cosmetics;
        public Cosmetics(UUID uuid, List<CommonProfileCosmetic> cosmetics) {
            super(uuid);
            this.cosmetics = cosmetics;
        }
        @Override
        public void read(JsonObject object) {
            super.read(object);
            for (JsonElement element : object.getAsJsonArray("cosmetics")) {
                JsonObject cosmeticObject = element.getAsJsonObject();
                String cosmeticName = cosmeticObject.get("cosmetic").getAsString();
                CommonCosmetic cosmetic = GaiaServer.getCosmeticHandler().getCommonCosmetic(cosmeticName);
                boolean active = cosmeticObject.get("active").getAsBoolean();
                cosmetics.add(new CommonProfileCosmetic(cosmetic, active));
            }
        }

        @Override
        public void write(JsonObject object) {
            super.write(object);
            JsonArray cosmetics = new JsonArray();
            for (CommonProfileCosmetic cosmetic : this.cosmetics) {
                JsonObject cosmeticObject = new JsonObject();
                cosmeticObject.addProperty("cosmetic", cosmetic.getCosmetic().getName());
                cosmeticObject.addProperty("active", cosmetic.isActive());
                cosmetics.add(cosmeticObject);
            }
            object.add("cosmetics", cosmetics);
        }

        @Override
        public void handle(IGaiaSlavePacketHandler handler) {
            handler.handle(this);
        }
    }
}
