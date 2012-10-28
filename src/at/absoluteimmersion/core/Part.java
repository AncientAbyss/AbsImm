package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.List;

public class Part extends BasePart {
    private List<Action> actions = new ArrayList<>();

    public Part(String name) {
        super(name);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public List<Action> getActions(String name) throws StoryException {
        List<Action> tmpActions = new ArrayList<>();
        for (Action action:actions)
        {
            if(action.getName().equals(name))
            {
                tmpActions.add(action);
            }
        }
        if (tmpActions.isEmpty()) throw new StoryException("Command not found!");
        return tmpActions;
    }
}
