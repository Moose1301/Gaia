package me.moose.gaia.common;

import me.moose.gaia.common.cosmetic.ICosmeticHandler;
import me.moose.gaia.common.redis.RedisHandler;
import me.moose.gaia.common.utils.Logger;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public interface IGaiaServer {

    Logger getLogger();
    RedisHandler getRedisHandler();
    ICosmeticHandler getCosmeticHandler();
    void shutdown();
}
