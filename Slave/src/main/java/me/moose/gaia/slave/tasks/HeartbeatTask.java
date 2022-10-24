package me.moose.gaia.slave.tasks;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveHeartbeatPacket;
import me.moose.gaia.slave.socket.SlaveSocket;

/**
 * @author Moose1301
 * @date 10/24/2022
 */
public class HeartbeatTask implements Runnable {
    @Override
    public void run() {
        int unauthorizedUsers = SlaveSocket.getInstance().getUnauthorizedUsers().size();
        Runtime runtime = Runtime.getRuntime();
        double memoryFree = runtime.freeMemory();
        double memoryMax = runtime.maxMemory();
        double memoryUsage = runtime.maxMemory() - runtime.freeMemory();
        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveHeartbeatPacket(unauthorizedUsers, memoryUsage, memoryMax, memoryFree));

    }
}
