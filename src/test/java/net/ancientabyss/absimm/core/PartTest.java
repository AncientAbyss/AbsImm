package net.ancientabyss.absimm.core;

import org.junit.Assert;
import org.junit.Test;

public class PartTest {
    @Test
    public void getActions_noAction_returnsEmptyList() throws StoryException {
        Part part = new Part("");
        Assert.assertEquals(0, part.findActions("blah").size());
    }

    @Test
    public void getActions_multipleActions_returnsWantedAction() throws StoryException {
        Part part = new Part("");
        StateList stateList = new StateList();
        Action a01 = new Action("01", "", new StateList(), new DefaultStory(stateList, new Settings()));
        Action a02 = new Action("02", "", new StateList(), new DefaultStory(stateList, new Settings()));
        part.addAction(a01);
        part.addAction(a02);
        for (Action action : part.findActions("01")) {
            Assert.assertEquals(a01, action);
        }
    }
}
