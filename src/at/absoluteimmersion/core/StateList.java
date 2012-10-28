package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.List;

public class StateList {
    private List<String> list = new ArrayList<>();

    public void add(String state) {
        list.add(state);
    }

    public boolean contains(String state) {
        return list.contains(state);
    }
}
