package at.absoluteimmersion.core;

import junit.framework.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ActionTest {

    @Test
    public void execute_executes_returnsText() {
        Action action = createAction("", mock(StateList.class));
        Assert.assertEquals("jamm", action.execute());
    }

    @Test
    public void execute_executes_setsState() {
        Part part = new Part("part");
        StateList stateList = mock(StateList.class);
        Action action = createAction("", stateList);
        action.execute();
        verify(stateList).add("drunk");
    }

    @Test
    public void execute_executes_savesState() {
        Part part = new Part("part");
        StateList stateList = new StateList();
        Action action = createAction("", stateList);
        action.execute();
        Assert.assertTrue(stateList.contains("drunk"));
    }

    private Action createAction(String condition, StateList stateList) {
        return new Action("drink", "jamm", condition, "drunk", stateList);
    }
}
