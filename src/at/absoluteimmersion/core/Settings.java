package at.absoluteimmersion.core;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    private Map<String, String> settings = new HashMap<String, String>();

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

    @Override
    public String toString() {
        return "Settings{" +
                "settings=" + settings +
                '}';
    }
}
