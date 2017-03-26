package net.ancientabyss.absimm.core;

import java.util.ArrayList;
import java.util.List;

public class BasePart {
    public static final String NOT = "NOT";
    public static final String AND = "AND";

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

    public String getName() {
        return name;
    }

    public boolean conditionsMet() {
        if (condition.isEmpty()) return true;

        String[] conditions = condition.split(" " + AND + " ");

        for (String single_condition : conditions) {
            if (!singleConditionMet(single_condition)) return false;
        }

        return true;
    }

    private boolean singleConditionMet(String single_condition) {
        String invertedCondition = conditionContainsNot(single_condition) ? single_condition.split(" ")[1] : NOT + " " + single_condition;
        int condition_index = stateList.lastIndexOf(single_condition);
        int inverted_condition_index = stateList.lastIndexOf(invertedCondition);

        if (conditionContainsNot(single_condition)) {
            if (condition_index == -1 && inverted_condition_index == -1) return true;
        } else {
            if (condition_index == -1) return false;
        }

        return (condition_index > inverted_condition_index);
    }

    private boolean conditionContainsNot(String single_condition) {
        return single_condition.startsWith(NOT + " ");
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
                "parts=" + parts +
                ", name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}