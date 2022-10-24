package me.moose.gaia.slave.socket;

import lombok.Getter;
import lombok.Setter;
import org.java_websocket.WebSocket;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
@Getter @Setter
public class SocketUser {
    private final UUID uuid;
    private final String username;
    private final String version;
    private final String server;
    private final String commit;
    private final WebSocket conn;
    private long sentAuthRequest;
    private boolean repliedAuthRequest;
    public SocketUser(WebSocket conn, UUID uuid, String username, String version, String server, String commit) {
        this.conn = conn;
        this.uuid = uuid;
        this.username = username;
        this.version = version;
        this.server = server;
        this.commit = commit;

        sentAuthRequest = -1;
        repliedAuthRequest = false;
    }

}
