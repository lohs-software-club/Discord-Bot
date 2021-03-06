import commands.GitHubCommand;
import commands.PingPong;
import commands.RoleCommands;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;
import utils.Credentials;

public class Main {

    public static void main(String[] args) {

        try {
            String discordToken = System.getenv("DISCORD_TOKEN");
            String ghToken = System.getenv("GITHUB_TOKEN");

            Credentials.setGhToken(ghToken);

            // Logging in client
            IDiscordClient client = createClient(discordToken);

            // Setting Events listener
            EventDispatcher dispatcher = client.getDispatcher();
            dispatcher.registerListener(new Events());

            CommandHandler handler = new Discord4JHandler(client);
            handler.setDefaultPrefix(".");
            handler.registerCommand(new PingPong());
            handler.registerCommand(new RoleCommands());
            handler.registerCommand(new GitHubCommand());
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Invalid Tokens");
        }

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
