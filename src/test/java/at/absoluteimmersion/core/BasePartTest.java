package at.absoluteimmersion.core;

import org.junit.Assert;
import org.junit.Test;

public class BasePartTest {

    @Test()
    public void findAll_partsAdded_findParts() throws StoryException {
        BasePart part = new BasePart();
        part.addPart(new Part("somePart"));
        part.addPart(new Part("somePart"));
        Assert.assertEquals(2, part.findAll("somePart").size());
    }

    @Test()
    public void findAll_partsAdded_findAllParts() throws StoryException {
        BasePart part = new BasePart();
        part.addPart(new Part("somePart"));
        part.addPart(new Part("somePart"));
        part.addPart(new Part(""));
        part.addPart(new Part("x"));
        Assert.assertEquals(4, part.findAll().size());
    }

    @Test
    public void conditionMet_noCondition_isMet() {
        Action action = createAction("", new StateList());
        Assert.assertTrue(action.conditionsMet());
    }

    @Test
    public void conditionMet_withCondition_isNotMet() {
        Action action = createAction("some_condition", new StateList());
        Assert.assertFalse(action.conditionsMet());
    }

    @Test
    public void conditionMet_withCondition_isMet() {
        StateList stateList = new StateList();
        Action action = createAction("some_condition", stateList);
        stateList.add("some_condition");
        Assert.assertTrue(action.conditionsMet());
    }

    @Test
    public void conditionMet_withNotCondition_isMet() {
        StateList stateList = new StateList();
        stateList.add("some_condition");
        Action action = createAction("NOT some_condition", stateList);
        Assert.assertFalse(action.conditionsMet());
    }

    @Test
    public void conditionMet_withNoNotCondition_isMet() {
        StateList stateList = new StateList();
        Action action = createAction("NOT some_condition", stateList);
        Assert.assertTrue(action.conditionsMet());
    }

    @Test
    public void conditionMet_withExistingStateAndNotState_isNotMet() {
        StateList stateList = new StateList();
        stateList.add("some_condition");
        stateList.add("NOT some_condition");
        Action action = createAction("some_condition", stateList);
        Assert.assertFalse(action.conditionsMet());
    }

    private Action createAction(String condition, StateList stateList) {
        return new Action("drink", "jamm", condition, "drunk", stateList, new Story(stateList, new Settings()));
    }

    @Test
    public void conditionMet_withANDCondition_isMet() {
        StateList stateList = new StateList();
        stateList.add("some_condition");
        stateList.add("some_other_condition");
        Action action = createAction("some_condition AND some_other_condition", stateList);
        Assert.assertTrue(action.conditionsMet());
    }

    @Test
    public void conditionMet_withANDCondition_isNotMet() {
        StateList stateList = new StateList();
        stateList.add("some_condition");
        Action action = createAction("some_condition AND some_other_condition", stateList);
        Assert.assertFalse(action.conditionsMet());
    }

    @Test
    public void conditionMet_withANDConditionAndNotCondition_isNotMet() {
        StateList stateList = new StateList();
        stateList.add("some_condition");
        stateList.add("some_other_condition");
        Action action = createAction("some_condition AND NOT some_other_condition", stateList);
        Assert.assertFalse(action.conditionsMet());
    }

    @Test
    public void conditionMet_withANDConditionAndNotCondition_isMet() {
        StateList stateList = new StateList();
        stateList.add("some_condition");
        Action action = createAction("some_condition AND NOT some_other_condition", stateList);
        Assert.assertTrue(action.conditionsMet());
    }
}
