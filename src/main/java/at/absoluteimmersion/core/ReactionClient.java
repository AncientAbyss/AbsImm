package at.absoluteimmersion.core;

import org.jivesoftware.smack.SmackException;

public interface ReactionClient {
    void reaction(String text) throws SmackException.NotConnectedException;
}
