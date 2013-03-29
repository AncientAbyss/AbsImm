package at.absoluteimmersion.client;

import at.absoluteimmersion.core.Loader;
import at.absoluteimmersion.core.Story;
import at.absoluteimmersion.core.StoryException;
import org.jivesoftware.smack.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Deprecated
public class AbsoluteImmersionXmpp {

    private static final Logger LOG = Logger.getLogger(AbsoluteImmersionXmpp.class.getCanonicalName());

    private List<MyNewMessageListener> listeners = new ArrayList<>();

    public static void main(String[] args) throws XMPPException, IOException, InterruptedException {
        if (args.length < 5) {
            printUsage();
            return;
        }

        new AbsoluteImmersionXmpp().init(args[0], args[1], args[2], args[3], Integer.parseInt(args[4]));
    }

    private void init(final String storyFile, String username, String password, String host, int port) throws XMPPException, IOException, InterruptedException {
        SASLAuthentication.registerSASLMechanism("DIGEST-MD5", MySASLDigestMD5Mechanism.class);
        SASLAuthentication.supportSASLMechanism("DIGEST-MD5", 1);

        ConnectionConfiguration config = new ConnectionConfiguration(host, port);
        XMPPConnection connection = new XMPPConnection(config);
        //config.setSASLAuthenticationEnabled(true); // TODO: does not work
        connection.connect();
        connection.login(username, password);

        connection.getChatManager().addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        if (!createdLocally)
                            run(storyFile, chat); // TODO: threads needed
                    }
                });

        LOG.info("absolute immersion listens... press any key to quit");
        while (!isClosed()) Thread.currentThread().sleep(1000);
        for (MyNewMessageListener listener : listeners) {
            listener.quit();
        }
        LOG.info("absolute immersion is down...");
    }

    private boolean isClosed() {
        for (MyNewMessageListener listener : listeners) {
            if (listener.isClosed()) return true;
        }
        return false;
    }

    public void run(String storyFile, Chat chat) {
        LOG.info("chat opened...");
        Story story;
        try {
            story = new Loader().fromFile(storyFile);
        } catch (StoryException e) {
            LOG.severe("Failed loading story: " + e.getMessage());
            return;
        }
        MyNewMessageListener listener = new MyNewMessageListener(chat, story);
        chat.addMessageListener(listener);
        listeners.add(listener);
    }

    private static void printUsage() {
        LOG.info("usage: AbsoluteImmersion [storyfile] [user] [password] [host] [port]");
    }

}

