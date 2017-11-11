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

    protected boolean singleConditionMet(String singleCondition) {
        String invertedCondition = conditionContainsNot(singleCondition) ? singleCondition.split(" ")[1] : NOT + " " + singleCondition;
        int conditionIndex = stateList.lastIndexOf(singleCondition);
        int invertedConditionIndex = stateList.lastIndexOf(invertedCondition);

        if (conditionContainsNot(singleCondition)) {
            if (conditionIndex == -1 && invertedConditionIndex == -1) return true;
        } else {
            if (conditionIndex == -1) return false;
        }

        return (conditionIndex > invertedConditionIndex);
    }

    private boolean conditionContainsNot(String singleCondition) {
        return singleCondition.startsWith(NOT + " ");
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