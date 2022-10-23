package me.moose.gaia.common.packet;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.handler.IGaiaPacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterCosmeticListUpdate;
import me.moose.gaia.common.packet.packets.master.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.GaiaMasterStatusPacket;
import me.moose.gaia.common.packet.packets.master.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveRequestUserDataPacket;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveStatusPacket;
import me.moose.gaia.common.packet.packets.slave.GaiaSlaveUserJoinPacket;
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

            //User Data Shit
            registerPacket(GaiaMasterUserDataPacket.Loaded.class);
            registerPacket(GaiaMasterUserDataPacket.Data.class);
            registerPacket(GaiaMasterUserDataPacket.Friends.class);
            registerPacket(GaiaMasterUserDataPacket.Cosmetics.class);
        }
    },
    SLAVE {
        {
            registerPacket(GaiaSlaveStatusPacket.Startup.class);
            registerPacket(GaiaSlaveStatusPacket.Started.class);
            registerPacket(GaiaSlaveStatusPacket.Shutdown.class);
            registerPacket(GaiaSlaveUserJoinPacket.class);
            registerPacket(GaiaSlaveRequestUserDataPacket.class);

        }

    };


    private final HashMap<Integer, Class<? extends GaiaPacket<?>>> registeredPackets = new HashMap<>();
    private final HashMap<Class<? extends GaiaPacket<?>>, Integer> reverseLookup = new HashMap<>();

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