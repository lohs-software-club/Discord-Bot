package commands;

import utils.Credentials;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import sx.blah.discord.handle.obj.IChannel;

import java.io.IOException;

public class GitHubCommand extends Commands implements CommandExecutor {

    @Command(aliases = { "gh", "github", "contribute" })
    public String onGithubCommand(String[] username, IChannel channel) {

        channel.setTypingStatus(true);  // Give feedback to user that bot is working

        // Checking if too many usernames are entered
        if (username.length > 1 || username.length < 1) {
            return "Incorrect number of arguments! Please only enter one GitHub username at a time.";
        } else {
            // One potential GH username entered. Check if user actually exists.

            // Read in login credential
            String OAuthToken = "";
            try {
                GitHub gh = GitHub.connectUsingOAuth(Credentials.ghToken);
                GHOrganization loSoftwareClub = gh.getOrganization("lohs-software-club");

                GHUser user = gh.getUser(username[0]);
                loSoftwareClub.getTeamByName("Members").add(user);
                if (user.getName() != null) {
                    done(channel);
                    return "Successfully added GitHub user " + user.getName() + " to the LOHS GitHub Team";

                } else {
                    done(channel);
                    return "Successfully added GitHub user " + user.getLogin() + " to the LOHS GitHub Team";

                }
            } catch (IOException e) {
                System.err.println(e.toString());
                return "An error occurred \n" + e.toString();
            }
        }

    }
}
