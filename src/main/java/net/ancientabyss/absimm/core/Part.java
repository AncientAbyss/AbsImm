package net.ancientabyss.absimm.core;

import java.util.ArrayList;
import java.util.List;

public class Part extends BasePart {
    private List<Action> actions = new ArrayList<>();

    public Part(String name) {
        super(name);
    }

    public Part(String name, String condition, StateList stateList) {
        super(name, condition, stateList);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public List<Action> findActions(String name) {
        List<Action> tmpActions = new ArrayList<>();
        for (Action action : actions) {
            if ((action.getName().equalsIgnoreCase(name) || name.equals("")) && action.conditionsMet()) {
                tmpActions.add(action);
            }
        }
        return tmpActions;
    }

    @Override
    public String toString() {
        return super.toString() + " >> Part{" +
                "actions=" + actions +
                ", name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                ", stateList=" + stateList +
                ", parts=" + parts +
                '}';
    }
}
