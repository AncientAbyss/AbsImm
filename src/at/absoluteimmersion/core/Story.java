package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Story {
    private List<ReactionClient> clients = new ArrayList<>();
    private Map<String, Part> parts = new HashMap<>();
    private boolean isInitialized = false;

    public void addClient(ReactionClient client) {
        clients.add(client);
    }

    public void interact(String interaction) throws StoryException {
        if (!isInitialized) throw new StoryException("Cannot run empty tale!");
        String[] splitted = interaction.split(" ");
        Action action = parts.get(splitted[1]).getAction(splitted[0]);
        for (ReactionClient client : clients) {
            client.reaction(parts.get(splitted[1]).getName());
            client.reaction(action.execute());
        }
    }

    public void addPart(Part part) {
        isInitialized = true;
        parts.put(part.getName(), part);
    }

    public void tell() throws StoryException {
        String initial_action = parts.keySet().iterator().next();
        interact("enter " + initial_action);
    }
}
