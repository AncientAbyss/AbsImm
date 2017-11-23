package net.ancientabyss.absimm.core;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActionTest {

    @Test
    public void execute_executes_returnsText() throws StoryException {
        StateList stateList = createStateListMock();
        Action action = createAction("", stateList);
        Assert.assertEquals("jamm", action.execute());
    }

    @Test
    public void execute_executes_setsState() throws StoryException {
        StateList stateList = createStateListMock();
        Action action = createAction("", stateList);
        action.execute();
        verify(stateList).setAll(new State[] {new State("drunk", false)});
    }

    @Test
    public void execute_executesEmptyState_doesNotSaveState() throws StoryException {
        StateList stateList = new StateList();
        Action action = new Action("drink", "jamm", "", "", stateList, new DefaultStory(stateList, new Settings()));
        action.execute();
        Assert.assertTrue(stateList.serialize().isEmpty());
    }

    @Test
    public void execute_executesWithState_saveState() throws StoryException {
        StateList stateList = new StateList();
        Action action = createAction("", stateList);
        action.execute();
        Assert.assertTrue(stateList.isSatisfied(new State("drunk", false)));
    }

    @Test
    public void execute_targetSet_executesOnTarget() throws StoryException {
        Story story = mock(DefaultStory.class);
        Action action = new Action("drink", "jamm", "", "drunk", new StateList(), story, "enter target01");
        action.execute();
        verify(story).interact("enter target01");
    }

    private Action createAction(String condition, StateList stateList) {
        return new Action("drink", "jamm", condition, "drunk", stateList, new DefaultStory(stateList, new Settings()));
    }

    private StateList createStateListMock() {
        StateList stateList = mock(StateList.class);
        when(stateList.areAllSatisfied(anyObject())).thenReturn(true);
        return stateList;
    }
}
