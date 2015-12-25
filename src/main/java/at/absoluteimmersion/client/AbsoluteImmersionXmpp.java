package at.absoluteimmersion.client;

import at.absoluteimmersion.core.Loader;
import at.absoluteimmersion.core.ReactionClient;
import at.absoluteimmersion.core.Story;
import at.absoluteimmersion.core.StoryException;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;

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
    private ChatManager chatManager;

    public static void main(String[] args) throws XMPPException, IOException, InterruptedException, SmackException {
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

    private void init(String username, String password, String host, int port) throws XMPPException, IOException, InterruptedException, SmackException {
        XMPPConnection connection = createXmppConnection(username, password, host, port);

        chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        LOG.info("chat created...");
                        if (!createdLocally) {
                            run(chat); // TODO: threads needed
                        }
                    }
                });
        PingManager pinger = PingManager.getInstanceFor(connection);

        LOG.info("absolute immersion listens... press any key to quit");
        pinger.setPingInterval(30);
        while (!isClosed()) {
            Thread.currentThread().sleep(1000);
        }

        for (MyNewMessageListener listener : listeners) {
            listener.quit();
        }

        pinger.setPingInterval(0);
        LOG.info("absolute immersion is down...");
    }

    private XMPPConnection createXmppConnection(String username, String password, String host, int port) throws XMPPException, IOException, SmackException, InterruptedException {
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username, password)
                .setServiceName(host)
                .setHost(host)
                .setPort(port);
        config.setDebuggerEnabled(true);
        AbstractXMPPConnection connection = new XMPPTCPConnection(config.build());
        connection.connect();
        connection.login();
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
        for (ReactionClient client: reactionClients) {
            listener.addAdditionialClient(client);
        }
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

