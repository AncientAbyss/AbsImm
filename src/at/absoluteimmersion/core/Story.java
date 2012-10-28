package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.List;

public class Story extends BasePart {
    public static final String INITIAL_ACTION = "enter";
    private List<ReactionClient> clients = new ArrayList<>();

    public void addClient(ReactionClient client) {
        clients.add(client);
    }

    public void interact(String interaction) throws StoryException {
        if (!isInitialized()) throw new StoryException("Cannot run empty tale!");
        String[] splitted = interaction.split(" ");
        String target = splitted[1];
        String actionName = splitted[0];
        Part part = (Part) parts.get(target);
        boolean sendPartName = true;
        if (part == null)
        {
            part = (Part) find(target);
            sendPartName = false;
        }
        List<Action> actions = part.getActions(actionName);
        for (ReactionClient client : clients) {
            if (sendPartName)
            {
                client.reaction(parts.get(target).getName());
            }
            for (Action action:actions)
            {
                String result = action.execute();
                if (result.isEmpty()) continue;
                client.reaction(result);
            }
        }
    }

    public void tell() throws StoryException {
        if (!isInitialized()) throw new StoryException("Cannot run empty tale!");
        String initial_action = parts.keySet().iterator().next();
        interact(INITIAL_ACTION + " " + initial_action);
    }

    public boolean isInitialized() {
        return parts.size() > 0;
    }
}
