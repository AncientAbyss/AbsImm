package at.absoluteimmersion.core;

public class Action extends BasePart {

    private String text;
    private String state;

    public Action(String name, String text, StateList stateList) {
        this(name, text, "", "", stateList);
    }

    public Action(String name, String text, String condition, String state, StateList stateList) {
        super(name, condition, stateList);
        this.text = text;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Action action = (Action) o;

        if (state != null ? !state.equals(action.state) : action.state != null) return false;
        if (text != null ? !text.equals(action.text) : action.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    public String execute() {
        if (!conditionMet()) return "";
        stateList.add(state);
        return text;
    }
}
