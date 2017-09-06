package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class RoleCommands extends Commands  implements CommandExecutor {

    private IGuild guild;

    @Command(aliases = { "subscribe", "sub" })
    public String addRoleCommand(IGuild guild, IChannel channel, IUser user, IMessage message, String[] roles) throws RateLimitException, DiscordException, MissingPermissionsException {

        if (!isInBotSpam(channel)) {
            return null;
        }
        this.guild = guild;

        String rolesSuccessfullyModified = "";

        // Making sure user is verified
        if (user.getRolesForGuild(guild).contains(getRole("Verified Member"))) {



            if (roles.length >= 1) {
                for (String mRole : roles) {
                    String role = mRole.toLowerCase();

                    rolesSuccessfullyModified += parseRoles(false, role, user);


                }
            } else {
                endWithDefaultCleanup(channel);
                return "Please specify a role to subscribe to.";
            }


            if (!rolesSuccessfullyModified.equals("")) {

                endWithDefaultCleanup(channel);
                return "Successfully subscribed to:" + rolesSuccessfullyModified;
            } else {
                endWithDefaultCleanup(channel);
                return "Please enter a valid role name from the list.";
            }

        } else {
            endWithDefaultCleanup(channel);
            return "You must be a verified member!";
        }


    }

    @Command(aliases = { "unsubscribe", "unsub" })
    public String removeRoleCommand(IGuild guild, IChannel channel, IUser user, IMessage message, String[] roles) throws RateLimitException, DiscordException, MissingPermissionsException {

        if (!isInBotSpam(channel)) {
            return null;
        }

        this.guild = guild;

        String rolesSuccessfullyModified = "";

        // Making sure user is verified
        if (user.getRolesForGuild(guild).contains(getRole("Verified Member"))) {



            if (roles.length >= 1) {
                for (String mRole : roles) {
                    String role = mRole.toLowerCase();

                    rolesSuccessfullyModified += parseRoles(true, role, user);


                }
            } else {
                endWithDefaultCleanup(channel);
                return "Please specify a role to unsubscribe from.";
            }


            if (!rolesSuccessfullyModified.equals("")) {

                endWithDefaultCleanup(channel);
                return "Successfully unsubscribed from:" + rolesSuccessfullyModified;
            } else {
                endWithDefaultCleanup(channel);
                return "Please enter a valid role name from the list.";
            }

        } else {
            endWithDefaultCleanup(channel);
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

    private String parseRoles(Boolean deleteRole, String role, IUser user) {

        //String[] roles = ["Java", "Swift", "JavaScript", "C++", "C#", "Python", "Web", "VCS", "Kotlin", "Security"];
        List<IRole> roleList = guild.getRoles();
        String rolesSucessfullyModified = "";

        List<IRole> subscribeableRoles = getSubscribeableRolesList(guild);

        //remove roles from the list if they dont begin with the * character
       // roleList.removeIf(m -> !(m.getName().substring(0,1) == "*"));

        //for every role on the server

        for (IRole aRole : roleList) {

            //if the role is a valid subscription role and the name matches the user input
            if (subscribeableRoles.contains(aRole) && aRole.getName().toLowerCase().substring(0, aRole.getName().length() - 1).equals(role)) {

                    if (deleteRole) {RequestBuffer.request(() -> user.removeRole(aRole));}
                    else RequestBuffer.request(() -> user.addRole(aRole));

                    rolesSucessfullyModified = rolesSucessfullyModified + " **" + aRole.getName().substring(0, aRole.getName().length() - 1) + "**";

            }
        }
        return rolesSucessfullyModified;
    }


    @Command(aliases = { "check", "subs" })
    public String checkSubscriptions(IUser user, IGuild guild, IChannel channel, String[] roles) throws RateLimitException, DiscordException, MissingPermissionsException {

        if (!isInBotSpam(channel)) {
            return null;
        }

        String rolesSubbedTo = "";
        List<IRole> subscribeableRoles = getSubscribeableRolesList(guild);

        for (IRole aRole : user.getRolesForGuild(guild)) {

            //if the role is a valid subscription role
            if (subscribeableRoles.contains(aRole)) {

                rolesSubbedTo += " **" + aRole.getName().substring(0, aRole.getName().length() - 1) + "**\r\n";
            }
        }

        //channel.sendMessage("lalala");

        endWithDefaultCleanup(channel);
        return "Hello " + user.getDisplayName(guild) + ". You are subscribed the following notification groups:\r\n" + rolesSubbedTo;
    }

}