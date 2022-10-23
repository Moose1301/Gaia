package me.moose.gaia.slave.profile.data;

import lombok.Getter;
import lombok.Setter;
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
@Getter @Setter
public class Profile {
    private boolean completelyLoaded;
    private final UUID uuid;
    private final String username;

    private Rank rank;

    private List<CommonFriend> friends;
    private List<CommonFriendRequest> friendRequests;
    private boolean requestsEnabled;
    private FriendStatus status;

    private List<CommonProfileCosmetic> cosmetics;


    public Profile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.completelyLoaded = false;
    }
}
