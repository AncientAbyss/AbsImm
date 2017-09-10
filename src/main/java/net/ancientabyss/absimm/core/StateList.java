package net.ancientabyss.absimm.core;

import net.ancientabyss.absimm.util.AbsimFile;
import net.ancientabyss.absimm.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.ancientabyss.absimm.core.BasePart.AND;

public class StateList {
    private List<String> list = new ArrayList<>();

    public void add(String state) {
        String[] individualStates = state.split(" " + AND + " ");
        Collections.addAll(list, individualStates);
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

    @Override
    public String toString() {
        return serialize();
    }
}
