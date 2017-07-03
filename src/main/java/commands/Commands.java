package commands;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.MessageHistory;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by Adrian on 6/30/17.
 * these methods apply to all commands
 */
class Commands {

    private void clearAllButMessageIDAfterTime( Long id, IChannel channel, Integer timeInSeconds) {

        //ID of #bot-spam intro message 330540361808084992

        //TODO: make this into a discord message that says something like "this message will self destruct"
        //System.out.println("WAITING SPECIFIED SECONDS...");


        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                MessageHistory history = RequestBuffer.request(() -> (MessageHistory) channel.getFullMessageHistory()).get();

                List<IMessage> messages = new ArrayList<>(history);

                // USE THIS TO GET THE ID OF THE FIRST MESSAGE IN THE CHANNEL IF YOU NEED TO CHANGE THE ID
                // System.out.println(messages.get(messages.size()-1).getLongID());

                messages.removeIf(m -> m.getLongID() == id);

                if (!messages.isEmpty()) {
                    RequestBuffer.request(() -> channel.bulkDelete(messages));
                }
        }, timeInSeconds, TimeUnit.SECONDS);


    }

    void end(IChannel channel) {
        end(channel, 60);
    }

    //Give feedback to user that bot is done working
    void end(IChannel channel, Integer cleanupInterval) {
        channel.setTypingStatus(false);

        if (cleanupInterval > 0 && channel == channel.getGuild().getChannelByID(326480795298693131L)) {
            clearAllButMessageIDAfterTime(330540361808084992L, channel, cleanupInterval);
        }

    }

    //Give feedback to user that bot has started working
    Boolean canStart(IChannel channel) {
        //if channel is not #bot-spam
        if (channel != channel.getGuild().getChannelByID(326480795298693131L)/*getChannelsByName("bot-spam")*/) {
            return false;
        } else {
            channel.setTypingStatus(true);
            return true;
        }
    }

}