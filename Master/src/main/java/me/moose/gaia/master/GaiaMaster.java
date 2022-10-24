package me.moose.gaia.master;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.IGaiaServer;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterSlaveRestartPacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterStatusPacket;
import me.moose.gaia.common.redis.RedisHandler;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.master.api.APIServer;
import me.moose.gaia.master.command.CommandHandler;
import me.moose.gaia.master.cosmetic.CosmeticHandler;
import me.moose.gaia.master.packet.GaiaMasterPacketHandler;
import me.moose.gaia.master.profile.ProfileHandler;
import me.moose.gaia.master.server.ServerHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class GaiaMaster implements IGaiaServer {
    public static final UpdateOptions UPSERT_OPTIONS = new UpdateOptions().upsert(true);
    @Getter private static GaiaMaster instance;
    private Logger logger;
    private GaiaMasterPacketHandler packetHandler;
    private RedisHandler redisHandler;
    private ServerHandler serverHandler;
    private ProfileHandler profileHandler;
    private CosmeticHandler cosmeticHandler;
    private CommandHandler commandHandler;
    private GaiaConfig config;
    private APIServer apiServer;
    private MongoClient client;

    @Getter private MongoDatabase database;
    @Getter private ScheduledExecutorService executor;


    public GaiaMaster() {
        logger = new Logger("Gaia", true);
        GaiaServer.setInstance(this);
        config = new GaiaConfig();
        try {
            config.load();
        } catch (IOException e) {
            logger.error("Config", "Failed to load Gaia Config");
            return;
        }
        instance = this;
        executor = Executors.newScheduledThreadPool(1);


        connectToMongo();
        serverHandler = new ServerHandler();
        cosmeticHandler = new CosmeticHandler();
        commandHandler = new CommandHandler();
        packetHandler = new GaiaMasterPacketHandler();
        if(config.getApiPort() != -1) {
            apiServer = new APIServer();
        }

        profileHandler = new ProfileHandler();
        redisHandler = new RedisHandler(
                config.getRedisHost(),
                config.getRedisPort(),
                "master",
                packetHandler
        );
        redisHandler.sendPacket(new GaiaMasterStatusPacket.Startup(), "slaves");


        //Stay Open
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                for(;;) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).run();

    }
    public void shutdown() {
        redisHandler.sendPacket(new GaiaMasterStatusPacket.Shutdown(), "slaves");
    }
    public void connectToMongo() {

        client = new MongoClient(new ServerAddress(config.getMongoHost(), config.getMongoPort()));
        database = client.getDatabase(config.getMongoDatabase());
    }
}
