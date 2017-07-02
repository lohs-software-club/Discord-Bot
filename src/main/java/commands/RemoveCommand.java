package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class RemoveCommand extends Commands  implements CommandExecutor {

    private IGuild guild;

    @Command(aliases = { "remove", "undevelop" })
    public String onRoleCommand(IGuild guild, IChannel channel, IUser user, IMessage message, String[] roles) throws RateLimitException, DiscordException, MissingPermissionsException {

        channel.setTypingStatus(true);  // Give feedback to user that bot is working

        this.guild = guild;

        // Making sure user is verified
        if (user.getRolesForGuild(guild).contains(getRole("Verified Member"))) {

            StringBuilder addedRolesBuilder = new StringBuilder(" roles successfully removed for user " + user.getDisplayName(guild) + "**:");
            int numRemovedRoles = 0;

            for (String mRole : roles) {
                String role = mRole.toLowerCase();
                numRemovedRoles++;

                switch (role) {
                    case "java":
                        user.removeRole(getRole("Java"));
                        addedRolesBuilder.append(" Java");
                        break;
                    case "swift":
                        user.removeRole(getRole("Swift"));
                        addedRolesBuilder.append(" Swift");
                        break;
                    case "javascript":
                    case "js":
                        user.removeRole(getRole("Javascript"));
                        addedRolesBuilder.append(" Javascript");
                        break;
                    case "c++":
                        user.removeRole(getRole("C++"));
                        addedRolesBuilder.append(" C++");
                        break;
                    case "c#":
                        user.removeRole(getRole("C#"));
                        addedRolesBuilder.append(" C#");
                        break;
                    case "python":
                        user.removeRole(getRole("Python"));
                        addedRolesBuilder.append(" Python");
                        break;
                    case "web":
                        user.removeRole(getRole("Web"));
                        addedRolesBuilder.append(" Web");
                        break;
                    case "vcs":
                        user.removeRole(getRole("VCS"));
                        addedRolesBuilder.append(" VCS");
                        break;
                    case "kotlin":
                        user.removeRole(getRole("Kotlin"));
                        addedRolesBuilder.append(" Kotlin");
                        break;
                    case "security":
                        user.removeRole(getRole("Security"));
                        addedRolesBuilder.append(" Security");
                        break;
                    default:
                        done(channel);
                        return "Invalid role: " + role;
                }
            }

            if (numRemovedRoles == 0) {
                done(channel);
                return "Please specify a role to remove.";
            }
            // Done looping through selected roles, add them now.
            done(channel);
            return addedRolesBuilder.insert(0, "**" + numRemovedRoles).toString();

        } else {
            done(channel);
            return "You must be a verified member!";
        }
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