package net.ancientabyss.absimm.history;

import org.junit.Assert;
import org.junit.Test;

public class DefaultHistoryTest {
    @Test
    public void add_validData_storesEntry() {
        History history = new DefaultHistory();
        history.add("test", HistoryType.INPUT);
        HistoryEntry actual = history.getAll().get(0);
        Assert.assertEquals(actual.getText(), "test");
        Assert.assertEquals(actual.getType(), HistoryType.INPUT);
        Assert.assertNotNull(actual.getCreated());
    }

    @Test
    public void clear_validData_removesEntries() {
        History history = new DefaultHistory();
        history.add("test", HistoryType.INPUT);
        Assert.assertEquals(history.getAll().size(), 1);
        history.clear();
        Assert.assertEquals(history.getAll().size(), 0);
    }
}
