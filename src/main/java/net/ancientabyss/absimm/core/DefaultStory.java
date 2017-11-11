package net.ancientabyss.absimm.core;

import net.ancientabyss.absimm.models.Statistics;
import net.ancientabyss.absimm.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultStory extends BasePart implements Story {
    private List<ReactionClient> clients = new ArrayList<>();
    private Settings settings;
    private StateList stateList;
    // actions in the automated mode will not be suggested by the hint system.
    private List<Action> automatedActions = new ArrayList<>();
    private boolean automatedMode = false;
    private Statistics statistics = new Statistics();

    public DefaultStory(StateList stateList, Settings settings) {
        super("", "", stateList);
        this.settings = settings;
        this.stateList = stateList;
    }

    @Override
    public void addClient(ReactionClient client) {
        clients.add(client);
    }

    @Override
    public void interact(String interaction) throws StoryException {
        if (!isInitialized()) throw new StoryException(settings.getSetting("empty_story_error"));

        if (handleSystemCommands(interaction)) return;

        Map<String, String> processed_interaction = extractActionAndTargetFromCommand(interaction);
        List<BasePart> allParts = processed_interaction.containsKey("target") ? findAll(processed_interaction.get("target")) : new ArrayList<BasePart>();
        List<Action> actions = findActions(processed_interaction.get("action"), allParts);

        if (allParts.isEmpty()) {
            statistics.increaseNumInvalidCommands();
            sendMessageToAllClients(settings.getRandom("object_error"));
        } else if (actions.isEmpty()) {
            statistics.increaseNumInvalidCommands();
            sendMessageToAllClients(settings.getRandom("action_error"));
        } else {
            int previousState = stateList.hashCode();
            for (Action action : actions) {
                String result = action.execute();
                if (result.isEmpty()) continue;
                sendMessageToAllClients(result);
                if (automatedMode && !automatedActions.contains(action)) {
                    automatedActions.add(action);
                }
            }
            if (previousState != stateList.hashCode()) {
                statistics.increaseNumValidCommands();
            } else {
                statistics.increaseNumOptionalCommands();
            }
        }

        if (!automatedMode && isFinished()) {
            finish();
        }
    }

    private List<Action> findActions(String action, List<BasePart> allParts) {
        List<Action> actions = new ArrayList<>();
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
        if (command.equals(settings.getSetting("help_command"))) {
            help();
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
            client.onReact(message);
        }
    }

    private void finish() {
        for (ReactionClient client : clients) {
            client.onFinish();
        }
    }

    private Map<String, String> extractActionAndTargetFromCommand(String interaction) throws StoryException {
        Map<String, String> processed_interaction = new HashMap<>();
        String[] splitted = new String[]{"", interaction}; // prepare array
        // for all possible actions...
        do {
            splitted = splitted[1].split(" ", 2); // check each word

            if (splitted.length < 2) {
                if (!isValidTarget(splitted[0])) {
                    processed_interaction.put("action", interaction); // fallback when no target has been found
                    processed_interaction.put("target", "");
                } else if (!processed_interaction.containsKey("action")) {
                    processed_interaction.put("action", splitted[0]);
                    processed_interaction.put("target", "");
                }
                return processed_interaction;
            }

            // append action word
            processed_interaction.put("action", processed_interaction.containsKey("action") ? processed_interaction.get("action") + " " + splitted[0] : splitted[0]);
        } while (isAction(splitted));
        // ... for the remaining target, do
        processed_interaction.put("target", splitted[1]);

        return processed_interaction;
    }

    private boolean isValidTarget(String target) {
        return !findAll(target).isEmpty();
    }

    private boolean isAction(String[] splitted) {
        return findAll(splitted[1]).size() == 0;
    }

    private void hint() {
        List<BasePart> allParts = findAll();
        StringBuilder message = new StringBuilder();
        for (BasePart part : allParts) {
            List<String> availableActions = getAvailableActions((Part) part);
            if (availableActions.size() > 0) {
                if (message.length() > 0) message.append('\n');
                String actions = StringUtils.join(availableActions, ", ");
                if (!part.getName().isEmpty()) {
                    message.append(String.format("%s (%s)", part.getName(), actions));
                } else {
                    message.append(String.format("%s", actions));
                }
            }
        }
        if (message.length() > 0) {
            message.insert(0, settings.getRandom("hint_intro_message") + "\n");
            sendMessageToAllClients(message.toString());
        }
        statistics.increaseNumUsedHints();
    }

    private List<String> getAvailableActions(Part part) {
        List<String> availableActions = new ArrayList<>();
        for (Action action : part.findActions("")) {
            if (availableActions.contains(action.getName())) continue;
            if (automatedActions.contains(action)) continue;
            availableActions.add(action.getName());
        }
        return availableActions;
    }

    private void help() {
        sendMessageToAllClients(settings.getSetting("help_message"));
    }

    @Override
    public void tell() throws StoryException {
        if (!isInitialized()) throw new StoryException(settings.getSetting("empty_story_error"));
        if (settings.getSetting("initial_command") == null)
            throw new StoryException(settings.getSetting("initial_command_missing"));
        interact(settings.getSetting("initial_command"));
        statistics = new Statistics(); // reset to not include startup commands
    }

    private boolean isInitialized() {
        return parts.size() > 0;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        return String.format("Story{\nclients=%s\nsettings=%s\nparts=%s}", clients, settings, parts);
    }

    @Override
    public String getState() {
        return stateList.serialize();
    }

    @Override
    public void setState(String state) {
        stateList.deserialize(state);
    }

    public void setAutomatedMode(boolean automatedMode) {
        this.automatedMode = automatedMode;
    }

    public boolean isAutomatedMode() {
        return automatedMode;
    }

    @Override
    public Statistics getStatistics() {
        return statistics;
    }

    private boolean isFinished() {
        for (BasePart part : findAll()) {
            if (getAvailableActions((Part) part).size() > 0) return false;
        }
        return true;
    }
}
