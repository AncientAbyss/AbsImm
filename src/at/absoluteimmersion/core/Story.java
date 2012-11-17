package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.List;

public class Story extends BasePart {
    public static final String INITIAL_ACTION = "enter";
    private List<ReactionClient> clients = new ArrayList<ReactionClient>();

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

        if (interaction.equals("hint")) {
            hint();
            return;
        }

        String[] splitted = interaction.split(" ", 2);
        if (splitted.length < 2) throw new StoryException("Invalid command!");
        String target = splitted[1];
        String actionName = splitted[0];
        List<BasePart> allParts = findAll(target);
        List<Action> actions = new ArrayList<Action>();
        for (BasePart part : allParts) {
            actions.addAll(((Part) part).findActions(actionName));
        }
        for (ReactionClient client : clients) {
            if (allParts.isEmpty()) {
                client.reaction("No such object!");
                continue;
            }
            if (actions.isEmpty()) {
                client.reaction("You can not do this with this object!"); //TODO not hardcoded
                continue;
            }
            for (Action action : actions) {
                String result = action.execute();
                if (result.isEmpty()) continue;
                client.reaction(result);
            }
        }
    }

    private void hint() {
        List<BasePart> allParts = findAll("");
        for (ReactionClient client : clients) {
            for (BasePart part : allParts) {
                String hint_message = part.getName() + " (";
                List<String> send_actions = new ArrayList<String>();
                for (Action action : ((Part) part).findActions(""))
                {
                    if (send_actions.contains(action.getName())) continue;
                    if (send_actions.size() > 0) hint_message += ", ";
                    hint_message += action.getName();
                    send_actions.add(action.getName());
                }
                if (send_actions.size() > 0) client.reaction(hint_message + ")");
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
