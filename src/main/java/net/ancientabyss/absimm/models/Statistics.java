package net.ancientabyss.absimm.models;

public class Statistics {
    private int numUsedHints;
    private int numValidCommands;
    private int numInvalidCommands;
    private int numOptionalCommands;

    public int getNumUsedHints() {
        return numUsedHints;
    }

    public void increaseNumUsedHints() {
        ++this.numUsedHints;
    }

    public int getNumValidCommands() {
        return numValidCommands;
    }

    public void increaseNumValidCommands() {
        ++numValidCommands;
    }

    public int getNumInvalidCommands() {
        return numInvalidCommands;
    }

    public void increaseNumInvalidCommands() {
        ++numInvalidCommands;
    }

    public int getNumOptionalCommands() {
        return numOptionalCommands;
    }

    public void increaseNumOptionalCommands() {
        ++numOptionalCommands;
    }

    public float getEfficiency() {
        return (float) numValidCommands / (numValidCommands + numOptionalCommands) * 100;
    }

    public float getHelplessness() {
        return (float) numUsedHints / (numValidCommands + numUsedHints) * 100;
    }

    public float getClumsiness() {
        return (float) numInvalidCommands / (numValidCommands + numInvalidCommands) * 100;
    }
}
