import Commands.GitHubCommand;
import Commands.RemoveCommand;
import Commands.RoleCommand;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {

        // Read in login credential
        String token = "";
        try {
            FileReader fr = new FileReader("credential.txt");
            BufferedReader br = new BufferedReader(fr);
            token = br.readLine();
        } catch (IOException e)
        {
            System.err.println(e.toString());
            exit(1);
        }



        // Logging in client
        IDiscordClient client = createClient(token);

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
