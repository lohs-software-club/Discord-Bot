package commands;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageHistory;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by Adrian on 6/30/17.
 * these methods apply to all commands
 */
class Commands {

    private List<ScheduledExecutorService> runningExecutors = new ArrayList<>();

    private void clearAllButWelcomeMessageInBotSpamChannelAfter(IChannel currentChannel, Integer timeInSeconds) {

        List<IMessage> messages = new ArrayList<>(
                RequestBuffer.request(() -> (MessageHistory) currentChannel.getMessageHistory(50)).get()
        );

        clearAllButMessageIDInChannelAfterTime(
                messages.get(messages.size()-1).getLongID(),//get first message in list
                currentChannel,
                getBotSpamChannel(currentChannel.getGuild()),
                timeInSeconds
        );
    }

    private void clearAllButMessageIDInChannelAfterTime(Long id, IChannel currentChannel, IChannel channelToClear, Integer timeInSeconds) {


        //TODO: make this into a discord message that says something like "this message will self destruct"
        //System.out.println("WAITING SPECIFIED SECONDS...");

        //don't do anything if you aren't in the channel specified for clearing
        if (currentChannel == channelToClear) {

            //if list of executors isnt empty
            if (!runningExecutors.isEmpty()) {
                System.out.println("Shutting down open executors");

                //loop through and shut them all down
                for (ScheduledExecutorService item : runningExecutors) {

                    item.shutdown();
                }
            }

            ScheduledExecutorService newSES = Executors.newSingleThreadScheduledExecutor();

            runningExecutors.add(newSES);
            System.out.println("making new executor:" + newSES);

            newSES.schedule(() -> {

                runningExecutors.remove(newSES);
                System.out.println("completing executor:" + newSES);
                //get the last 50 messages in the channel (as an additional safety measure)
                List<IMessage> messages = new ArrayList<>(
                        RequestBuffer.request(() -> (MessageHistory) currentChannel.getMessageHistory(50)).get()
                );

                // USE THIS TO GET THE ID OF THE FIRST MESSAGE IN THE CHANNEL IF YOU NEED TO CHANGE THE ID
                // System.out.println(messages.get(messages.size()-1).getLongID());

                //delete any messages with the specified ID (preserve welcome message for example)
                messages.removeIf(m -> m.getLongID() == id);

                //delete whats left in the messages list
                if (!messages.isEmpty()) {
                    RequestBuffer.request(() -> {currentChannel.bulkDelete(messages);
                    System.out.println("Nom Nom Nom. Yummy Messages.");
                    });
                }

            }, timeInSeconds, TimeUnit.SECONDS);

        }
    }

    //use this for commands that should be limited to only #bot-spam channel
    Boolean isInBotSpam(IChannel channel) {
        //if channel is not #bot-spam, get the bot to just not respond.
        if (channel != getBotSpamChannel(channel.getGuild())) {
            return true;
        } else {
            start(channel);
            return false;
        }
    }

    IChannel getBotSpamChannel(IGuild guild) {
        return guild.getChannelsByName("bot-spam").get(0);
    }

    //Give feedback to user that bot has started working
    //this is a convenient function to allow quick, easy changes to the code that ALL commands use before getting into the command-specific code
    void start(IChannel currentChannel) {
        currentChannel.setTypingStatus(true);
    }


    void endWithNoCleanup(IChannel currentChannel) {
        endWithSpecifiedCleanup(currentChannel, 0);
    }

    //set a default value for the cleanup time for convenience
    void endWithDefaultCleanup(IChannel currentChannel) {
        endWithSpecifiedCleanup(currentChannel, 60);
    }

    //Give feedback to user that bot is done working
    void endWithSpecifiedCleanup(IChannel currentChannel, Integer cleanupInterval) {
        currentChannel.setTypingStatus(false);

        //if there is a cleanup interval greater than 0, then clean up.
        if (cleanupInterval >= 0) {
            clearAllButWelcomeMessageInBotSpamChannelAfter(currentChannel, cleanupInterval);
        }

    }


    List<IRole> getSubscribeableRolesList(IGuild guild) {

        List<IRole> subscribedRolesList = new ArrayList<>();

        for (IRole role : guild.getRoles()) {

            //if last character of role name is *,
            if (role.getName().substring(role.getName().length() - 1).equals("*")) {

                subscribedRolesList.add(role);

            }
        }

        return subscribedRolesList;
    }


}