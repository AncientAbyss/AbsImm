package net.ancientabyss.absimm.core;

import java8.util.J8Arrays;

public class State {
    private String name;
    private boolean isNegated;

    public State(String name, boolean isNegated) {
        this.name = name;
        this.isNegated = isNegated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNegated() {
        return isNegated;
    }

    public void setNegated(boolean negated) {
        isNegated = negated;
    }

    public static State[] fromString(String states) {
        if (states.isEmpty()) return new State[0];
        String[] allStates = states.split(" " + StateList.AND + " ");
        return J8Arrays.stream(allStates).map(x -> {
            boolean isNegated = x.startsWith(StateList.NOT);
            return new State(isNegated ? x.replaceFirst(StateList.NOT + " ", "") : x, isNegated);
        }).toArray(State[]::new);
   }

    @Override
    public String toString() {
        return (isNegated ? StateList.NOT + " " : "") + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (isNegated != state.isNegated) return false;
        return name.equals(state.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (isNegated ? 1 : 0);
        return result;
    }
}
