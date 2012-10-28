package at.absoluteimmersion.core;

import java.util.HashMap;
import java.util.Map;

public class Part {
    private String text;
    private String name;
    private Map<String, Action> actions = new HashMap<>();

    public Part(String name, String text) {
        this.text = text;
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public void addAction(Action action) {
        actions.put(action.getName(), action);
    }

    public Action getAction(String name) throws StoryException {
        Action action = actions.get(name);
        if (action == null) throw new StoryException("Command not found!");
        return action;
    }
}
