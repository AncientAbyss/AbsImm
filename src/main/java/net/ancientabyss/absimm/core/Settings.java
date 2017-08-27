package net.ancientabyss.absimm.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Settings {

    private Map<String, String> settings = new HashMap<>();
    private Random rand = new Random();
    private static final String listSeparator = "\\|";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings settings1 = (Settings) o;

        if (!settings.equals(settings1.settings)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return settings.hashCode();
    }

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

    public int getSettingsCount() {
        return settings.size();
    }

    @Override
    public String toString() {
        return "Settings{" +
                "settings=" + settings +
                '}';
    }
}
