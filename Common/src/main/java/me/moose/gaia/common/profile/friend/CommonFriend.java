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
@AllArgsConstructor @NoArgsConstructor @Getter
public class CommonFriend {
    private UUID uuid;
    private String name;
    private String status;
    private String server;
    private boolean online;
    private long offlineSince;
    private FriendStatus onlineStatus;
}
