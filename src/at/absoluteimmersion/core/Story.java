package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.List;

public class Story extends BasePart {
    public static final String INITIAL_ACTION = "enter";
    private List<ReactionClient> clients = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Story story = (Story) o;

        if (clients != null ? !clients.equals(story.clients) : story.clients != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (clients != null ? clients.hashCode() : 0);
        return result;
    }

    public void addClient(ReactionClient client) {
        clients.add(client);
    }

    public void interact(String interaction) throws StoryException {
        if (!isInitialized()) throw new StoryException("Cannot run empty tale!");
        String[] splitted = interaction.split(" ");
        String target = splitted[1];
        String actionName = splitted[0];
        List<BasePart> allParts = findAll(target);
        List<Action> actions = new ArrayList<>();
        for (BasePart part : allParts) {
            actions.addAll(((Part) part).getActions(actionName));
        }
        for (ReactionClient client : clients) {
            for (BasePart part : parts) {
                if (allParts.contains(part)) {
                    client.reaction(part.getName());
                }
            }
            for (Action action : actions) {
                String result = action.execute();
                if (result.isEmpty()) continue;
                client.reaction(result);
            }
        }
    }

    public void tell() throws StoryException {
        if (!isInitialized()) throw new StoryException("Cannot run empty tale!");
        String initial_action = parts.get(0).getName();
        interact(INITIAL_ACTION + " " + initial_action);
    }

    public boolean isInitialized() {
        return parts.size() > 0;
    }
}
