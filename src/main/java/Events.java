import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.Status;

/**
 * Sets bots playing text to
 */
public class Events {
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        event.getClient().changeStatus(Status.game("with code."));  // Fun message to show bot is online and working.
    }
}
