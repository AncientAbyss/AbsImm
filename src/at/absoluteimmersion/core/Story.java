package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String, String> processed_interaction = new HashMap<String, String>();
        processInteraction(interaction, processed_interaction);
        List<BasePart> allParts = processed_interaction.containsKey("target") ? findAll(processed_interaction.get("target")) : new ArrayList<BasePart>();
        List<Action> actions = new ArrayList<Action>();
        for (BasePart part : allParts) {
            actions.addAll(((Part) part).findActions(processed_interaction.get("action")));
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

    private void processInteraction(String interaction, Map<String, String> processed_interaction) throws StoryException {
        String[] splitted = new String[]{"", interaction};
        List<BasePart> allParts = new ArrayList<BasePart>();
        do {
            splitted = splitted[1].split(" ", 2);

            if (splitted.length < 2)
            {
                if (!processed_interaction.containsKey("action")) throw new StoryException("Invalid command!");
                else return;
            }

            processed_interaction.put("action", processed_interaction.containsKey("action") ? processed_interaction.get("action") + " " + splitted[0] : splitted[0]);
            allParts = findAll(splitted[1]);
        } while (allParts.size() == 0);

        processed_interaction.put("target", splitted[1]);
    }

    private void hint() {
        List<BasePart> allParts = findAll("");
        for (ReactionClient client : clients) {
            for (BasePart part : allParts) {
                String hint_message = part.getName() + " (";
                List<String> send_actions = new ArrayList<String>();
                for (Action action : ((Part) part).findActions("")) {
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
