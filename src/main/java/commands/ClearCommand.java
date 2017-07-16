package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IChannel;

public class ClearCommand extends Commands implements CommandExecutor {

    @Command(aliases = { "clear" })
    public String clearChat(IChannel channel) {

        if (!canStartInBotSpam(channel)) {
            return null;
        }

        endWithNoCleanup(channel);
        return null;


    }
}