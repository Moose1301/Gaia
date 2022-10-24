package me.moose.gaia.slave.profile.data;

import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.profile.cosmetic.CommonProfileCosmetic;
import me.moose.gaia.common.profile.friend.CommonFriend;
import me.moose.gaia.common.profile.friend.CommonFriendRequest;
import me.moose.gaia.common.profile.friend.status.FriendStatus;
import me.moose.gaia.common.profile.rank.Rank;
import me.moose.gaia.slave.GaiaSlave;
import me.moose.gaia.slave.socket.SlaveSocket;
import me.moose.gaia.slave.socket.nethandler.WSPacket;
import org.java_websocket.WebSocket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter @Setter
public class Profile {

    private long startLoaded;
    private boolean completelyLoaded;
    private final UUID uuid;
    private final String username;

    private Rank rank;
    private String version;
    private String server;
    private String commit;

    private List<CommonFriend> friends;
    private List<CommonFriendRequest> friendRequests;
    private boolean requestsEnabled;
    private FriendStatus status;

    private List<CommonProfileCosmetic> cosmetics;

    private WebSocket conn;


    public Profile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.startLoaded = System.currentTimeMillis();
        this.completelyLoaded = false;
    }

    public Optional<CommonFriend> getFriend(UUID uuid) {
        return friends.stream().filter(commonFriend -> commonFriend.getUuid().equals(uuid)).findFirst();
    }
    public boolean isOnline() {
        return conn != null && conn.isOpen();
    }
    public void sendPacket(WSPacket packet) {
        if(isOnline()) {
            SlaveSocket.getInstance().getHandler().sendPacket(conn, packet);
        }
    }
}
