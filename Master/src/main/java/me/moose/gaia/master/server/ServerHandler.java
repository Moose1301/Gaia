package me.moose.gaia.master.server;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.profile.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class ServerHandler {
    private Map<String, Server> servers = new HashMap<>();

    public Server getServer(String id) {
        return servers.get(id);
    }

    public Server addServer(String id, String region) {
        String correctId = region + "-" + id + "-" + UUID.randomUUID().toString().substring(0, 8);
        Server server = new Server(id, correctId, region);
        servers.put(correctId, server);
        GaiaServer.getInstance().getLogger().debug("ServerHandler", "Added Slave Server with Generated ID: " + correctId +
                " (ID: " + id + ", Region: " + region + ")", Logger.DebugType.SUCCESS);
        return server;
    }
    public void removeServer(String id) {
        Server server = servers.remove(id);
        if(server != null) {
            GaiaServer.getInstance().getLogger().debug("ServerHandler", "Removed Slave Server with ID: " + id +
                    " (Real ID: " + server.getRealId() + ", Region: " + server.getRegion() + ")", Logger.DebugType.SUCCESS);
            for (UUID uuid : server.getProfiles()) {
                Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(uuid);
                profile.setCurrentSlave(null);
            }
        }
    }
}
