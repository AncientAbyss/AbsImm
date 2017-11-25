package net.ancientabyss.absimm.models;

import org.junit.Assert;
import org.junit.Test;

public class StatisticsTest {
    @Test
    public void getEfficiency_veryEfficient_returnsEfficiency() {
        Statistics statistics = new Statistics();
        statistics.increaseNumValidCommands();
        Assert.assertEquals(100f, statistics.getEfficiency(), 0.01f);
    }

    @Test
    public void getEfficiency_notEfficient_returnsEfficiency() {
        Statistics statistics = new Statistics();
        statistics.increaseNumOptionalCommands();
        Assert.assertEquals(0f, statistics.getEfficiency(), 0.01f);
    }

    @Test
    public void getEfficiency_somewhatEfficient_returnsEfficiency() {
        Statistics statistics = new Statistics();
        statistics.increaseNumOptionalCommands();
        statistics.increaseNumValidCommands();
        statistics.increaseNumValidCommands();
        Assert.assertEquals(66.66f, statistics.getEfficiency(), 0.01f);
    }

    @Test
    public void getClumsiness_veryClumsy_returnsClumsyness() {
        Statistics statistics = new Statistics();
        statistics.increaseNumInvalidCommands();
        Assert.assertEquals(100f, statistics.getClumsiness(), 0.01f);
    }

    @Test
    public void getClumsiness_notClumsy_returnsClumsyness() {
        Statistics statistics = new Statistics();
        statistics.increaseNumValidCommands();
        Assert.assertEquals(0f, statistics.getClumsiness(), 0.01f);
    }

    @Test
    public void getClumsiness_somewhatClumsy_returnsClumsiness() {
        Statistics statistics = new Statistics();
        statistics.increaseNumValidCommands();
        statistics.increaseNumValidCommands();
        statistics.increaseNumInvalidCommands();
        Assert.assertEquals(33.33f, statistics.getClumsiness(), 0.01f);
    }

    @Test
    public void getHelplessness_veryHelpless_returnsHelplessness() {
        Statistics statistics = new Statistics();
        statistics.increaseNumUsedHints();
        Assert.assertEquals(100f, statistics.getHelplessness(), 0.01f);
    }

    @Test
    public void getHelplessness_notHelpless_returnsHelplessness() {
        Statistics statistics = new Statistics();
        statistics.increaseNumValidCommands();
        Assert.assertEquals(0f, statistics.getHelplessness(), 0.01f);
    }

    @Test
    public void getHelplessness_somewhatHelpless_returnsHelplessness() {
        Statistics statistics = new Statistics();
        statistics.increaseNumValidCommands();
        statistics.increaseNumValidCommands();
        statistics.increaseNumUsedHints();
        Assert.assertEquals(33.33f, statistics.getHelplessness(), 0.01f);
    }
}
