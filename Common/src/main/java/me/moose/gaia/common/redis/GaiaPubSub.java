package me.moose.gaia.common.redis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.GaiaPacket;
import me.moose.gaia.common.packet.PacketRegistry;
import me.moose.gaia.common.packet.handler.IGaiaPacketHandler;
import me.moose.gaia.common.utils.Logger;
import redis.clients.jedis.JedisPubSub;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
@AllArgsConstructor
public class GaiaPubSub extends JedisPubSub {
    public static Gson GSON = new Gson();
    private RedisHandler redisHandler;
    private String channel;

    @Override
    public void onMessage(String channel,String message) {

        if(!channel.equals(this.channel)) {
            return;
        }
        JsonObject object = GSON.fromJson(message, JsonObject.class);
        int id = object.get("id").getAsInt();
        PacketRegistry registry = PacketRegistry.valueOf(object.get("type").getAsString());
        String sender = object.get("sender").getAsString();
        String target = object.get("target").getAsString();
        if(sender.equals(redisHandler.getServerId())) {
            return;
        }
        if(!isTarget(target)) {
            return;
        }
        GaiaServer.getLogger().debug("RedisHandler", "Got Message: " + message + " on channel: " + channel);
        JsonObject data = object.get("data").getAsJsonObject();
        GaiaPacket<IGaiaPacketHandler> gaiaPacket = null;
        try {
            gaiaPacket = registry.createPacket(id);
            gaiaPacket.setSendingID(sender);
            gaiaPacket.read(data);
            GaiaServer.getInstance().getLogger().debug("RedisHandler", "Handling Packet: " + gaiaPacket.getClass().getSimpleName());
            gaiaPacket.handle(redisHandler.getPacketHandler());
            GaiaServer.getInstance().getLogger().debug("RedisHandler", "Handled Packet: " + gaiaPacket.getClass().getSimpleName(), Logger.DebugType.SUCCESS);
        } catch (IllegalAccessException | InstantiationException e) {
            //TODO LOGGER
            GaiaServer.getLogger().error("RedisHandler", "Error Creating Packet With ID: " + id + " (From: " + sender + ")");
            e.printStackTrace();
        }

    }
    public boolean isTarget(String target) {
        if(redisHandler.getServerId().equals("master")) {
            return target.equals("master");
        }
        return target.equals("slaves") || target.equals(redisHandler.getServerId());
    }
}
