package me.moose.gaia.master.profile;

import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserMessagePacket;
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
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
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
    public boolean isOnline() {
        return currentSlave != null;
    }
    public Optional<Friend> getFriend(UUID uuid) {
        return friends.stream().filter(friend -> friend.getPlayerId().equals(uuid)).findFirst();
    }

    public CommonFriend toFriend() {
        return new CommonFriend(uniqueId, username, null, server, currentSlave != null, offlineSince, status);
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
    public void updateFriends() {
        for (Friend friend : friends) {
            Profile profile = friend.getProfile();
            if(profile != null) {
                friend.setName(profile.getUsername());
                friend.setServer(profile.getServer());
                friend.setOnline(profile.isOnline());
                friend.setOfflineSince(profile.getOfflineSince());
                friend.setOnlineStatus(profile.getStatus());
                friend.setClientVersion(profile.getVersion());

                profile.getFriend(friend.getPlayerId()).ifPresent(friend1 -> {
                    friend1.setName(getUsername());
                    friend1.setServer(getServer());
                    friend1.setOnline(isOnline());
                    friend1.setOfflineSince(getOfflineSince());
                    friend1.setOnlineStatus(getStatus());
                    friend1.setClientVersion(getVersion());
                });
            }

        }
    }


    /**
     * Send an array of console outputs to the player
     *
     * @param strings the strings to output to the players console
     */
    public void sendMessage(String... strings) {
        for (String string : strings) {
            sendMessage(string);
        }
    }

    /**
     * Send a message to the player in their console
     *
     * @param string the string to output to the players console
     */
    public void sendMessage(String string) {
        if(currentSlave == null) {
            return;
        }
        String[] split = string.split("\n");

        for (String str : split) {
            GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserMessagePacket.ConsoleMessage(uniqueId, str), currentSlave);
        }
    }
}
