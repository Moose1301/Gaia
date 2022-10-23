package me.moose.gaia.slave;

import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.IGaiaServer;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveStatusPacket;
import me.moose.gaia.common.redis.RedisHandler;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.slave.cosmetic.CosmeticHandler;
import me.moose.gaia.slave.packet.GaiaSlavePacketHandler;
import me.moose.gaia.slave.profile.ProfileHandler;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class GaiaSlave implements IGaiaServer {
    @Getter
    private static GaiaSlave instance;
    @Setter
    private boolean connected;
    private Logger logger;
    private GaiaSlavePacketHandler packetHandler;
    private RedisHandler redisHandler;
    private CosmeticHandler cosmeticHandler;
    private ProfileHandler profileHandler;
    private GaiaConfig gaiaConfig;
    public GaiaSlave() {
        logger = new Logger("Gaia", true);
        GaiaServer.setInstance(this);
        instance = this;
        connected = false;
        gaiaConfig = new GaiaConfig();

        cosmeticHandler = new CosmeticHandler();
        profileHandler = new ProfileHandler();
        packetHandler = new GaiaSlavePacketHandler();
        redisHandler = new RedisHandler(
                "127.0.0.1",
                6379,
                gaiaConfig.getId(),
                packetHandler
        );
        redisHandler.sendPacket(new GaiaSlaveStatusPacket.Startup(gaiaConfig.getId(), gaiaConfig.getRegion()));

        //Stay Open
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                for(;;)
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
            }
        }).run();
    }


    @Override
    public void shutdown() {
        redisHandler.sendPacket(new GaiaSlaveStatusPacket.Shutdown());
    }
}
