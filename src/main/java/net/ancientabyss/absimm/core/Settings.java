package net.ancientabyss.absimm.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Settings {

    private Map<String, String> settings = new HashMap<>();
    private Random rand = new Random();
    private static final String listSeparator = "\\|";

    public void addSetting(String name, String value) {
        settings.put(name, value);
    }

    public String getSetting(String name) {
        return settings.get(name);
    }

    public String getRandom(String name) {
        String[] elements = toArray(name);
        return elements[rand.nextInt(elements.length)];
    }

    private String[] toArray(String name) {
        return settings.get(name).split(listSeparator);
    }

    @Override
    public String toString() {
        return "Settings{" +
                "settings=" + settings +
                '}';
    }
}
