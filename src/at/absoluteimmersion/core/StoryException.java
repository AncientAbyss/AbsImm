package at.absoluteimmersion.core;

public class StoryException extends Exception {
    public StoryException(String message) {
        super(message);
    }

    public StoryException(Exception e) {
        super(e);
    }
}
