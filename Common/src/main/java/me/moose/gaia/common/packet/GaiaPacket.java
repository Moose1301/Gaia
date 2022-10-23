package me.moose.gaia.common.packet;

import com.google.gson.JsonObject;
import me.moose.gaia.common.packet.handler.IGaiaPacketHandler;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
public interface GaiaPacket<T extends IGaiaPacketHandler> {
    void read(JsonObject object);
    void write(JsonObject object);

    void handle(T handler);

    PacketRegistry getRegistry();

    void setSendingID(String id);
    String getSendingID();



}
