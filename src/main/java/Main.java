import Commands.GitHubCommand;
import Commands.RemoveCommand;
import Commands.RoleCommand;
import Utils.Credentials;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

public class Main {

    public static void main(String[] args) {

        // Logging in client
        IDiscordClient client = createClient(Credentials.discordToken);

        // Setting Events listener
        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener(new Events());

        CommandHandler handler = new Discord4JHandler(client);
        handler.setDefaultPrefix(".");
        handler.registerCommand(new RoleCommand());
        handler.registerCommand(new RemoveCommand());
        handler.registerCommand(new GitHubCommand());

    }

    /**
     * Builds a Discord Client and logs it in.
     * @param token The token for the Discord Client
     * @return Logged in Discord Client
     */
    private static IDiscordClient createClient(String token) {

        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            return clientBuilder.login();
        } catch (DiscordException e) {
            e.printStackTrace();
            return null;
        }

    }

}
