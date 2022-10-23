package me.moose.gaia.master.profile.friend;

import lombok.Getter;
import me.moose.gaia.common.profile.friend.CommonFriendRequest;
import org.bson.Document;

import java.util.UUID;

@Getter
public class FriendRequest {
    private final UUID friendId;
    private final String username;

    private final boolean outgoing;

    public FriendRequest(UUID friendId, String username, boolean outgoing) {
        this.friendId = friendId;
        this.username = username;
        this.outgoing = outgoing;
    }

    public FriendRequest(Document document) {
        this.friendId = UUID.fromString(document.getString("friendId"));
        this.username = document.getString("username");
        this.outgoing = document.getBoolean("outgoing");
    }
    public CommonFriendRequest toCommon() {
        return new CommonFriendRequest(friendId, username, outgoing);
    }
    /**
     * Clones the friend request to a document
     *
     * @return the friend request in document form
     */
    public Document toDocument() {
        final Document document = new Document();

        document.append("friendId", friendId.toString());
        document.append("username", username);
        document.append("outgoing", outgoing);

        return document;
    }
}
