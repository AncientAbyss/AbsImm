package at.absoluteimmersion.core;

public class Action extends BasePart {

    private String text;
    private StateList stateList;
    private String state;

    public Action(String name, String text, StateList stateList) {
        this(name, text, "", "", stateList);
    }

    public Action(String name, String text, String condition, String state, StateList stateList) {
        super(name, condition);
        this.text = text;
        this.state = state;
        this.stateList = stateList;
    }

    public boolean conditionMet() {
        if (condition.isEmpty()) return true;
        boolean containsNot = condition.startsWith("not ");
        if (!containsNot) return stateList.contains(condition);
        return (!stateList.contains(condition.split(" ")[1]));
    }

    public String execute() {
        if (!conditionMet()) return "";
        stateList.add(state);
        return text;
    }
}
