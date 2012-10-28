package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.List;

public class BasePart {
    protected List<BasePart> parts = new ArrayList<>();
    protected String name = "";
    protected String condition = "";
    protected StateList stateList;

    public BasePart() {
    }

    public BasePart(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasePart basePart = (BasePart) o;

        if (condition != null ? !condition.equals(basePart.condition) : basePart.condition != null) return false;
        if (name != null ? !name.equals(basePart.name) : basePart.name != null) return false;
        if (parts != null ? !parts.equals(basePart.parts) : basePart.parts != null) return false;
        if (stateList != null ? !stateList.equals(basePart.stateList) : basePart.stateList != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parts != null ? parts.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (stateList != null ? stateList.hashCode() : 0);
        return result;
    }

    public BasePart(String name, String condition, StateList stateList) {
        this.name = name;
        this.condition = condition;
        this.stateList = stateList;
    }

    public void addPart(BasePart part) {
        parts.add(part);
    }

    public BasePart find(String name) throws StoryException {
        for (BasePart part : parts) {
            if (part.getName().equals(name)) return part;
            BasePart child = part.find(name);
            if (child != null) {
                return child;
            }
        }
        throw new StoryException(("Part not found!"));
    }

    public String getName() {
        return name;
    }

    public boolean conditionMet() {
        if (condition.isEmpty()) return true;
        boolean containsNot = condition.startsWith("not ");
        if (!containsNot) return stateList.contains(condition);
        return (!stateList.contains(condition.split(" ")[1]));
    }

    public List<BasePart> findAll(String somePart) {
        List<BasePart> result = new ArrayList<BasePart>();
        for (BasePart part : parts) {
            if (!part.conditionMet()) continue;
            if (part.getName().equals(somePart)) result.add(part);
            result.addAll(part.findAll(somePart));
        }
        return result;
    }
}
