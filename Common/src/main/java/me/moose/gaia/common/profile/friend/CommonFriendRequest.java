package me.moose.gaia.common.profile.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor @Getter @Setter
public class CommonFriendRequest {
    private final UUID friendId;
    private final String username;

    private final boolean outgoing;
}
