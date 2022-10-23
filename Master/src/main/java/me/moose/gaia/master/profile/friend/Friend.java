package me.moose.gaia.master.profile.friend;

import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.profile.friend.CommonFriend;
import me.moose.gaia.common.profile.friend.status.FriendStatus;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.profile.Profile;
import org.bson.Document;

import java.util.UUID;

@Getter @Setter
public class Friend {
    private final UUID playerId;
    private String name;
    private String status;
    private String server;
    private boolean online;
    private long offlineSince;
    private FriendStatus onlineStatus;
    private String clientVersion;



    public Friend(String playerId, String name, String status, String server, boolean online, long offlineSince, FriendStatus onlineStatus, String version) {
        this.playerId = UUID.fromString(playerId);
        this.name = name;
        this.status = status;
        this.server = server;
        this.online = online;
        this.offlineSince = offlineSince;
        this.onlineStatus = onlineStatus;
        this.clientVersion = version;
    }

    public Friend(Document document) {
        this.playerId = UUID.fromString(document.getString("playerId"));
        this.name = document.getString("name");
        this.server = "";
    }

    /**
     * Clones the friend to a document
     *
     * @return the cloned friend in document form
     */
    public Document toDocument() {
        final Document document = new Document();

        document.append("playerId", playerId.toString());
        document.append("name", name);

        return document;
    }
    public Profile getProfile() {
        return GaiaMaster.getInstance().getProfileHandler().getProfile(playerId);
    }
    public CommonFriend toCommon() {
        return new CommonFriend(playerId, name,  status, server, online, offlineSince, onlineStatus);
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Friend)) {
            return false;
        }
        return playerId.equals(((Friend)obj).getPlayerId());
    }


    @Override
    public int hashCode() {
        return playerId.hashCode();
    }

    public static class FriendBuilder {
        private String playerUUID;
        private String name;
        private String status;
        private String server;
        private boolean online;
        private int offlineSince;
        private FriendStatus friendStatus;
        private String clientVersion;

        public FriendBuilder() {}

        public FriendBuilder playerId(final String playerUUID) {
            this.playerUUID = playerUUID;
            return this;
        }

        public FriendBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public FriendBuilder status(final String status) {
            this.status = status;
            return this;
        }

        public FriendBuilder server(final String server) {
            this.server = server;
            return this;
        }

        public FriendBuilder online(final boolean online) {
            this.online = online;
            return this;
        }

        public FriendBuilder offlineSince(final int offlineSince) {
            this.offlineSince = offlineSince;
            return this;
        }

        public FriendBuilder onlineStatus(final FriendStatus friendStatus) {
            this.friendStatus = friendStatus;
            return this;
        }

        public FriendBuilder version(final String version) {
            this.clientVersion = version;
            return this;
        }

        public Friend build() {
            return new Friend(this.playerUUID, this.name, this.status, this.server, this.online, this.offlineSince, this.friendStatus, this.clientVersion);
        }
    }
}
