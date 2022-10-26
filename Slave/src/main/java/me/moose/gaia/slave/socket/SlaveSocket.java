package me.moose.gaia.slave.socket;

import io.netty.buffer.Unpooled;
import lombok.Getter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.utils.UUIDUtils;
import me.moose.gaia.slave.GaiaSlave;
import me.moose.gaia.slave.socket.nethandler.ByteBufWrapper;
import me.moose.gaia.slave.socket.nethandler.NetHandler;
import me.moose.gaia.slave.socket.nethandler.packets.server.WSPacketJoinServer;
import me.moose.gaia.slave.tasks.UnauthorizedCleanerTask;
import me.moose.gaia.slave.utils.KickConstants;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
public class SlaveSocket extends WebSocketServer {
    @Getter
    private Map<UUID, SocketUser> users = new HashMap<>();
    @Getter
    private List<UUID> unauthorizedUsers = new ArrayList<>();

    @Getter
    private NetHandler handler;
    public SlaveSocket() {
        super(new InetSocketAddress(GaiaSlave.getInstance().getGaiaConfig().getSocketPort()));
    }
    @Override
    public void onStart() {
        GaiaSlave.getInstance().getExecutor().scheduleAtFixedRate(new UnauthorizedCleanerTask(), 500, 500, TimeUnit.MILLISECONDS);
        GaiaSlave.getInstance().getLogger().sucess("SlaveSocket", "Started on Port: " + GaiaSlave.getInstance().getGaiaConfig().getSocketPort());
        handler = new NetHandler();

    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if(handler == null) {
            conn.close(1000, KickConstants.NOT_STARTED);
            return;
        }
        String handshakeUuid = handshake.getFieldValue("playerId");
        String username = handshake.getFieldValue("username");
        String version = handshake.getFieldValue("version");
        String gitCommit = handshake.getFieldValue("gitCommit");
        if (handshakeUuid.equals("") || username.equals("") || version.equals("") || handshakeUuid.equals(username)) {
            GaiaServer.getLogger().debug("SlaveSocket", "Someone tried connecting as " + username
                    + " (UUID: " + handshakeUuid + (version.isEmpty()
                    ? ")" : ", Version: " + version + ")"));
            conn.close(1003, KickConstants.INVALID_UUID_NAME);
            return;
        }
        UUID uuid;

        try {
            uuid = UUID.fromString(handshakeUuid);
        } catch (Exception ex) {
            uuid = UUIDUtils.fromString(handshakeUuid);
        }

        String server = "";
        if(handshake.hasFieldValue("server")) {
            server = handshake.getFieldValue("server");
        }

        SocketUser user = new SocketUser(conn, uuid, username, version, server, gitCommit);
        conn.setAttachment(uuid);
        users.put(uuid, user);
        unauthorizedUsers.add(uuid);
        GaiaSlave.getInstance().getLogger().debug("SlaveSocket", "User Connected: " + username + " (UUID: " + uuid.toString() + ")");
        handler.sendPacket(conn, new WSPacketJoinServer(GaiaSlave.getInstance().getPublicKey(), UUID.randomUUID().toString().getBytes()));
        user.setSentAuthRequest(System.currentTimeMillis());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        SocketUser user =users.remove(conn.getAttachment());
        if(user != null && user.isRepliedAuthRequest()) {
            GaiaMaster.getRedisHandler().sendPacket(new GaiaSlaveUserLeavePacket(user.getUuid()));
        }
    }


    public void onMessage(WebSocket conn, ByteBuffer message) {
        handler.handlePacket(conn, new ByteBufWrapper(Unpooled.wrappedBuffer(message.array())));
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        GaiaSlave.getInstance().getLogger().error("SlaveSocket", "Error from " + conn.getRemoteSocketAddress().toString());
        ex.printStackTrace();
    }
    @Override
    public void onMessage(WebSocket conn, String message) {

    }
    public static SlaveSocket getInstance() {
        return GaiaSlave.getInstance().getSlaveSocket();
    }
}
