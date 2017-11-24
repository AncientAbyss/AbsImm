package net.ancientabyss.absimm.core;

import java8.util.J8Arrays;
import java8.util.Optional;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import net.ancientabyss.absimm.util.AbsimFile;
import net.ancientabyss.absimm.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StateList {

    public static final String NOT = "NOT";
    public static final String AND = "AND";

    private Map<String, State> list = new HashMap<>();

    public void set(State state) {
        if (isSet(state)) return;
        if (list.containsKey(state.getName())) {
            list.get(state.getName()).setNegated(state.isNegated());
        } else {
            list.put(state.getName(), state);
        }
    }

    public void setAll(State[] states) {
        J8Arrays.stream(states).forEach(this::set);
    }

    private boolean isSet(State state) {
        Optional<State> lastState = getLastState(state);
        return lastState.filter(x -> x.isNegated() == state.isNegated()).isPresent();
    }

    private Optional<State> getLastState(State state) {
        return Optional.ofNullable(list.get(state.getName()));
    }

    public boolean areAllSatisfied(State[] states) {
        return J8Arrays.stream(states).map(this::isSatisfied).allMatch(x -> x);
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
        return StringUtils.join(StreamSupport.stream(list.values()).map(State::toString).collect(Collectors.toList()), " " + AND + " ");
    }

    public void deserialize(String data) {
        list = new HashMap<>();
        J8Arrays.stream(State.fromString(data)).forEachOrdered(x -> list.put(x.getName(), x));
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
