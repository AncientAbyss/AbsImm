package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.List;

public class Part extends BasePart {
    private List<Action> actions = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Part part = (Part) o;

        if (actions != null ? !actions.equals(part.actions) : part.actions != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (actions != null ? actions.hashCode() : 0);
        return result;
    }

    public Part(String name) {
        super(name);
    }

    public Part(String name, String condition, StateList stateList) {
        super(name, condition, stateList);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public List<Action> getActions(String name) throws StoryException {
        List<Action> tmpActions = new ArrayList<>();
        for (Action action : actions) {
            if (action.getName().equals(name) && action.conditionMet()) {
                tmpActions.add(action);
            }
        }
        return tmpActions;
    }
}
