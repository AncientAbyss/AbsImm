package at.absoluteimmersion.core;

import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StoryTest {

    @Test(expected = StoryException.class)
    public void interact_noContent_throwsException() throws StoryException {
        Story story = new Story();
        story.interact("test");
    }

    @Test()
    public void interact_initial_callsReaction() throws StoryException {
        Story story = new Story();
        story.addSection("start", new StorySection("start"));
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.interact("start");
        verify(client).reaction(Matchers.anyString());
    }

    @Test()
    public void interact_initial_callsReactionWithCorrectParameter() throws StoryException {
        Story story = new Story();
        StorySection storySection = new StorySection("init");
        story.addSection("start", storySection);
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.interact("start");
        verify(client).reaction("init");
    }
}
