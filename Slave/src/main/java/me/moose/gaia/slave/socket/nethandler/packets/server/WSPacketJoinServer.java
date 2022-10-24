package me.moose.gaia.slave.socket.nethandler.packets.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.slave.socket.nethandler.ByteBufWrapper;
import me.moose.gaia.slave.socket.nethandler.NetHandler;
import me.moose.gaia.slave.socket.nethandler.WSPacket;
import org.java_websocket.WebSocket;

import java.security.PublicKey;

@Getter
@NoArgsConstructor @AllArgsConstructor
public class WSPacketJoinServer extends WSPacket {
    private PublicKey publicKey;
    private byte[] bytes;

    @Override
    public void write(ByteBufWrapper buf) {
        writeBlob(buf, publicKey.getEncoded());
        writeBlob(buf, bytes);
    }

    @Override
    public void read(ByteBufWrapper buf) {

    }

    @Override
    public void process(WebSocket conn, NetHandler netHandler) {

    }

}
