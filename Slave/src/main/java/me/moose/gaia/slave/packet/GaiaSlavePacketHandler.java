package me.moose.gaia.slave.packet;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.cosmetics.GaiaMasterCosmeticListUpdate;
import me.moose.gaia.common.packet.packets.master.friend.GaiaMasterUserFriendUpdatePacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterStatusPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserInteractionPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserKickPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserMessagePacket;
import me.moose.gaia.common.packet.packets.slave.user.GaiaSlaveRequestUserDataPacket;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveStatusPacket;
import me.moose.gaia.common.packet.packets.slave.user.GaiaSlaveUserJoinPacket;
import me.moose.gaia.common.profile.friend.CommonFriend;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.slave.GaiaSlave;
import me.moose.gaia.slave.profile.data.Profile;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class GaiaSlavePacketHandler implements IGaiaSlavePacketHandler {


    @Override
    public void handle(GaiaMasterSlaveServerInfoPacket packet) {
        GaiaServer.getRedisHandler().setServerId(packet.getServerId());
        GaiaSlave.getInstance().setConnected(true);
        GaiaServer.getLogger().debug("Packet Handler", "Set Server ID to: " + packet.getServerId(), Logger.DebugType.SUCCESS);
        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveStatusPacket.Started());
    }


    @Override
    public void handle(GaiaMasterStatusPacket.Startup packet) {
        GaiaServer.getRedisHandler().setServerId(GaiaSlave.getInstance().getGaiaConfig().getId());
        GaiaServer.getLogger().debug("Packet Handler", "Master Server Startup");
        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveStatusPacket.Startup(
                GaiaSlave.getInstance().getGaiaConfig().getId(),
                GaiaSlave.getInstance().getGaiaConfig().getRegion())
        );

    }

    @Override
    public void handle(GaiaMasterStatusPacket.Shutdown packet) {
        GaiaServer.getLogger().debug("Packet Handler", "Master Server Shutdown");
        GaiaSlave.getInstance().setConnected(false);
        GaiaServer.getRedisHandler().setServerId(GaiaSlave.getInstance().getGaiaConfig().getId());
        //TODO KICK ALL PLAYERS AND DISABLE JOINS
    }

    @Override
    public void handle(GaiaMasterUserDataPacket.Loaded packet) {
        Profile profile = new Profile(packet.getUuid(), packet.getUsername());
        GaiaSlave.getInstance().getProfileHandler().getProfiles().put(packet.getUuid(), profile);
        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveRequestUserDataPacket(packet.getUuid(), GaiaSlaveRequestUserDataPacket.DataType.FRIENDS));
        if(GaiaSlave.getInstance().getProfileHandler().getLoadRequests().containsKey(packet.getUuid())) {
            GaiaSlave.getInstance().getProfileHandler().getLoadRequests().get(packet.getUuid()).complete(profile);
            GaiaSlave.getInstance().getProfileHandler().getLoadRequests().remove(packet.getUuid());
        }
    }

    @Override
    public void handle(GaiaMasterUserDataPacket.Friends packet) {
        Profile profile = GaiaSlave.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            GaiaServer.getLogger().error("PacketHandler", "Master sent a Profile Friends with UUID \"" + packet.getUuid().toString() + "\"");
            return;
        }
        profile.setFriends(packet.getFriends());
        profile.setFriendRequests(packet.getRequests());
        profile.setRequestsEnabled(packet.isRequestsEnabled());
        profile.setStatus(packet.getStatus());
        GaiaServer.getLogger().debug("PacketHandler", "Loaded " + profile.getUsername() + "'s Friends", Logger.DebugType.SUCCESS);
        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveRequestUserDataPacket(packet.getUuid(), GaiaSlaveRequestUserDataPacket.DataType.COSMETICS));
    }

    @Override
    public void handle(GaiaMasterUserDataPacket.Cosmetics packet) {
        Profile profile = GaiaSlave.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            GaiaServer.getLogger().error("PacketHandler", "Master sent a Profile Cosmetics with UUID \"" + packet.getUuid().toString() + "\"");
            return;
        }
        profile.setCosmetics(packet.getCosmetics());
        GaiaServer.getLogger().debug("PacketHandler", "Loaded " + profile.getUsername() + "'s Cosmetics", Logger.DebugType.SUCCESS);
        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveRequestUserDataPacket(packet.getUuid(), GaiaSlaveRequestUserDataPacket.DataType.DATA));

    }

    @Override
    public void handle(GaiaMasterUserDataPacket.Data packet) {
        Profile profile = GaiaSlave.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            GaiaServer.getLogger().error("PacketHandler", "Master sent a Profile Cosmetics with UUID \"" + packet.getUuid().toString() + "\"");
            return;
        }
        profile.setRank(packet.getRank());
        profile.setVersion(packet.getVersion());
        profile.setCommit(packet.getCommit());
        profile.setServer(packet.getServer());
        profile.setCompletelyLoaded(true);
        GaiaServer.getLogger().debug("PacketHandler", "Completely Loaded " + profile.getUsername() + "'s Profile", Logger.DebugType.SUCCESS);
    }


    @Override
    public void handle(GaiaMasterCosmeticListUpdate packet) {
        GaiaSlave.getInstance().getCosmeticHandler().addCosmetics(packet.getCosmetics());
        GaiaServer.getLogger().debug("PacketHandler", "Synced " + packet.getCosmetics().size() + " Cosmetics with Master", Logger.DebugType.SUCCESS);
        GaiaServer.getRedisHandler().sendPacket(new GaiaSlaveUserJoinPacket(
                UUID.fromString("285c25e3-74f6-47e0-81a6-4e74ceb54ed3"),
                "Moose1301",
                "Real",
                "Real",
                "Real"
        ));
    }

    @Override
    public void handle(GaiaMasterUserKickPacket packet) {
        //TODO KICK USER HERE
    }
    @Override
    public void handle(GaiaMasterUserInteractionPacket.Crash packet) {
        //TODO CRASH USER HERE
    }

    @Override
    public void handle(GaiaMasterUserMessagePacket.ConsoleMessage packet) {
        //TODO SEND USER CONSOLE MESSAGE
    }

    @Override
    public void handle(GaiaMasterUserMessagePacket.Notification packet) {
        //TODO SEND USER NOTIFICATION
    }

    @Override
    public void handle(GaiaMasterUserFriendUpdatePacket packet) {
        Profile profile = GaiaSlave.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            GaiaServer.getLogger().error("PacketHandler", "Master sent a Friend Update with UUID \"" + packet.getUuid().toString() + "\"");
            return;
        }
        profile.getFriend(packet.getFriend().getUuid()).ifPresent(commonFriend -> commonFriend.update(packet.getFriend()));
    }
}
