package net.ancientabyss.absimm.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultHistory implements History {
    private List<HistoryEntry> entries = new ArrayList<>();

    @Override
    public void add(String text, HistoryType type) {
        entries.add(new HistoryEntry(new Date(), type, text));
    }

    @Override
    public void clear() {
        entries.clear();
    }

    @Override
    public List<HistoryEntry> getAll() {
        return entries;
    }
}
