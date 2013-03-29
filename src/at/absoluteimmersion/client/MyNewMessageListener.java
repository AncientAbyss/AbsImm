package at.absoluteimmersion.client;

import at.absoluteimmersion.core.ReactionClient;
import at.absoluteimmersion.core.Story;
import at.absoluteimmersion.core.StoryException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.logging.Logger;

public class MyNewMessageListener implements MessageListener, ReactionClient {

    private static final Logger LOG = Logger.getLogger(MyNewMessageListener.class.getCanonicalName());

    private Story story;
    private Chat chat;
    private boolean closed = false;
    private boolean firstMessage = true;

    public MyNewMessageListener(Chat chat, Story story) {
        this.chat = chat;
        this.story = story;
        story.addClient(this);
        try {
            story.tell();
        } catch (StoryException e) {
            LOG.severe("Failed telling story: " + e.getMessage());
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        if (firstMessage) {
            firstMessage = false;
            return;
        }

        LOG.info(chat.getParticipant());
        LOG.info(message.getBody());
        try {
            if (message.getBody() != null && !message.getBody().isEmpty()) {
                if (message.getBody().equals(story.getSettings().getSetting("quit_command"))) {
                    quit();
                    return;
                }
                if (message.getBody().equals("admin_quit")) {  // TODO: extract generic admin command interface
                    closed = true;
                    return;
                }
                story.interact(message.getBody());
            }
        } catch (StoryException e) {
            try {
                chat.sendMessage(e.getMessage());
            } catch (XMPPException e1) {
                LOG.severe(e.getMessage());
            }
            LOG.severe(e.getMessage());
        } catch (XMPPException e) {
            LOG.severe(e.getMessage());
        }
    }

    public void quit() throws XMPPException {
        // TODO: properly cleanup
        if (chat.getListeners().isEmpty()) return;

        chat.sendMessage("kthxbye");
        chat.removeMessageListener(this); // TODO: how to resume game in this chat session
        return;
    }

    @Override
    public void reaction(String text) {
        try {
            /*
            // TODO: the message is not delivered properly if sent as a whole in fb
            String blah = text.replaceAll("\\\\n", "\n");
            System.out.println(": " + blah);
            Thread.sleep(2 * 100);
            chat.sendMessage(blah);
            */
            for (String part : text.split("\\\\n")) {
                Thread.sleep(2 * 100);
                LOG.info(": " + part);
                chat.sendMessage(part);
            }
        } catch (XMPPException e) {
            LOG.severe(e.getMessage());
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }
    }

    public boolean isClosed() {
        return closed;
    }
}
