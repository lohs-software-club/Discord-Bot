package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import utils.Credentials;

import java.io.IOException;

public class GitHubCommand extends Commands implements CommandExecutor {

    @Command(aliases = { "gh", "github" }, async = true)
    public String addToGithubCommand(String[] username, IChannel channel, IGuild guild) {

        if (checkChannel(channel, guild.getChannelsByName("bot-spam"))) {
            start(channel);
        } else {
            return null;
        }

        // Checking if too many usernames are entered
        if (username.length > 1 || username.length < 1) {
            endWithDefaultCleanup(channel);
            return "Please only enter one GitHub username at a time.";
        } else {
            // One potential GH username entered. Check if user actually exists.

            try {
                GitHub gh =  GitHub.connectUsingOAuth(Credentials.getGhToken());

                GHOrganization loSoftwareClub = gh.getOrganization("lohs-software-club");

                GHUser user = gh.getUser(username[0]);
                loSoftwareClub.getTeamByName("Members").add(user);

                if (user.getName() != null) {
                    endWithDefaultCleanup(channel);
                    return "Successfully added " + user.getName() + " to the LOHS GitHub Team";
                } else {
                    endWithDefaultCleanup(channel);
                    return "Successfully added GitHub user " + user.getLogin() + " to the LOHS GitHub Team";
                }
            } catch (IOException e) {
                System.err.println(e.toString());
                System.out.println(e.toString());
                endWithNoCleanup(channel);
                return "An Error occurred. Please check the log.";
            }
        }

    }
}
