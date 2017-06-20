package Commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class GitHubCommand implements CommandExecutor {

    @Command(aliases = { "gh", "github", "contribute" })
    public String onGithubCommand() {
        return "WIP";
    }

}
