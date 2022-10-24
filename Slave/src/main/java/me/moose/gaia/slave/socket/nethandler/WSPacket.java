package me.moose.gaia.slave.socket.nethandler;

import io.netty.buffer.ByteBuf;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.slave.GaiaSlave;
import me.moose.gaia.slave.socket.nethandler.packets.client.WSPacketClientJoinServerResponse;
import me.moose.gaia.slave.socket.nethandler.packets.server.WSPacketJoinServer;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
public abstract class WSPacket {
    public static Map<Class<? extends WSPacket>, Integer> PACKET_TO_ID;
    public static Map<Integer, Class<? extends WSPacket>> ID_TO_PACKET;

    /**
     * Writes an outgoing packet
     *
     * @param out the byte buffer of the packet
     * @throws IOException
     */
    public abstract void write(ByteBufWrapper out) throws IOException;

    /**
     * Reads a incoming packet
     *
     * @param in the byte buffer of the packet
     * @throws IOException
     */
    public abstract void read(ByteBufWrapper in) throws IOException;

    /**
     * Processes incoming packet
     *
     * @param conn the player connection
     * @param handler the nethandler that handles the packet
     */
    public abstract void process(WebSocket conn, NetHandler handler);

    protected void writeBlob(ByteBuf buf, byte[] bytes) {
        buf.writeShort(bytes.length);
        buf.writeBytes(bytes);
    }

    protected byte[] readBlob(ByteBuf buf) {
        short key = buf.readShort();
        if (key < 0) {
            GaiaSlave.getInstance().getLogger().error("WSPacket", "Key was smaller than nothing! Weird key!");
            return new byte[0];
        }
        byte[] data = new byte[key];
        buf.readBytes(data);
        return data;
    }

    private static void register(Class<? extends WSPacket> packet, Integer id) {
        PACKET_TO_ID.put(packet, id);
        ID_TO_PACKET.put(id, packet);
    }
    static {
        ID_TO_PACKET = new HashMap<>();
        PACKET_TO_ID = new HashMap<>();

        register(WSPacketJoinServer.class, 0);
        register(WSPacketClientJoinServerResponse.class, 1);
    }
}
