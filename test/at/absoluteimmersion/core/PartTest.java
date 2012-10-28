package at.absoluteimmersion.core;

import junit.framework.Assert;
import org.junit.Test;

public class PartTest {
    @Test(expected = StoryException.class)
    public void getAction_noAction_throwsException() throws StoryException {
        Part part = new Part("");
        part.getActions("blah");
    }

    @Test()
    public void getAction_multipleActions_returnsWantedAction() throws StoryException {
        Part part = new Part("");
        Action a01 = new Action("01", "", new StateList());
        Action a02 = new Action("02", "", new StateList());
        part.addAction(a01);
        part.addAction(a02);
        for (Action action : part.getActions("01")) {
            Assert.assertEquals(a01, action);
        }
    }
}
