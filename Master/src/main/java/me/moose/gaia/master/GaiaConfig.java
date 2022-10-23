package me.moose.gaia.master;

import lombok.Getter;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class GaiaConfig {
    private String id = "master";

    private String mongoHost = "127.0.0.1";
    private int mongoPort = 27017;
    private String mongoDatabase = "gaia";
}
