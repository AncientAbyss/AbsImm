package at.absoluteimmersion.core;

import java.util.HashMap;
import java.util.Map;

public class BasePart {
    protected Map<String, BasePart> parts = new HashMap<>();
    protected String name;
    protected String condition;

    public BasePart() {
        name = "";
    }

    public BasePart(String name) {
        this.name = name;
    }

    public BasePart(String name, String condition) {
        this.name = name;
        this.condition = condition;
    }

    public void addPart(BasePart part) {
        parts.put(part.getName(), part);
    }

    public BasePart find(String name) throws StoryException {
        BasePart child = parts.get(name);
        if (child != null) return child;
        for (BasePart part : parts.values()) {
            child = part.find(name);
            if (child != null) {
                return child;
            }
        }
        throw new StoryException(("Part not found!"));
    }

    public String getName() {
        return name;
    }
}
