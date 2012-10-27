package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Story {
    private List<ReactionClient> clients = new ArrayList<>();
    private Map<String, StorySection> sections = new HashMap<>();
    private boolean isInitialized = false;

    public void addClient(ReactionClient client) {
        clients.add(client);
    }

    public void interact(String interaction) throws StoryException {
        if (!isInitialized) throw new StoryException();
        for (ReactionClient client : clients) {
            client.reaction(sections.get(interaction).getText());
        }
    }

    public void addSection(String interaction, StorySection storySection) {
        isInitialized = true;
        sections.put(interaction, storySection);
    }
}
