package at.absoluteimmersion.core;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ActionTest {

    @Test
    public void execute_executes_returnsText() throws StoryException {
        Action action = createAction("", mock(StateList.class));
        Assert.assertEquals("jamm", action.execute());
    }

    @Test
    public void execute_executes_setsState() throws StoryException {
        StateList stateList = mock(StateList.class);
        Action action = createAction("", stateList);
        action.execute();
        verify(stateList).add("drunk");
    }

    @Test
    public void execute_executes_savesState() throws StoryException {
        StateList stateList = new StateList();
        Action action = new Action("drink", "jamm", "", "", stateList, new Story(stateList, new Settings()));
        action.execute();
        Assert.assertFalse(stateList.contains(""));
    }

    @Test
    public void execute_executesEmptyState_doesNotSaveState() throws StoryException {
        StateList stateList = new StateList();
        Action action = createAction("", stateList);
        action.execute();
        Assert.assertTrue(stateList.contains("drunk"));
    }

    @Test
    public void execute_targetSet_executesOnTarget() throws StoryException {
        Story story = mock(Story.class);
        Action action = new Action("drink", "jamm", "", "drunk", new StateList(), story, "enter target01");
        action.execute();
        verify(story).interact("enter target01");
    }

    private Action createAction(String condition, StateList stateList) {
        return new Action("drink", "jamm", condition, "drunk", stateList, new Story(stateList, new Settings()));
    }
}
