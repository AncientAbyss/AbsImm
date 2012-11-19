package at.absoluteimmersion.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Story extends BasePart {
    private List<ReactionClient> clients = new ArrayList<ReactionClient>();
    private Settings settings;

    public Story(StateList stateList, Settings settings) {
        super("", "", stateList);
        this.settings = settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Story story = (Story) o;

        if (clients != null ? !clients.equals(story.clients) : story.clients != null) return false;
        if (settings != null ? !settings.equals(story.settings) : story.settings != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (clients != null ? clients.hashCode() : 0);
        result = 31 * result + (settings != null ? settings.hashCode() : 0);
        return result;
    }

    public void addClient(ReactionClient client) {
        clients.add(client);
    }

    public void interact(String interaction) throws StoryException {
        if (!isInitialized()) throw new StoryException(settings.getSetting("empty_story_error"));

        if (handleSystemCommands(interaction)) return;

        Map<String, String> processed_interaction = new HashMap<String, String>();
        processInteraction(interaction, processed_interaction);
        List<BasePart> allParts = processed_interaction.containsKey("target") ? findAll(processed_interaction.get("target")) : new ArrayList<BasePart>();
        List<Action> actions = findActions(processed_interaction.get("action"), allParts);

        if (allParts.isEmpty()) sendMessageToAllClients(settings.getSetting("object_error"));
        else if (actions.isEmpty())
            sendMessageToAllClients(settings.getSetting("action_error"));
        else {
            for (Action action : actions) {
                String result = action.execute();
                if (result.isEmpty()) continue;
                sendMessageToAllClients(result);
            }
        }

    }

    private List<Action> findActions(String action, List<BasePart> allParts) {
        List<Action> actions = new ArrayList<Action>();
        for (BasePart part : allParts) {
            actions.addAll(((Part) part).findActions(action));
        }
        return actions;
    }

    private boolean handleSystemCommands(String interaction) {
        String command = interaction.split(" ")[0];
        if (command.equals(settings.getSetting("hint_command"))) {
            hint();
            return true;
        }

        if (command.equals(settings.getSetting("save_command"))) {
            if (!interaction.contains(" ")) {
                sendMessageToAllClients(settings.getSetting("save_invalid_command"));
                return true;
            }
            try {
                stateList.save(interaction.split(" ", 2)[1]);
                sendMessageToAllClients(settings.getSetting("save_success"));
            } catch (IOException e) {
                sendMessageToAllClients(settings.getSetting("save_error"));
            }
            return true;
        }

        if (command.equals(settings.getSetting("load_command"))) {
            if (!interaction.contains(" ")) {
                sendMessageToAllClients(settings.getSetting("load_invalid_command"));
                return true;
            }
            try {
                stateList.load(interaction.split(" ", 2)[1]);
                sendMessageToAllClients(settings.getSetting("load_success"));
            } catch (IOException e) {
                sendMessageToAllClients(settings.getSetting("load_error"));
            }

            return true;
        }
        return false;
    }

    private void sendMessageToAllClients(String message) {
        for (ReactionClient client : clients) {
            client.reaction(message);
        }
    }

    private void processInteraction(String interaction, Map<String, String> processed_interaction) throws StoryException {
        String[] splitted = new String[]{"", interaction};
        List<BasePart> allParts = new ArrayList<BasePart>();
        do {
            splitted = splitted[1].split(" ", 2);

            if (splitted.length < 2) {
                if (!processed_interaction.containsKey("action")) throw new StoryException(settings.getSetting("invalid_command"));
                else return;
            }

            processed_interaction.put("action", processed_interaction.containsKey("action") ? processed_interaction.get("action") + " " + splitted[0] : splitted[0]);
            allParts = findAll(splitted[1]);
        } while (allParts.size() == 0);

        processed_interaction.put("target", splitted[1]);
    }

    private void hint() {
        List<BasePart> allParts = findAll("");
        for (BasePart part : allParts) {
            String hint_message = part.getName() + " (";
            List<String> send_actions = new ArrayList<String>();
            for (Action action : ((Part) part).findActions("")) {
                if (send_actions.contains(action.getName())) continue;
                if (send_actions.size() > 0) hint_message += ", ";
                hint_message += action.getName();
                send_actions.add(action.getName());
            }
            if (send_actions.size() > 0) sendMessageToAllClients(hint_message + ")");
        }
    }

    public void tell() throws StoryException {
        if (!isInitialized()) throw new StoryException(settings.getSetting("empty_story_error"));
        String initial_action = parts.get(0).getName();
        if (settings.getSetting("initial_command") == null) throw new StoryException(settings.getSetting("initial_command_missing"));
        interact(settings.getSetting("initial_command"));
    }

    public boolean isInitialized() {
        return parts.size() > 0;
    }

    public Settings getSettings() {
        return settings;
    }
}
