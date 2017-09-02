package net.ancientabyss.absimm.core;

public class Action extends BasePart {

    private String text;
    private String state;
    private String command;
    private Story story;

    public Action(String name, String text, String condition, String state, StateList stateList, Story story) {
        super(name, condition, stateList);
        this.story = story;
        this.text = text;
        this.state = state;
        this.command = "";
    }

    public Action(String name, String text, StateList stateList, Story story) {
        this(name, text, "", "", stateList, story);
    }

    public Action(String name, String text, String condition, String state, StateList stateList, Story story, String command) {
        this(name, text, condition, state, stateList, story);
        this.command = command;
    }

    public String execute() throws StoryException {
        if (!conditionsMet()) return "";
        if (!state.isEmpty()) stateList.add(state);
        if (!command.isEmpty()) story.interact(command);
        return text;
    }

    @Override
    public String toString() {
        return super.toString() + " >> Action{" +
                "text='" + text + '\'' +
                ", state='" + state + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
