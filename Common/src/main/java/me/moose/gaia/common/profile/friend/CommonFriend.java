package me.moose.gaia.common.profile.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.profile.friend.status.FriendStatus;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor @Getter
public class CommonFriend {
    private final UUID uuid;
    private String name;
    private String status;
    private String server;
    private boolean online;
    private long offlineSince;
    private FriendStatus onlineStatus;

    public void update(CommonFriend friend) {
        name = friend.getName();
        status = friend.getStatus();
        server = friend.getServer();
        offlineSince = friend.getOfflineSince();
        onlineStatus = friend.getOnlineStatus();
    }
}
