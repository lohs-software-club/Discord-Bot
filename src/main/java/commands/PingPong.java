package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IChannel;

public class PingPong extends Commands implements CommandExecutor {

    @Command(aliases = { "ping" })
    public String pingCommand(String[] username, IChannel channel, String[] arguments) {

        System.out.println("Pong!" + arguments[0]);

        return "Pong!" + arguments[0];

    }
}
