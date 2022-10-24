package me.moose.gaia.master.command.impl;

import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.packet.packets.master.user.GaiaMasterUserInteractionPacket;
import me.moose.gaia.common.profile.rank.Rank;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.command.Command;
import me.moose.gaia.master.profile.Profile;
import me.moose.gaia.master.profile.ProfileHandler;

import java.util.Arrays;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
public class TestingCommand extends Command {
    public TestingCommand() {
        super(true, Rank.DEFAULT, "dev");
    }

    @Override
    public void executeCommand(Profile sender, String[] args) {
        sender.sendMessage("Testing: " + Arrays.toString(args));
    }
}
