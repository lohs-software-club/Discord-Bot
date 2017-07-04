package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class PingPong extends Commands implements CommandExecutor {

    @Command(aliases = { "bug" })
    public String pingCommand(IUser username, IChannel channel, IGuild guild) {

        start(channel);

        System.out.println(username.getDisplayName(guild) + " Sent a Ping.");

        endWithNoCleanup(channel);
        return "Feature!";


    }
}