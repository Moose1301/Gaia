package me.moose.gaia.slave.socket.nethandler;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.Unpooled;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.packets.slave.user.GaiaSlaveUserJoinPacket;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.slave.GaiaSlave;
import me.moose.gaia.slave.socket.SlaveSocket;
import me.moose.gaia.slave.socket.SocketUser;
import me.moose.gaia.slave.socket.nethandler.packets.client.WSPacketClientJoinServerResponse;
import me.moose.gaia.slave.socket.nethandler.packets.shared.WSPacketConsoleMessage;
import me.moose.gaia.slave.utils.CryptUtils;
import me.moose.gaia.slave.utils.KickConstants;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Proxy;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
public class NetHandler {
    private MinecraftSessionService sessionService;
    public NetHandler() {
        sessionService = (new YggdrasilAuthenticationService(Proxy.NO_PROXY,
                UUID.randomUUID().toString())).createMinecraftSessionService();
    }
    public void handlePacket(WebSocket conn, ByteBufWrapper data) {
        SocketUser user = SlaveSocket.getInstance().getUsers().get(conn.getAttachment());
        int packetId = data.readVarInt();
        Class<? extends WSPacket> packetClass = WSPacket.ID_TO_PACKET.get(packetId);
        if(packetClass != null) {
            try {
                WSPacket packet = packetClass.newInstance();
                packet.read(data);
                packet.process(conn, this);
            } catch (Exception ex) {
                GaiaServer.getLogger().error("NetHandler", "Error while handling packet with ID \"" + packetId + "\" from " + user.getUsername());
                ex.printStackTrace();
            }
        } else {
            GaiaServer.getLogger().debug("NetHandler", "Received a packet with unknown id \""
                    + packetId + "\" from " + user.getUsername(), Logger.DebugType.ERROR);
        }
    }
    public void sendPacket(WebSocket conn, WSPacket packet) {
        if (conn == null || !conn.isOpen()) {
            return;
        }

        ByteBufWrapper wrapper = new ByteBufWrapper(Unpooled.buffer());
        wrapper.writeVarInt(WSPacket.PACKET_TO_ID.get(packet.getClass()));
        try {
            packet.write(wrapper);
            conn.send(wrapper.array());
            GaiaSlave.getInstance().getLogger().debug(
                    "NetHandler (Sending)",
                    "Sending " + conn.getRemoteSocketAddress().toString() + " " + packet.getClass().getSimpleName(),
                    Logger.DebugType.SUCCESS
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleJoinServerResponse(WebSocket conn, WSPacketClientJoinServerResponse packet) {
        SocketUser user = GaiaSlave.getInstance().getSlaveSocket().getUsers().get(conn.getAttachment());
        byte[] serverIdHash = CryptUtils.getServerIdHash("", GaiaSlave.getInstance().getPublicKey(), packet.getSecretKey());


        try {
            boolean hasJoinedServer = sessionService.hasJoinedServer(
                    new GameProfile(user.getUuid(), user.getUsername()),
                    (new BigInteger(serverIdHash).toString(16))
            ) != null;
            if(!hasJoinedServer) {
                GaiaSlave.getInstance().getLogger().error("NetHandler (Authentication)", "Cannot Auth " + user.getUsername() +
                        ". Did not Join Server?");
                conn.close(4000, KickConstants.FAILED_AUTH_DIDNT_JOIN);
                return;
            }
            user.setRepliedAuthRequest(true);
            GaiaServer.getLogger().debug("NetHandler (Authentication)", user.getUsername() + " completed authentication in " +
                    (System.currentTimeMillis() - user.getSentAuthRequest()) + "ms", Logger.DebugType.SUCCESS);
            GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveUserJoinPacket(
                    user.getUuid(), user.getUsername(),
                    user.getServer(), user.getCommit(), user.getVersion())
            );
            SlaveSocket.getInstance().getUnauthorizedUsers().remove(user.getUuid());
        } catch (AuthenticationUnavailableException e) {
            GaiaSlave.getInstance().getLogger().error("NetHandler (Authentication)", "Cannot Auth " + user.getUsername() + ". Auth Unavailable");
            conn.close(4000, KickConstants.FAILED_AUTH_UNAVAILABLE);
            e.printStackTrace();
        }
    }
    public void handleConsoleMessage(WebSocket conn, WSPacketConsoleMessage packet) {
        SocketUser user = GaiaSlave.getInstance().getSlaveSocket().getUsers().get(conn.getAttachment());
    }




}
