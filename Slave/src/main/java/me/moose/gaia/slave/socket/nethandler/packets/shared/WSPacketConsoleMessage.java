package me.moose.gaia.slave.socket.nethandler.packets.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.slave.socket.nethandler.ByteBufWrapper;
import me.moose.gaia.slave.socket.nethandler.NetHandler;
import me.moose.gaia.slave.socket.nethandler.WSPacket;
import org.java_websocket.WebSocket;

import java.io.IOException;

/**
 * @author Moose1301
 * @date 10/24/2022
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class WSPacketConsoleMessage extends WSPacket {
    private String message;
    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.writeString(this.message);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        message = in.readString(32767);
    }

    @Override
    public void process(WebSocket conn, NetHandler handler) {
        handler.handleConsoleMessage(conn, this);
    }
}
