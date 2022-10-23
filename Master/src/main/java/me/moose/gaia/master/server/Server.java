package me.moose.gaia.master.server;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class Server {
    private String realId;
    private String id;
    private String region;
    private Set<UUID> profiles = new HashSet<>();


    public Server(String realId, String id, String region) {
        this.realId = realId;
        this.region = region;
        this.id = id;

    }
}
