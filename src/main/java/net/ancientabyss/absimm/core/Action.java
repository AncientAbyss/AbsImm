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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Action action = (Action) o;

        if (command != null ? !command.equals(action.command) : action.command != null) return false;
        if (state != null ? !state.equals(action.state) : action.state != null) return false;
        //if (story != null ? !story.equals(action.story) : action.story != null) return false;
        if (text != null ? !text.equals(action.text) : action.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (command != null ? command.hashCode() : 0);
        //result = 31 * result + (story != null ? story.hashCode() : 0);
        return result;
    }

    public String execute() throws StoryException {
        if (!conditionsMet()) return "";
        if (!state.isEmpty()) stateList.add(state);
        if (!command.isEmpty()) story.interact(command);
        return text;
    }
}
