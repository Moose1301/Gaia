package me.moose.gaia.master.server;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.utils.Logger;

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
}
