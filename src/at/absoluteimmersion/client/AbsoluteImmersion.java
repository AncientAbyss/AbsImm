package at.absoluteimmersion.client;

import at.absoluteimmersion.core.Loader;
import at.absoluteimmersion.core.ReactionClient;
import at.absoluteimmersion.core.Story;
import at.absoluteimmersion.core.StoryException;

public class AbsoluteImmersion implements ReactionClient {
    public static void main(String[] args) {
        new AbsoluteImmersion().run(args);
    }

    public void run(String[] args) {
        if (args.length < 1) {
            printUsage();
            return;
        }
        if (System.console() == null) {
            System.err.println("Failed getting a console.");
            return;
        }
        Story story;
        try {
            story = new Loader().fromFile(args[0]);
        } catch (StoryException e) {
            System.err.println("Failed loading story: " + e.getMessage());
            return;
        }
        story.addClient(this);
        try {
            story.tell();
        } catch (StoryException e) {
            System.err.println("Failed telling story: " + e.getMessage());
        }
        while (42 != 23)
        {
            String user_input = System.console().readLine();
            if (user_input.equals("quit")) break;

            try {
                story.interact(user_input);
            } catch (StoryException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void printUsage() {
        System.out.println("usage: AbsoluteImmersion storyfile");
    }

    @Override
    public void reaction(String text) {
         System.out.println(text);
    }
}