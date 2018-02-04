package net.ancientabyss.absimm.history;

import java.util.List;

/**
 * Use the History to track user input and output of the engine.
 */
public interface History {
    void add(String text, HistoryType type);
    void clear();
    List<HistoryEntry> getAll();
}
