package at.absoluteimmersion.core;

import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StoryTest {

    @Test(expected = StoryException.class)
    public void interact_noContent_throwsException() throws StoryException {
        Story story = new Story();
        story.interact("test");
    }

    @Test(expected = StoryException.class)
    public void tell_noContent_throwsException() throws StoryException {
        Story story = new Story();
        story.tell();
    }

    @Test(expected = StoryException.class)
    public void tell_noEnterAction_throwsException() throws StoryException {
        Story story = new Story();
        story.addPart(new Part("chapter"));
        story.tell();
    }

    @Test()
    public void tell_hasEnterAction_callsReaction() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(Matchers.anyString());
    }

    @Test()
    public void tell_hasEnterAction_callsReactionWithCorrectParameter() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter01");
        verify(client).reaction("introduction");
    }

    private Story createStory() {
        Story story = new Story();
        Part part = new Part("chapter01");
        Action action = new Action(Story.INITIAL_ACTION, "introduction", mock(StateList.class));
        part.addAction(action);
        story.addPart(part);
        return story;
    }

    @Test()
    public void tell_actionOnNonChapterPart_callsReactionWithCorrectParameter() throws StoryException {
        Story story = createStory();
        Part part = (Part) story.find("chapter01");
        part.addPart(createLocker(false));
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter01");
        verify(client).reaction("introduction");
        story.interact("open locker");
        verify(client).reaction("Locker is open now!");
    }

    private Part createLocker(boolean condition) {
        Part locker = new Part("locker");
        Action open;
        StateList list = new StateList();
        if (condition)
        {
            open = new Action("open", "Locker is open now!", "have_key", "locker_open", list);
            Action locked = new Action("open", "You need a key!", "not have_key", "", list);
            locker.addAction(locked);
        }
        else
        {
            open = new Action("open", "Locker is open now!", mock(StateList.class));
        }
        locker.addAction(open);
        return locker;
    }

    @Test()
    public void tell_actionWithConditionNotMet_returnsEmptyString() throws StoryException {
        Story story = createStory();
        Part part = (Part) story.find("chapter01");
        Part locker = new Part("locker");
        Action open;
        StateList list = new StateList();
        open = new Action("open", "Locker is open now!", "have_key", "locker_open", list);
        locker.addAction(open);
        part.addPart(locker);
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter01");
        verify(client).reaction("introduction");
        story.interact("open locker");
    }

    @Test()
    public void tell_actionWithCondition_callsReactionWithCorrectParameter() throws StoryException {
        Story story = createStory();
        Part part = (Part) story.find("chapter01");
        part.addPart(createLocker(true));
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter01");
        verify(client).reaction("introduction");
        story.interact("open locker");
        verify(client).reaction("You need a key!");
    }
}
