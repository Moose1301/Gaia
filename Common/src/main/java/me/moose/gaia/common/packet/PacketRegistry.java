package me.moose.gaia.common.packet;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.handler.IGaiaPacketHandler;
import me.moose.gaia.common.packet.packets.master.cosmetics.GaiaMasterCosmeticListUpdate;
import me.moose.gaia.common.packet.packets.master.friend.GaiaMasterUserFriendUpdatePacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterSlaveRestartPacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterStatusPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserInteractionPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserKickPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserMessagePacket;
import me.moose.gaia.common.packet.packets.slave.friend.GaiaSlaveUserFriendStatusChangePacket;
import me.moose.gaia.common.packet.packets.slave.friend.GaiaSlaveUserRequestStateUpdatePacket;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveStatusPacket;
import me.moose.gaia.common.packet.packets.slave.user.*;
import me.moose.gaia.common.utils.Logger;

import java.util.HashMap;

/**
 * @author Moose1301
 * @date 10/21/2022
 */
public enum PacketRegistry {
    MASTER {
        {
            registerPacket(GaiaMasterSlaveServerInfoPacket.class);
            registerPacket(GaiaMasterStatusPacket.Startup.class);
            registerPacket(GaiaMasterStatusPacket.Shutdown.class);
            registerPacket(GaiaMasterCosmeticListUpdate.class);
            registerPacket(GaiaMasterSlaveRestartPacket.class);

            //User Data Shit
            registerPacket(GaiaMasterUserDataPacket.Loaded.class);
            registerPacket(GaiaMasterUserDataPacket.Data.class);
            registerPacket(GaiaMasterUserDataPacket.Friends.class);
            registerPacket(GaiaMasterUserDataPacket.Cosmetics.class);
            registerPacket(GaiaMasterUserKickPacket.class);

            //User Interaction Shit
            registerPacket(GaiaMasterUserInteractionPacket.Crash.class);
            registerPacket(GaiaMasterUserMessagePacket.ConsoleMessage.class);
            registerPacket(GaiaMasterUserMessagePacket.Notification.class);

            //Friend Shit
            registerPacket(GaiaMasterUserFriendUpdatePacket.class);

            GaiaServer.getLogger().debug("PacketRegistry", "Registered " + registeredPackets.size() + " Master Packets");
        }
    },
    SLAVE {
        {
            registerPacket(GaiaSlaveStatusPacket.Startup.class);
            registerPacket(GaiaSlaveStatusPacket.Started.class);
            registerPacket(GaiaSlaveStatusPacket.Shutdown.class);
            registerPacket(GaiaSlaveUserJoinPacket.class);
            registerPacket(GaiaSlaveUserLeavePacket.class);
            registerPacket(GaiaSlaveRequestUserDataPacket.class);
            registerPacket(GaiaSlaveUserCrashReportPacket.class);
            registerPacket(GaiaSlaveUserFriendStatusChangePacket.class);
            registerPacket(GaiaSlaveUserConsoleMessagePacket.class);



            GaiaServer.getLogger().debug("PacketRegistry", "Registered " + registeredPackets.size() + " Slave Packets");
        }

    };


    protected final HashMap<Integer, Class<? extends GaiaPacket<?>>> registeredPackets = new HashMap<>();
    protected final HashMap<Class<? extends GaiaPacket<?>>, Integer> reverseLookup = new HashMap<>();

    protected void registerPacket(Class<? extends GaiaPacket<?>> packetClass) {
        if (registeredPackets.containsValue(packetClass))
            throw new IllegalArgumentException("Packet " + packetClass.getSimpleName() + " was registered twice!");

        int id = registeredPackets.size();

        GaiaServer.getLogger().debug("PacketRegistry", "Registered " + name() + " Packet: " + packetClass.getSimpleName() + " to " + id,
                Logger.DebugType.SUCCESS);
        registeredPackets.put(id, packetClass);
        reverseLookup.put(packetClass, id);
    }

    public int getPacketId(Class<? extends GaiaPacket<?>> packet) {
        return reverseLookup.get(packet);
    }

    public GaiaPacket<IGaiaPacketHandler> createPacket(int packetId) throws IllegalAccessException, InstantiationException {
        if (!registeredPackets.containsKey(packetId)) return null;

        return (GaiaPacket<IGaiaPacketHandler>) registeredPackets.get(packetId).newInstance();
    }
}