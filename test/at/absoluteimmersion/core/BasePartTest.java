package at.absoluteimmersion.core;

import junit.framework.Assert;
import org.junit.Test;

public class BasePartTest {
    @Test(expected = StoryException.class)
    public void find_noPartsAdded_throwsException() throws StoryException {
        BasePart part = new BasePart();
        part.find("somePart");
    }

    @Test()
    public void find_partsAdded_findsPart() throws StoryException {
        BasePart part = new BasePart();
        Part somePart = new Part("somePart");
        part.addPart(somePart);
        Assert.assertEquals(somePart, part.find("somePart"));
    }

    @Test()
    public void findAll_partsAdded_findParts() throws StoryException {
        BasePart part = new BasePart();
        part.addPart(new Part("somePart"));
        part.addPart(new Part("somePart"));
        Assert.assertEquals(2, part.findAll("somePart").size());
    }


    @Test
    public void conditionMet_noCondition_isMet() {
        Action action = createAction("", new StateList());
        Assert.assertTrue(action.conditionMet());
    }

    @Test
    public void conditionMet_withCondition_isNotMet() {
        Action action = createAction("some_condition", new StateList());
        Assert.assertFalse(action.conditionMet());
    }

    @Test
    public void conditionMet_withNotCondition_isMet() {
        StateList stateList = new StateList();
        stateList.add("some_condition");
        Action action = createAction("not some_condition", stateList);
        Assert.assertFalse(action.conditionMet());
    }

    private Action createAction(String condition, StateList stateList) {
        return new Action("drink", "jamm", condition, "drunk", stateList);
    }
}
