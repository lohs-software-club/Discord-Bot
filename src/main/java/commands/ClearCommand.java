package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class ClearCommand extends Commands implements CommandExecutor {

    @Command(aliases = { "clear" })
    public String clearChat(IChannel channel, IGuild guild) {

        if (checkChannel(channel, guild.getChannelsByName("bot-spam"))) {
            start(channel);
        } else {
            return null;
        }

        endWithSmallCleanup(channel);
        return "";


    }
}