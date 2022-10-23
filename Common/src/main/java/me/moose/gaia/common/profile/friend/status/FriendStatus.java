package me.moose.gaia.common.profile.friend.status;

import java.util.stream.Stream;

public enum FriendStatus {
    ONLINE,
    AWAY,
    BUSY,
    HIDDEN;

    public static FriendStatus getByOrdinal(int ordinal) {
        return Stream.of(values()).filter(status -> status.ordinal() == ordinal).findFirst().orElse(FriendStatus.HIDDEN);
    }
}
