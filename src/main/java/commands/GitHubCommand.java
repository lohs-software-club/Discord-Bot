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

    @Command(aliases = { "gh", "github" })
    public String addToGithubCommand(String[] username, IChannel channel) {

        if (!canStart(channel)) {
            return null;
        }

        // Checking if too many usernames are entered
        if (username.length > 1 || username.length < 1) {
            end(channel);
            return "Please only enter one GitHub username at a time.";
        } else {
            // One potential GH username entered. Check if user actually exists.

            try {
                GitHub gh = GitHub.connectUsingOAuth(Credentials.ghToken);
                GHOrganization loSoftwareClub = gh.getOrganization("lohs-software-club");

                GHUser user = gh.getUser(username[0]);
                loSoftwareClub.getTeamByName("Members").add(user);

                if (user.getName() != null) {
                    end(channel);
                    return "Successfully added " + user.getName() + " to the LOHS GitHub Team";
                } else {
                    end(channel);
                    return "Successfully added GitHub user " + user.getLogin() + " to the LOHS GitHub Team";
                }
            } catch (IOException e) {
                System.err.println(e.toString());
                System.out.println(e.toString());
                end(channel, false);
                return "An Error occurred. Please check the log.";
            }
        }

    }
}
