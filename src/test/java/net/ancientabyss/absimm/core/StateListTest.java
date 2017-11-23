package net.ancientabyss.absimm.core;

import org.junit.Assert;
import org.junit.Test;

public class StateListTest {
    @Test
    public void isSatisfied_emptyList_returnsFalse() {
        StateList stateList = new StateList();
        Assert.assertFalse(stateList.isSatisfied(new State("x", false)));
    }

    @Test
    public void isSatisfied_emptyStateEmptyList_returnsFalse() {
        StateList stateList = new StateList();
        Assert.assertFalse(stateList.isSatisfied(new State("", false)));
    }

    @Test
    public void isSatisfied_listContainsState_returnsTrue() {
        StateList stateList = new StateList();
        State state = new State("x", false);
        stateList.set(state);
        Assert.assertTrue(stateList.isSatisfied(state));
    }

    @Test
    public void isSatisfied_listContainsNegatedState_returnsTrue() {
        StateList stateList = new StateList();
        State state = new State("x", true);
        stateList.set(state);
        Assert.assertTrue(stateList.isSatisfied(state));
    }

    @Test
    public void isSatisfied_listDoesNotContainNegatedState_returnsTrue() {
        StateList stateList = new StateList();
        State state = new State("x", true);
        Assert.assertTrue(stateList.isSatisfied(state));
    }

    @Test
    public void isSatisfied_listContainsDifferentNegation_returnsFalse() {
        StateList stateList = new StateList();
        State state = new State("x", true);
        stateList.set(state);
        Assert.assertFalse(stateList.isSatisfied(new State("x", false)));
    }

    @Test
    public void isSatisfied_listContainsOtherState_returnsFalse() {
        StateList stateList = new StateList();
        stateList.set(new State("x", false));
        Assert.assertFalse(stateList.isSatisfied(new State("y", false)));
    }

    @Test
    public void set_listDoesNotContainState_setState() {
        StateList stateList = new StateList();
        stateList.set(new State("x", false));
        Assert.assertFalse(stateList.toString().isEmpty());
    }

    @Test
    public void set_listContainsState_doesNotSetState() {
        StateList stateList = new StateList();
        stateList.set(new State("x", false));
        String expected = stateList.serialize();
        stateList.set(new State("x", false));
        Assert.assertEquals(expected, stateList.serialize());
    }

    @Test
    public void set_listContainsNegatedState_shouldOverwriteState() {
        StateList stateList = new StateList();
        stateList.set(new State("x", true));
        stateList.set(new State("x", false));
        Assert.assertEquals("x", stateList.serialize());
    }

    @Test
    public void setAll_listDoesNotContainStates_setsStates() {
        StateList stateList = new StateList();
        State state1 = new State("x", true);
        State state2 = new State("y", false);
        State state3 = new State("y", true);
        stateList.setAll(new State[]{state1, state2});
        Assert.assertTrue(stateList.isSatisfied(state1));
        Assert.assertTrue(stateList.isSatisfied(state2));
        Assert.assertFalse(stateList.isSatisfied(state3));
    }

    @Test
    public void setAll_duplicateStates_doesNotSetStates() {
        StateList stateList = new StateList();
        State state1 = new State("x", true);
        State state2 = new State("y", false);
        stateList.setAll(new State[]{state1, state2});
        String expected = stateList.serialize();
        stateList.setAll(new State[]{state1, state2});
        Assert.assertEquals(expected, stateList.serialize());
    }

    @Test
    public void setAll_differentNegation_updatesStates() {
        StateList stateList = new StateList();
        stateList.setAll(new State[]{new State("x", true), new State("y", false)});
        stateList.setAll(new State[]{new State("x", false), new State("y", true)});
        Assert.assertEquals("x AND NOT y", stateList.serialize());
    }

    @Test
    public void areAllSatisfied_allSet_returnsTrue() {
        StateList stateList = new StateList();
        State state1 = new State("x", true);
        State state2 = new State("y", false);
        stateList.setAll(new State[]{state1, state2});
        Assert.assertTrue(stateList.areAllSatisfied(new State[]{state1, state2}));
    }

    @Test
    public void areAllSatisfied_NoneSet_returnsTrue() {
        StateList stateList = new StateList();
        State state1 = new State("x", true);
        State state2 = new State("y", false);
        Assert.assertFalse(stateList.areAllSatisfied(new State[]{state1, state2}));
    }

    @Test
    public void areAllSatisfied_someSet_returnsFalse() {
        StateList stateList = new StateList();
        State state1 = new State("x", true);
        State state2 = new State("y", false);
        stateList.setAll(new State[]{state1});
        Assert.assertFalse(stateList.areAllSatisfied(new State[]{state1, state2}));
    }

    @Test
    public void areAllSatisfied_emptySet_returnsTrue() {
        StateList stateList = new StateList();
        Assert.assertTrue(stateList.areAllSatisfied(new State[0]));
    }

    @Test
    public void serialize_notNegatedState_returnsStateName() {
        StateList stateList = new StateList();
        State state1 = new State("x", false);
        stateList.set(state1);
        Assert.assertEquals("x", stateList.serialize());
    }

    @Test
    public void serialize_negatedState_returnsStateNameIncludingNegation() {
        StateList stateList = new StateList();
        State state1 = new State("x", true);
        stateList.set(state1);
        Assert.assertEquals("NOT x", stateList.serialize());
    }

    @Test
    public void serialize_multipleStates_returnsAllStates() {
        StateList stateList = new StateList();
        State state1 = new State("x", true);
        State state2 = new State("y", false);
        stateList.setAll(new State[]{state1, state2});
        Assert.assertEquals("NOT x AND y", stateList.serialize());
    }

    @Test
    public void deserialize_notNegatedState_setsState() {
        StateList stateList = new StateList();
        stateList.deserialize("x");
        Assert.assertEquals("x", stateList.serialize());
    }

    @Test
    public void deserialize_negatedState_setsNegatedState() {
        StateList stateList = new StateList();
        stateList.deserialize("NOT x");
        Assert.assertEquals("NOT x", stateList.serialize());
    }

    @Test
    public void deserialize_multipleStates_setsAllStates() {
        StateList stateList = new StateList();
        stateList.deserialize("x AND NOT y");
        Assert.assertEquals("x AND NOT y", stateList.serialize());
    }
}
