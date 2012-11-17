package at.absoluteimmersion.core;

import java.util.ArrayList;
import java.util.List;

// TODO: kill
@Deprecated
public class StateList {
    private List<String> list = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateList stateList = (StateList) o;

        if (list != null ? !list.equals(stateList.list) : stateList.list != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return list != null ? list.hashCode() : 0;
    }

    public void add(String state) {
        list.add(state);
    }

    public boolean contains(String state) {
        return list.contains(state);
    }

    public int lastIndexOf(String state) {
        return list.lastIndexOf(state);
    }
}
