package me.moose.gaia.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.moose.gaia.common.cosmetic.ICosmeticHandler;
import me.moose.gaia.common.redis.RedisHandler;
import me.moose.gaia.common.utils.Logger;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class GaiaServer {
    public static Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    private static IGaiaServer gaiaServer;


    public static void setInstance(IGaiaServer server) {
        if(gaiaServer != null) {
            throw new RuntimeException("Cannot Change Instance of GaiaServer");
        }
        gaiaServer = server;

        //Disable PacketRegistry Debug
        getLogger().disableDebugBrand("PacketRegistry");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> GaiaServer.getInstance().shutdown()));
    }
    public static IGaiaServer getInstance() {
        return gaiaServer;
    }
    public static Logger getLogger() {
        return gaiaServer.getLogger();
    }
    public static RedisHandler getRedisHandler() {
        return gaiaServer.getRedisHandler();
    }
    public static ICosmeticHandler getCosmeticHandler() {
        return gaiaServer.getCosmeticHandler();
    }
}
