package at.absoluteimmersion.client;

import at.absoluteimmersion.core.Loader;
import at.absoluteimmersion.core.Story;
import at.absoluteimmersion.core.StoryException;
import org.jivesoftware.smack.*;

import java.io.IOException;

@Deprecated
public class AbsoluteImmersionXmpp {

    public static void main(String[] args) throws XMPPException, IOException {
        if (args.length < 5) {
            printUsage();
            return;
        }
        if (System.console() == null) {
            System.err.println("Failed getting a console.");
            return;
        }
        init(args[0], args[1], args[2], args[3], Integer.parseInt(args[4]));
    }

    private static void init(final String storyFile, String username, String password, String host, int port) throws XMPPException, IOException {
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
                            new AbsoluteImmersionXmpp().run(storyFile, chat); // TODO: threads needed
                    }
                });

        System.out.println("absolute immersion listens... press any key to quit");
        System.in.read();
    }

    public void run(String storyFile, Chat chat) {
        System.out.println("chat opened...");
        Story story;
        try {
            story = new Loader().fromFile(storyFile);
        } catch (StoryException e) {
            System.err.println("Failed loading story: " + e.getMessage());
            return;
        }
        chat.addMessageListener(new MyNewMessageListener(chat, story));
    }

    private static void printUsage() {
        System.out.println("usage: AbsoluteImmersion [storyfile] [user] [password] [host] [port]");
    }
}

