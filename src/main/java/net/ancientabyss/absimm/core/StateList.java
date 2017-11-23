package net.ancientabyss.absimm.core;

import net.ancientabyss.absimm.util.AbsimFile;
import net.ancientabyss.absimm.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StateList {

    public static final String NOT = "NOT";
    public static final String AND = "AND";

    private List<State> list = new ArrayList<>();

    public void set(State state) {
        if (isSet(state)) return;
        Optional<State> lastState = getLastState(state);
        if (lastState.isPresent()) {
            lastState.get().setNegated(state.isNegated());
        } else {
            list.add(state);
        }
    }

    public void setAll(State[] states) {
        Arrays.stream(states).forEach(this::set);
    }

    private boolean isSet(State state) {
        Optional<State> lastState = getLastState(state);
        return lastState.filter(x -> x.isNegated() == state.isNegated()).isPresent();
    }

    private Optional<State> getLastState(State state) {
        return list.stream().filter(x -> Objects.equals(x.getName(), state.getName())).reduce((x, y) -> y);
    }

    public boolean areAllSatisfied(State[] states) {
        return Arrays.stream(states).map(this::isSatisfied).allMatch(x -> x);
    }

    public boolean isSatisfied(State state) {
        Optional<State> lastState = getLastState(state);
        return lastState.map(x -> x.isNegated() == state.isNegated()).orElseGet(state::isNegated);
    }

    public void save(String file) throws IOException {
        AbsimFile.writeFile(file, serialize());
    }

    public void load(String file) throws IOException {
        deserialize(AbsimFile.readFileAsString(file));
    }

    public String serialize() {
        return StringUtils.join(list.stream().map(State::toString).collect(Collectors.toList()), " " + AND + " ");
    }

    public void deserialize(String data) {
        list = Arrays.stream(State.fromString(data)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return serialize();
    }

    @Override
    public int hashCode() {
        return list != null ? list.hashCode() : 0;
    }
}
