package at.absoluteimmersion.client;

import at.absoluteimmersion.core.Loader;
import at.absoluteimmersion.core.ReactionClient;
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
    private List<ReactionClient> reactionClients = new ArrayList<>();
    private Story story;

    public static void main(String[] args) throws XMPPException, IOException, InterruptedException {
        if (args.length < 5) {
            printUsage();
            return;
        }

        AbsoluteImmersionXmpp absoluteImmersionXmpp = new AbsoluteImmersionXmpp();
        absoluteImmersionXmpp.initStoryFromFile(args[0]);
        absoluteImmersionXmpp.init(args[1], args[2], args[3], Integer.parseInt(args[4]));
    }

    public void addReactionClient(ReactionClient client) {
        reactionClients.add(client);
    }

    private void init(String username, String password, String host, int port) throws XMPPException, IOException, InterruptedException {
        XMPPConnection connection = createXmppConnection(username, password, host, port);
        connection.getChatManager().addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        if (!createdLocally)
                            run(chat); // TODO: threads needed
                    }
                });

        LOG.info("absolute immersion listens... press any key to quit");
        while (!isClosed()) Thread.currentThread().sleep(1000);
        for (MyNewMessageListener listener : listeners) {
            listener.quit();
        }
        LOG.info("absolute immersion is down...");
    }

    private XMPPConnection createXmppConnection(String username, String password, String host, int port) throws XMPPException {
        SASLAuthentication.registerSASLMechanism("DIGEST-MD5", MySASLDigestMD5Mechanism.class);
        SASLAuthentication.supportSASLMechanism("DIGEST-MD5", 1);
        SASLAuthentication.supportSASLMechanism("PLAIN", 0);
        ConnectionConfiguration config = new ConnectionConfiguration(host, port);
        XMPPConnection connection = new XMPPConnection(config);
        //config.setSASLAuthenticationEnabled(true); // TODO: does not work
        connection.connect();
        connection.login(username, password);
        return connection;
    }

    private boolean isClosed() {
        for (MyNewMessageListener listener : listeners) {
            if (listener.isClosed()) return true;
        }
        return false;
    }

    public void run(Chat chat) {
        LOG.info("chat opened...");
        MyNewMessageListener listener = new MyNewMessageListener(chat, story);
        for (ReactionClient client: reactionClients)
        listener.addAdditionialClient(client);
        chat.addMessageListener(listener);
        listeners.add(listener);
    }

    public void initStoryFromFile(String storyFile) {
        try {
            story = new Loader().fromFile(storyFile);
        } catch (StoryException e) {
            LOG.severe("Failed loading story: " + e.getMessage());
        }
    }

    public void initStoryFromString(String storyContent) {
        try {
            story = new Loader().fromString(storyContent  );
        } catch (StoryException e) {
            LOG.severe("Failed loading story: " + e.getMessage());
        }
    }

    private static void printUsage() {
        LOG.info("usage: AbsoluteImmersion [storyfile] [user] [password] [host] [port]");
    }

}

