package me.moose.gaia.master.profile;

import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.profile.cosmetic.CommonProfileCosmetic;
import me.moose.gaia.common.profile.friend.CommonFriend;
import me.moose.gaia.common.profile.friend.CommonFriendRequest;
import me.moose.gaia.master.profile.cosmetic.ProfileCosmetic;
import me.moose.gaia.master.profile.crash.CrashReport;
import me.moose.gaia.master.profile.friend.Friend;
import me.moose.gaia.master.profile.friend.FriendRequest;
import me.moose.gaia.common.profile.friend.status.FriendStatus;
import me.moose.gaia.common.profile.rank.Rank;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter @Setter
public class Profile {
    private final UUID uniqueId;
    private final String username;
    private final HashSet<Friend> friends;
    private final List<FriendRequest> friendRequests;
    private final List<ProfileCosmetic> cosmetics;
    private final List<CrashReport> crashReports;

    private boolean acceptingRequests = true;

    private String commit;
    private String server;

    private boolean banned = false;

    private String version;
    private Rank rank = Rank.DEFAULT;
    private FriendStatus status = FriendStatus.HIDDEN;

    private long offlineSince;

    /**
     * The Current Slave Instance the user is on. Null if Offline
     */
    private transient String currentSlave;

    @ConstructorProperties({ "uniqueId", "username" })
    public Profile(UUID uniqueId, String username) {
        this.uniqueId = uniqueId;
        this.username = username;

        this.server = "";

        this.cosmetics = new ArrayList<>();

        this.friends = new HashSet<>();
        this.friendRequests = new ArrayList<>();

        this.crashReports = new ArrayList<>();
    }


    public List<CommonFriend> getCommonFriends() {
        return friends.stream().map(Friend::toCommon).collect(Collectors.toList());
    }
    public List<CommonFriendRequest> getCommonFriendRequests() {
        return friendRequests.stream().map(FriendRequest::toCommon).collect(Collectors.toList());
    }
    public List<CommonProfileCosmetic> getCommonCosmetics() {
        return cosmetics.stream().map(ProfileCosmetic::toCommon).collect(Collectors.toList());
    }
}
