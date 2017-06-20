package Commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class RemoveCommand implements CommandExecutor {

    private IGuild guild;

    @Command(aliases = {"undevelop", "remove"})
    public String onRemoveCommand(IGuild guild, IUser user, String[] roles) {

        this.guild = guild;

        // Making sure user is verified
        if (user.getRolesForGuild(guild).contains(getRole("Verified Member"))) {

            for (String mRole : roles) {
                String role = mRole.toLowerCase();

                switch (role) {
                    case "java":
                        user.removeRole(getRole("Java"));
                        break;
                    case "swift":
                        user.removeRole(getRole("Swift"));
                        break;
                    case "javascript":
                    case "js":
                        user.removeRole(getRole("Javascript"));
                        break;
                    case "c++":
                        user.removeRole(getRole("C++"));
                        break;
                    case "c#":
                        user.removeRole(getRole("C#"));
                        break;
                    case "python":
                        user.removeRole(getRole("Python"));
                        break;
                    case "web":
                        user.removeRole(getRole("Web"));
                        break;
                    case "vcs":
                        user.removeRole(getRole("VCS"));
                        break;
                    case "kotlin":
                        user.removeRole(getRole("Kotlin"));
                        break;
                    default:
                        return "Invalid role!";
                }
                return "Role successfully removed!";
            }

        } else {
            return "You must be a verified member!";
        }

        return "";
    }

    private IRole getRole(String roleName) {
        for (IRole iRole : guild.getRoles()) {
            if (iRole.getName().equalsIgnoreCase(roleName)) {
                return iRole;
            }
        }
        return null;
    }
}