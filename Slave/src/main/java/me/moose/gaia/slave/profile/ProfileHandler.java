package me.moose.gaia.slave.profile;

import lombok.Getter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveRequestUserDataPacket;
import me.moose.gaia.slave.profile.data.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class ProfileHandler {
    @Getter
    private Map<UUID, Profile> profiles = new HashMap<>();

    @Getter
    private Map<UUID, CompletableFuture<Profile>> loadRequests = new HashMap<>();

    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }
    public CompletableFuture<Profile> getProfileOrLoad(UUID uuid, String username) {
        if(profiles.containsKey(uuid)) {
            return CompletableFuture.completedFuture(profiles.get(uuid));
        }

        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveRequestUserDataPacket(uuid, GaiaSlaveRequestUserDataPacket.DataType.LOAD, username));
        CompletableFuture<Profile> feature = new CompletableFuture<>();
        loadRequests.put(uuid, feature);
        return feature;
    }
}
