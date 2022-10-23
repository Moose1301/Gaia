package me.moose.gaia.common.redis;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.GaiaPacket;
import me.moose.gaia.common.packet.handler.IGaiaPacketHandler;
import me.moose.gaia.common.utils.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.ForkJoinPool;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
public class RedisHandler {
    private static final String CHANNEL = "Gaia";

    private JedisPool pool;
    private GaiaPubSub pubSub;
    @Getter private IGaiaPacketHandler packetHandler;
    @Setter @Getter private String serverId;

    public RedisHandler(String host, int port, String serverId, IGaiaPacketHandler packetHandler) {
        pool = new JedisPool(
                new JedisPoolConfig(),
                host,
                port,
                20000,
                null,
                0
        );
        this.packetHandler = packetHandler;
        this.serverId = serverId;
        pubSub = new GaiaPubSub(this, CHANNEL);
        ForkJoinPool.commonPool().execute(() -> {

            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(this.pubSub, CHANNEL);
            }

        });
        while (!pubSub.isSubscribed()) {
            //Make sure its subbed before continuing
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {

            }
        }
    }
    /**
     * Sends a packet to master
     *
     * @param packet The Packet to send

     */
    public void sendPacket(GaiaPacket<?> packet) {
        sendPacket(packet, null);
    }


    /**
     *
     * @param packet The Packet to send
     * @param target The target of the packet, Null when the target is the master server
     */
    public void sendPacket(GaiaPacket<?> packet, String target) {
        JsonObject packetJson = new JsonObject();
        packetJson.addProperty("id", packet.getRegistry().getPacketId((Class<? extends GaiaPacket<?>>) packet.getClass()));
        packetJson.addProperty("type", packet.getRegistry().name());
        if(target != null) {
            packetJson.addProperty("target", target);
        } else {
            packetJson.addProperty("target", "master");
        }
        packetJson.addProperty("sender", serverId);
        JsonObject data = new JsonObject();
        packet.write(data);
        packetJson.add("data", data);
        try (Jedis jedis = this.pool.getResource()) {
            GaiaServer.getLogger().debug("RedisHandler", "Publishing: " + packetJson.toString() + " to channel: " + CHANNEL);
            jedis.publish(CHANNEL, packetJson.toString());
        }

    }
    public void sendData(String data) {
        try (Jedis jedis = this.pool.getResource()) {
            jedis.publish(CHANNEL, data);
        }
    }
}
