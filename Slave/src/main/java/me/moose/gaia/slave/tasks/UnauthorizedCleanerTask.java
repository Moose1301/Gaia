package me.moose.gaia.slave.tasks;

import me.moose.gaia.slave.socket.SlaveSocket;
import me.moose.gaia.slave.socket.SocketUser;
import me.moose.gaia.slave.utils.KickConstants;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Moose1301
 * @date 10/24/2022
 */
public class UnauthorizedCleanerTask implements Runnable {
    private static final long MAX_RESPONSE_TIME = TimeUnit.SECONDS.toMillis(30);
    @Override
    public void run() {
        for (UUID unauthorizedUser : SlaveSocket.getInstance().getUnauthorizedUsers()) {
            SocketUser user = SlaveSocket.getInstance().getUsers().get(unauthorizedUser);
            if(user.getSentAuthRequest() < System.currentTimeMillis() + MAX_RESPONSE_TIME) {
                user.getConn().close(4000, KickConstants.FAILED_AUTH_NO_REPLY);
            }
        }
    }
}
