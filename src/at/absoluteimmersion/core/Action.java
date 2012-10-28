package at.absoluteimmersion.core;

import java.util.HashMap;
import java.util.Map;

public class Action {
    private String name;
    private Map<String, Part> parts = new HashMap<>();

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPart(Part part) {
        parts.put(part.getName(), part);
    }

    public String execute() {
        return parts.values().iterator().next().getText();
    }
}
