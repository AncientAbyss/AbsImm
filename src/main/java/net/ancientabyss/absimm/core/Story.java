package net.ancientabyss.absimm.core;

/**
 * This is the main component for handling stories.
 * Get started: create stories using a Loader, add a client and run tell.
 */
public interface Story {
    /** Register a client, where messages will be sent to. */
    void addClient(ReactionClient client);
    /** Run the story. */
    void tell() throws StoryException;
    /** Execute a command. */
    void interact(String interaction) throws StoryException;

    /** Get the serialized state, which can be restored using setState. */
    String getState();
    /** Restore a state which has been saved via getState. */
    void setState(String state);

    /** Do not expose commands to the hint system if automatedMode is set. */
    void setAutomatedMode(boolean automatedMode);
    /** Check whether the automated mode is set. */
    boolean isAutomatedMode();

    @Override
    String toString();
}
