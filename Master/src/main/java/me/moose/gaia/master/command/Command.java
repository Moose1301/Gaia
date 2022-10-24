package me.moose.gaia.master.command;


import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.profile.rank.Rank;
import me.moose.gaia.master.profile.Profile;
import me.moose.gaia.master.utils.EnumChatFormatting;

public abstract class Command {
    @Getter private String[] aliases;

    @Getter @Setter private boolean async;
    @Getter @Setter private Rank requiredRank;

    public Command(boolean async, Rank requiredRankType, String... aliases) {
        this.async = async;
        this.requiredRank = requiredRankType;
        this.aliases = aliases;
    }

    public Command(boolean async, String... aliases) {
        this.async = async;
        this.aliases = aliases;
    }

    public abstract void executeCommand(Profile sender, String[] args);

    public void sendMessage(Profile sender, String... strings) {
        if (sender != null) {
            sender.sendMessage(strings);
        } else {
            for (String string : strings) {
                System.out.println("> " + EnumChatFormatting.getTextWithoutFormattingCodes(EnumChatFormatting.translate(string)));
            }
        }
    }
}
