package net.ancientabyss.absimm.core;

import org.junit.Assert;
import org.junit.Test;

public class StateTest {
    @Test
    public void fromString_notNegatedState_shouldReturnState() {
        State[] states = State.fromString("x");
        Assert.assertArrayEquals(new State[] {new State("x", false)}, states);
    }

    @Test
    public void fromString_negatedState_shouldReturnState() {
        State[] states = State.fromString("NOT x");
        Assert.assertArrayEquals(new State[] {new State("x", true)}, states);
    }

    @Test
    public void fromString_multipleStates_shouldReturnStates() {
        State[] states = State.fromString("NOT x AND y");
        Assert.assertArrayEquals(new State[] {new State("x", true), new State("y", false)}, states);
    }

    @Test
    public void fromString_emptyString_shouldReturnEmptyArray() {
        State[] states = State.fromString("");
        Assert.assertArrayEquals(new State[0], states);
    }
}
