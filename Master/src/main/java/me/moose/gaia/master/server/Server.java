package me.moose.gaia.master.server;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class Server {
    /**
     * The ID Sent By the Server
     * Do not use when sending Packets
     */
    private String realId;
    /**
     * Generated ID by the Master Server
     * Use when sending packets
     */
    private String id;
    private String region;
    private Set<UUID> profiles = new HashSet<>();

    @Setter private int unauthorizedUsers;
    @Setter private double memoryUsage;
    @Setter private double memoryMax;
    @Setter private double memoryFree;

    public Server(String realId, String id, String region) {
        this.realId = realId;
        this.region = region;
        this.id = id;

    }
}
