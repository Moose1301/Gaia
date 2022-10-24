package me.moose.gaia.slave;

import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.IGaiaServer;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveStatusPacket;
import me.moose.gaia.common.redis.RedisHandler;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.common.utils.SecurityHandler;
import me.moose.gaia.slave.cosmetic.CosmeticHandler;
import me.moose.gaia.slave.packet.GaiaSlavePacketHandler;
import me.moose.gaia.slave.profile.ProfileHandler;
import me.moose.gaia.slave.socket.SlaveSocket;
import me.moose.gaia.slave.tasks.HeartbeatTask;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private SlaveSocket slaveSocket;

    private ScheduledExecutorService executor;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public GaiaSlave() {
        logger = new Logger("Gaia", true);
        GaiaServer.setInstance(this);
        instance = this;
        executor = Executors.newScheduledThreadPool(1);

        connected = false;
        gaiaConfig = new GaiaConfig();
        try {
            gaiaConfig.load();
        } catch (Exception ex) {
            logger.error("Config", "Failed to load Gaia Config");
            return;
        }
        KeyPair pair = SecurityHandler.generateKeyPair();
        if (pair != null) {
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
        } else  {
            getLogger().error("GaiaSlave", "Could not get Key Pair Shutting Down.");
            System.exit(20);
        }
        cosmeticHandler = new CosmeticHandler();
        profileHandler = new ProfileHandler();
        packetHandler = new GaiaSlavePacketHandler();
        redisHandler = new RedisHandler(
                gaiaConfig.getRedisHost(),
                gaiaConfig.getRedisPort(),
                gaiaConfig.getId(),
                packetHandler
        );
        redisHandler.sendPacket(new GaiaSlaveStatusPacket.Startup(gaiaConfig.getId(), gaiaConfig.getRegion()));
        slaveSocket = new SlaveSocket();
        executor.scheduleAtFixedRate(new HeartbeatTask(), 2, 2, TimeUnit.SECONDS);
        slaveSocket.start();
    }


    @Override
    public void shutdown() {
        try {
            slaveSocket.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisHandler.sendPacket(new GaiaSlaveStatusPacket.Shutdown());
    }
}
