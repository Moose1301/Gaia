package me.moose.gaia.master;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.IGaiaServer;
import me.moose.gaia.common.packet.packets.master.GaiaMasterStatusPacket;
import me.moose.gaia.common.redis.RedisHandler;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.master.cosmetic.CosmeticHandler;
import me.moose.gaia.master.packet.GaiaMasterPacketHandler;
import me.moose.gaia.master.profile.ProfileHandler;
import me.moose.gaia.master.server.ServerHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
    private GaiaConfig config;

    private MongoClient client;

    @Getter private MongoDatabase database;
    @Getter private ScheduledExecutorService executor;


    public GaiaMaster() {
        logger = new Logger("Gaia", true);
        GaiaServer.setInstance(this);
        config = new GaiaConfig();
        instance = this;
        executor = Executors.newScheduledThreadPool(1);


        connectToMongo();
        serverHandler = new ServerHandler();
        cosmeticHandler = new CosmeticHandler();
        packetHandler = new GaiaMasterPacketHandler();

        profileHandler = new ProfileHandler();
        redisHandler = new RedisHandler(
                "127.0.0.1",
                6379,
                "master",
                packetHandler
        );
        redisHandler.sendPacket(new GaiaMasterStatusPacket.Startup(), "slaves");

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

    public void connectToMongo() {

        client = new MongoClient(new ServerAddress(config.getMongoHost(), config.getMongoPort()));
        database = client.getDatabase(config.getMongoDatabase());
    }
}
