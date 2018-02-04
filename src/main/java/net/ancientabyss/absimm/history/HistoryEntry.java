package net.ancientabyss.absimm.history;

import java.util.Date;

public class HistoryEntry {
    private Date created;
    private HistoryType type;
    private String text;

    public HistoryEntry(Date created, HistoryType type, String text) {
        this.created = created;
        this.type = type;
        this.text = text;
    }

    public Date getCreated() {
        return created;
    }

    public HistoryType getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
