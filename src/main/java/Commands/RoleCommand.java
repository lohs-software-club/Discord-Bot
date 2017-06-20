package Commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class RoleCommand implements CommandExecutor {

    private IGuild guild;

    @Command(aliases = {"role", "developer", "add"})
    public String onRoleCommand(IGuild guild, IUser user, String[] roles) {

        this.guild = guild;

        // Making sure user is verified
        if (user.getRolesForGuild(guild).contains(getRole("Verified Member"))) {

            for (String mRole : roles) {
                String role = mRole.toLowerCase();

                switch (role) {
                    case "java":
                        user.addRole(getRole("Java"));
                        break;
                    case "swift":
                        user.addRole(getRole("Swift"));
                        break;
                    case "javascript":
                    case "js":
                        user.addRole(getRole("Javascript"));
                        break;
                    case "c++":
                        user.addRole(getRole("C++"));
                        break;
                    case "c#":
                        user.addRole(getRole("C#"));
                        break;
                    case "python":
                        user.addRole(getRole("Python"));
                        break;
                    case "web":
                        user.addRole(getRole("Web"));
                        break;
                    case "vcs":
                        user.addRole(getRole("VCS"));
                        break;
                    case "kotlin":
                        user.addRole(getRole("Kotlin"));
                        break;
                    default:
                        return "Invalid role!";
                }
                return "Role successfully added!";
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