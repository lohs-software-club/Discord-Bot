package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class PingPong extends Commands implements CommandExecutor {

    @Command(aliases = { "bug" })
    public String pingCommand(IUser username, IChannel channel, IGuild guild) {

        channel.setTypingStatus(true);

        System.out.println(username.getDisplayName(guild) + " Sent a Ping.");

        end(channel, 5);//cleanup messages as
        return "Feature!";


    }
}