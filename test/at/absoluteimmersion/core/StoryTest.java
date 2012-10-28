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
    public void interact_noEnterAction_throwsException() throws StoryException {
        Story story = new Story();
        story.addPart(new Part("chapter", ""));
        story.tell();
    }

    @Test()
    public void interact_initial_callsReaction() throws StoryException {
        Story story = new Story();
        Part part = new Part("testpart", "");
        Action action = new Action("enter");
        Part actionText = new Part("", "introduction");
        action.addPart(actionText);
        part.addAction(action);
        story.addPart(part);
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(Matchers.anyString());
    }

    @Test()
    public void interact_initial_callsReactionWithCorrectParameter() throws StoryException {
        Story story = new Story();
        Part part = new Part("chapter01", "");
        Action action = new Action("enter");
        Part actionText = new Part("", "introduction");
        action.addPart(actionText);
        part.addAction(action);
        story.addPart(part);
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter01");
        verify(client).reaction("introduction");
    }
}
