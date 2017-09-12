package commands;

import sx.blah.discord.handle.impl.obj.Guild;
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

    //THESE ALL FORCE BOT TO BE IN #bot-spam


    void endWithNoCleanup(IChannel currentChannel) {
        endWithSpecifiedCleanup(currentChannel, 0);
    }

    void endWithSmallCleanup(IChannel currentChannel) {
        endWithSpecifiedCleanup(currentChannel, 10);
    }


    //set a default value for the cleanup time for convenience
    void endWithDefaultCleanup(IChannel currentChannel) {
        endWithSpecifiedCleanup(currentChannel, 60);
    }


    //Give feedback to user that bot is done working
    void endWithSpecifiedCleanup(IChannel currentChannel, Integer cleanupInterval) {
        currentChannel.setTypingStatus(false);

        //if there is a cleanup interval greater than or equal to 0 and the bot is in bot spam, then clean up.
        if (cleanupInterval > 0 && checkChannel(currentChannel, currentChannel.getGuild().getChannelsByName("bot-spam"))) {
          //  System.out.println(currentChannel.getPinnedMessages());

//            List<IMessage> messages = new ArrayList<>(
//                    RequestBuffer.request(() -> (MessageHistory) currentChannel.getMessageHistory(50)).get()
//            );

            List<IMessage> pins = currentChannel.getPinnedMessages();

            clearAllButMessageIDInChannelAfterTime(
                    pins.get(pins.size()-1).getLongID(),//get first pinned message in channel (last in list)
                    currentChannel,
                    currentChannel.getGuild().getChannelsByName("bot-spam"),
                    cleanupInterval
            );
        } else {
            System.out.println("could not clean up in channel:" + currentChannel);
        }

    }


    private void clearAllButMessageIDInChannelAfterTime(Long id, IChannel currentChannel, List<IChannel> channelToClear, Integer timeInSeconds) {


        //TODO: make this into a discord message that says something like "this message will self destruct"
        //System.out.println("WAITING SPECIFIED SECONDS...");

        //don't do anything if you aren't in the channel specified for clearing


        if (checkChannel(currentChannel, channelToClear)) {

            //if list of executors isnt empty
            System.out.println("executor list is empty:" + runningExecutors.isEmpty());
            System.out.println(runningExecutors);

            if (!runningExecutors.isEmpty()) {

                System.out.println("Shutting down open executors...");

                //loop through and shut them all down
                //for (ScheduledExecutorService item : runningExecutors) {
                for (int i = 0; i <= runningExecutors.size()-1; i++) {
                    System.out.println("shutting down executor:" + runningExecutors.get(i));

                    runningExecutors.get(i).shutdownNow();
                }
            }

            ScheduledExecutorService newSES = Executors.newSingleThreadScheduledExecutor();

            System.out.println(runningExecutors.size());



            newSES.schedule(() -> {

                runningExecutors.remove(newSES);//remove self from executors list
                System.out.println("completing executor:" + newSES);
                //get the last 50 messages in the channel (as an additional safety measure)
                List<IMessage> messages = new ArrayList<>(
                        RequestBuffer.request(() -> (MessageHistory) currentChannel.getMessageHistory(50)).get()
                );

                //delete any messages with the specified ID (preserve welcome message for example)
                messages.removeIf(m -> m.getLongID() == id);

                //delete whats left in the messages list
                if (!messages.isEmpty()) {
                    RequestBuffer.request(() -> {currentChannel.bulkDelete(messages);
                    System.out.println("Nom Nom Nom. Yummy Messages.");
                    });
                }

            }, timeInSeconds, TimeUnit.SECONDS);

            runningExecutors.add(newSES);

            System.out.println("made new executor:" + newSES);

        }
    }

    boolean checkChannel(IChannel currentChannel, IChannel intendedChannel) {

        List<IChannel> intendedChannelList = new ArrayList();

        intendedChannelList.add(intendedChannel);

        return checkChannel(currentChannel, intendedChannelList);
    }

    boolean checkChannel(IChannel currentChannel, List<IChannel> intendedChannel) {

        if (intendedChannel.contains(currentChannel)) {
            return true;
        } else {
            return false;
        }
    }

    //Give feedback to user that bot has started working
    //this is a convenient function to allow quick, easy changes to the code that ALL commands use before getting into the command-specific code
    void start(IChannel currentChannel) {
        currentChannel.setTypingStatus(true);
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