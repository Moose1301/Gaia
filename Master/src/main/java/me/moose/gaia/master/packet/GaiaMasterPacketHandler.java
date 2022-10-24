package me.moose.gaia.master.packet;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.handler.IGaiaMasterPacketHandler;
import me.moose.gaia.common.packet.packets.master.*;
import me.moose.gaia.common.packet.packets.master.cosmetics.GaiaMasterCosmeticListUpdate;
import me.moose.gaia.common.packet.packets.master.friend.GaiaMasterUserFriendUpdatePacket;
import me.moose.gaia.common.packet.packets.master.server.GaiaMasterSlaveServerInfoPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserDataPacket;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserKickPacket;
import me.moose.gaia.common.packet.packets.slave.friend.GaiaSlaveUserFriendStatusChangePacket;
import me.moose.gaia.common.packet.packets.slave.friend.GaiaSlaveUserRequestStateUpdatePacket;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveHeartbeatPacket;
import me.moose.gaia.common.packet.packets.slave.user.*;
import me.moose.gaia.common.packet.packets.slave.server.GaiaSlaveStatusPacket;
import me.moose.gaia.common.profile.friend.CommonFriend;
import me.moose.gaia.common.profile.rank.Rank;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.command.Command;
import me.moose.gaia.master.profile.friend.Friend;
import me.moose.gaia.master.utils.KickConstants;
import me.moose.gaia.master.profile.Profile;
import me.moose.gaia.master.profile.crash.CrashReport;
import me.moose.gaia.master.server.Server;

import java.util.Arrays;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class GaiaMasterPacketHandler implements IGaiaMasterPacketHandler {
    @Override
    public void handle(GaiaSlaveStatusPacket.Startup packet) {
        Server server = GaiaMaster.getInstance().getServerHandler().addServer(packet.getName(), packet.getRegion());
        GaiaServer.getRedisHandler().sendPacket(new GaiaMasterSlaveServerInfoPacket(server.getId()), server.getRealId());
    }

    @Override
    public void handle(GaiaSlaveStatusPacket.Started packet) {
        GaiaServer.getRedisHandler().sendPacket(
                new GaiaMasterCosmeticListUpdate(GaiaMaster.getInstance().getCosmeticHandler().getCommonCosmetics()),
                packet.getSendingID()
        );
    }
    @Override
    public void handle(GaiaSlaveStatusPacket.Shutdown packet) {
        GaiaMaster.getInstance().getServerHandler().removeServer(packet.getSendingID());
    }

    @Override
    public void handle(GaiaSlaveHeartbeatPacket packet) {
        Server server = GaiaMaster.getInstance().getServerHandler().getServer(packet.getSendingID());
        if(server == null) {
            return;
        }
        server.setUnauthorizedUsers(packet.getUnauthorizedUsers());
        server.setMemoryMax(packet.getMemoryMax());
        server.setMemoryUsage(packet.getMemoryUsage());
        server.setMemoryFree(packet.getMemoryFree());

    }

    @Override
    public void handle(GaiaSlaveUserJoinPacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfileOrLoad(packet.getUuid(), packet.getUsername());
        if(profile.isBanned()) {
            GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserKickPacket(packet.getUuid(), KickConstants.USER_BANNED));
            return;
        }
        profile.setCurrentSlave(packet.getSendingID());
        profile.setVersion(packet.getVersion());
        profile.setCommit(packet.getCommit());
        profile.setServer(packet.getServer());


        GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserDataPacket.Loaded(packet.getUuid(), packet.getUsername()), packet.getSendingID());
        Server server = GaiaMaster.getInstance().getServerHandler().getServer(packet.getSendingID());
        server.getProfiles().add(packet.getUuid());

        for (Friend friend : profile.getFriends()) {
            if(friend.getProfile() != null) {
                Profile friendProfile = friend.getProfile();
                friendProfile.getFriend(profile.getUniqueId()).ifPresent(friend1 -> {
                    friend1.setOnline(true);
                    if(friendProfile.isOnline()) {
                        GaiaServer.getRedisHandler().sendPacket(
                                new GaiaMasterUserFriendUpdatePacket(friend.getPlayerId(), friend1.toCommon()),
                                friendProfile.getCurrentSlave()
                        );
                    }
                });
            }

        }


    }
    @Override
    public void handle(GaiaSlaveUserLeavePacket packet) {
        Server server = GaiaMaster.getInstance().getServerHandler().getServer(packet.getSendingID());
        server.getProfiles().remove(packet.getUuid());
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            return;
        }
        profile.setOfflineSince(System.currentTimeMillis());
        for (Friend friend : profile.getFriends()) {
            if(friend.getProfile() != null) {
                Profile friendProfile = friend.getProfile();
                friendProfile.getFriend(profile.getUniqueId()).ifPresent(friend1 -> {
                    friend1.setOnline(false);
                    friend1.setOfflineSince(profile.getOfflineSince());
                    if(friendProfile.isOnline()) {
                        GaiaServer.getRedisHandler().sendPacket(
                                new GaiaMasterUserFriendUpdatePacket(friend.getPlayerId(), friend1.toCommon()),
                                friendProfile.getCurrentSlave()
                        );
                    }
                });
            }

        }


    }
    @Override
    public void handle(GaiaSlaveRequestUserDataPacket packet) {
        if(packet.getType() == GaiaSlaveRequestUserDataPacket.DataType.LOAD) {
            GaiaMaster.getInstance().getProfileHandler().getProfileOrLoad(packet.getUuid(), packet.getUsername());
            GaiaServer.getRedisHandler().sendPacket(new GaiaMasterUserDataPacket.Loaded(packet.getUuid(), packet.getUsername()), packet.getSendingID());
            return;
        }
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            GaiaServer.getLogger().error("PacketHandler", packet.getSendingID() + " Requested Profile with UUID \"" +
                    packet.getUuid().toString() + "\" but it wasn't loaded");
            return;
        }
        GaiaMasterPacket sendingPacket = null;
        switch (packet.getType()) {
            case DATA:
                sendingPacket = new GaiaMasterUserDataPacket.Data(
                        profile.getUniqueId(), profile.getRank(),
                        profile.getVersion(), profile.getServer(),
                        profile.getCommit()
                );
                break;
            case COSMETICS:
                sendingPacket = new GaiaMasterUserDataPacket.Cosmetics(profile.getUniqueId(), profile.getCommonCosmetics());
                break;
            case FRIENDS:
                sendingPacket = new GaiaMasterUserDataPacket.Friends(
                        profile.getUniqueId(), profile.getCommonFriends(),
                        profile.getCommonFriendRequests(), profile.isAcceptingRequests(),
                        profile.getStatus()
                );
                break;
        }
        if(sendingPacket == null) {
            return;
        }
        GaiaServer.getRedisHandler().sendPacket(sendingPacket, packet.getSendingID());
    }

    @Override
    public void handle(GaiaSlaveUserCrashReportPacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            return;
        }
        profile.getCrashReports().add(new CrashReport(
                packet.getId(),
                packet.getVersion(),
                packet.getOs(),
                packet.getMemory(),
                packet.getStackTrace()
        ));
        GaiaServer.getLogger().debug("PacketHandler", "Added a Crash Report to " + profile.getUsername() + "'s profile", Logger.DebugType.SUCCESS);
    }

    @Override
    public void handle(GaiaSlaveUserRequestStateUpdatePacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            return;
        }
        profile.setAcceptingRequests(packet.isFriendRequests());
    }

    @Override
    public void handle(GaiaSlaveUserFriendStatusChangePacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().getProfile(packet.getUuid());
        if(profile == null) {
            return;
        }
        profile.setStatus(packet.getStatus());
        CommonFriend profileFriend = profile.toFriend();
        for (Friend friend : profile.getFriends()) {
            if(friend.getProfile() != null) {
                Profile friendProfile = friend.getProfile();
                friendProfile.getFriend(profile.getUniqueId()).ifPresent(friend1 -> friend1.setOnlineStatus(profile.getStatus()));
                if(profile.isOnline()) {
                    GaiaServer.getRedisHandler().sendPacket(
                            new GaiaMasterUserFriendUpdatePacket(friend.getPlayerId(), profileFriend),
                            friendProfile.getCurrentSlave()
                    );
                }
            }
        }

        GaiaServer.getLogger().debug("PacketHandler", "Updated " + profile.getUsername() + " Friends with their new status", Logger.DebugType.SUCCESS);
    }

    @Override
    public void handle(GaiaSlaveUserConsoleMessagePacket packet) {
        Profile profile = GaiaMaster.getInstance().getProfileHandler().fromUuid(packet.getUuid(), false);

        String[] args = packet.getMessage().split(" ");
        Command command = GaiaMaster.getInstance().getCommandHandler().getCommand(args[0].toLowerCase());

        if (command != null) {
            if (command.getRequiredRank() == null || Rank.isValid(command.getRequiredRank(), profile.getRank())) {

                // Require login whenever the command requires MOD+
                if (command.getRequiredRank() != null && Rank.isValid(Rank.DEVELOPER, command.getRequiredRank())) {
                    /*
                    if (!LoginCommand.LOGGED_IN_USERS.contains(profile.getUniqueId())) {
                        profile.sendMessage("&cYou must be logged in to run this command because it requires "
                                + Rank.DEVELOPER.getFormatColor() + "MOD&c+.");
                        return;
                    }

                     */
                }

                try {
                    if (command.isAsync()) {
                        GaiaMaster.getInstance().getExecutor().execute(() -> command.executeCommand(profile, Arrays.copyOfRange(args, 1, args.length)));
                    } else {
                        command.executeCommand(profile, Arrays.copyOfRange(args, 1, args.length));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                profile.sendMessage("&cYou don't have the required rank to run the &f" + command.getAliases()[0]
                        + " &ccommand. (Required Rank: " + command.getRequiredRank().name() + "&c)");
            }
        } else {
            profile.sendMessage("&c" + args[0] + " isn't a valid command.");
        }
    }
}
