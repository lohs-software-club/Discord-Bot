import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;

/**
 * Sets bots playing text to
 */
public class Events {
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        event.getClient().changePlayingText("with code.");
    }
}
