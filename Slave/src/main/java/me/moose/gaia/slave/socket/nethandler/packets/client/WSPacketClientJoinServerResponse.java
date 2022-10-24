package me.moose.gaia.slave.socket.nethandler.packets.client;

import lombok.Getter;
import me.moose.gaia.slave.GaiaSlave;
import me.moose.gaia.slave.socket.nethandler.ByteBufWrapper;
import me.moose.gaia.slave.socket.nethandler.NetHandler;
import me.moose.gaia.slave.socket.nethandler.WSPacket;
import me.moose.gaia.slave.utils.CryptUtils;
import org.java_websocket.WebSocket;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.PublicKey;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
@Getter
public class WSPacketClientJoinServerResponse extends WSPacket {
    private byte[] secretKeyEncrypted;
    private byte[] dataEncrypted;

    private SecretKey secretKey;
    private byte[] data;

    @Override
    public void write(ByteBufWrapper out) throws IOException {

    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        // Read the Encrypted Data
        secretKeyEncrypted = readBlob(in);
        dataEncrypted = readBlob(in);

        // Decrypt the Data
        secretKey = CryptUtils.decryptSharedKey(GaiaSlave.getInstance().getPrivateKey(), secretKeyEncrypted);
        data = CryptUtils.decryptData(GaiaSlave.getInstance().getPrivateKey(), dataEncrypted);

    }

    @Override
    public void process(WebSocket conn, NetHandler netHandler) {
        netHandler.handleJoinServerResponse(conn, this);
    }
}
