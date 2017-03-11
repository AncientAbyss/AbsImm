package at.absoluteimmersion.core;

import at.absoluteimmersion.util.AbsimFile;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public void save(String file) throws IOException {
        AbsimFile.writeFile(file, serialize());
    }

    public void load(String file) throws IOException {
        deserialize(AbsimFile.readFileAsString(file));
    }

    public String serialize() {
        return StringUtils.join(list, ",");
    }

    public void deserialize(String data) {
        list = new ArrayList<>(Arrays.asList(data.split(",")));
    }
}
