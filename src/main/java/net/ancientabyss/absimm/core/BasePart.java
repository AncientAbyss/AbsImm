package net.ancientabyss.absimm.core;

import java.util.ArrayList;
import java.util.List;

public class BasePart {
    protected List<BasePart> parts = new ArrayList<>();
    protected String name = "";
    protected String condition = "";
    protected StateList stateList = new StateList();

    public BasePart() {
    }

    public BasePart(String name) {
        this.name = name;
    }

    public BasePart(String name, String condition, StateList stateList) {
        this.name = name;
        this.condition = condition;
        this.stateList = stateList;
    }

    public void addPart(BasePart part) {
        parts.add(part);
    }

    public String getName() {
        return name;
    }

    public boolean conditionsMet() {
        return stateList.areAllSatisfied(State.fromString(this.condition));
    }

    public List<BasePart> findAll(String somePart) {
        return findAll(somePart, false);
    }

    private List<BasePart> findAll(String somePart, boolean ignoreArgument) {
        List<BasePart> result = new ArrayList<>();
        for (BasePart part : parts) {
            if (!part.conditionsMet()) continue;
            if (part.getName().equalsIgnoreCase(somePart) || ignoreArgument) result.add(part);
            result.addAll(part.findAll(somePart, ignoreArgument));
        }
        return result;
    }

    public List<BasePart> findAll() {
        return findAll("", true);
    }

    @Override
    public String toString() {
        return "BasePart{" +
                "name='" + name + '\'' +
                ", parts=" + parts +
                ", condition='" + condition + '\'' +
                '}';
    }
}