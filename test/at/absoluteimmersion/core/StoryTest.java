package at.absoluteimmersion.core;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;

import static org.mockito.Mockito.*;

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

    @Test
    public void tell_noEnterAction_throwsException() throws StoryException {
        Story story = new Story();
        story.addPart(new Part("chapter"));
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("You can not do this with this object!");
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
        Action action = new Action(Story.INITIAL_ACTION, part.getName(), mock(StateList.class), story);
        Action action2 = new Action(Story.INITIAL_ACTION, "introduction", mock(StateList.class), story);
        part.addAction(action);
        part.addAction(action2);
        story.addPart(part);
        return story;
    }

    @Test()
    public void tell_actionOnNonChapterPart_callsReactionWithCorrectParameter() throws StoryException {
        Story story = createStory();
        Part part = (Part) story.findAll("chapter01").get(0);
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
        if (condition) {
            open = new Action("open", "Locker is open now!", "have_key", "locker_open", list, new Story());
            Action locked = new Action("open", "You need a key!", "not have_key", "", list, new Story());
            locker.addAction(locked);
        } else {
            open = new Action("open", "Locker is open now!", mock(StateList.class), new Story());
        }
        locker.addAction(open);
        return locker;
    }

    @Test()
    public void tell_actionWithConditionNotMet_returnsEmptyString() throws StoryException {
        Story story = createStory();
        Part part = (Part) story.findAll("chapter01").get(0);
        Part locker = new Part("locker");
        Action open;
        StateList list = new StateList();
        open = new Action("open", "Locker is open now!", "have_key", "locker_open", list, story);
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
        Part part = (Part) story.findAll("chapter01").get(0);
        part.addPart(createLocker(true));
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter01");
        verify(client).reaction("introduction");
        story.interact("open locker");
        verify(client).reaction("You need a key!");
    }

    @Test
    public void tell_multipleChapters_executesAllActions() throws StoryException {
        Story story = createStory();
        Part part = (Part) story.findAll("chapter01").get(0);
        Part locker = new Part("locker");
        Action locker1 = mock(Action.class);
        when(locker1.getName()).thenReturn("open");
        when(locker1.conditionMet()).thenReturn(true);
        when(locker1.execute()).thenReturn("");
        locker.addAction(locker1);
        part.addPart(locker);
        part = new Part("chapter02");
        locker = new Part("locker");
        Action locker2 = mock(Action.class);
        when(locker2.getName()).thenReturn("open");
        when(locker2.conditionMet()).thenReturn(true);
        when(locker2.execute()).thenReturn("");
        locker.addAction(locker2);
        part.addPart(locker);
        story.addPart(part);
        story.tell();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.interact("open locker");
        verify(locker1).execute();
        verify(locker2).execute();
    }

    @Test
    public void tell_multipleChaptersWithConditions_executesOneAction() throws StoryException {
        StateList stateList = new StateList();
        Story story = new Story();
        Part part = new Part("chapter01", "", stateList);
        Action action = new Action(Story.INITIAL_ACTION, "introduction", "", "in_chapter01", stateList, story);
        part.addAction(action);
        story.addPart(part);
        Part locker = new Part("locker");
        Action locker1 = mock(Action.class);
        when(locker1.getName()).thenReturn("open");
        when(locker1.conditionMet()).thenReturn(true);
        when(locker1.execute()).thenReturn("");
        locker.addAction(locker1);
        part.addPart(locker);
        part = new Part("chapter02", "in_chapter02", stateList);
        locker = new Part("locker");
        Action locker2 = mock(Action.class);
        when(locker2.getName()).thenReturn("open");
        when(locker2.conditionMet()).thenReturn(true);
        when(locker2.execute()).thenReturn("");
        locker.addAction(locker2);
        part.addPart(locker);
        story.addPart(part);
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        story.interact("open locker");
        verify(locker1).execute();
        verifyNoMoreInteractions(locker2);
    }

    @Test
    public void tell_multipleChaptersWithActionsWithUnsatisfiedConditions_returnsErrorMessage() throws StoryException {
        Loader loader = new Loader();
        Story story = loader.fromFile("res/test_02.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("You can not do this with this object!");
    }

    @Test
    public void interact_nonexistentObject_returnsErrorMessage() throws StoryException {
        Loader loader = new Loader();
        Story story = loader.fromFile("res/test_02.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        story.interact("look blah");
        verify(client).reaction("No such object!");
    }

    @Test
    public void tell_multipleChaptersWithConditions_playsGameWithoutProblem() throws StoryException {
        Loader loader = new Loader();
        Story story = loader.fromFile("res/test_03.xml");
        ReactionClient client = mock(ReactionClient.class);
        InOrder inOrder = inOrder(client);
        story.addClient(client);
        story.tell();
        inOrder.verify(client).reaction("chapter_01");
        inOrder.verify(client).reaction("Entered chapter 01!");
        story.interact("open locker");
        inOrder.verify(client).reaction("You can not do this with this object!");
        story.interact("enter locker");
        inOrder.verify(client).reaction("You can not do this with this object!");
        story.interact("look locker");
        inOrder.verify(client).reaction("A nice locker!");
        story.interact("take key");
        inOrder.verify(client).reaction("You found a key!");
        story.interact("open locker");
        inOrder.verify(client).reaction("The locker is open now!");
        story.interact("enter locker");
        inOrder.verify(client).reaction("chapter_02");
        inOrder.verify(client).reaction("You entered the locker!");
        story.interact("open smalllocker");
        inOrder.verify(client).reaction("You can not do this with this object!");
        story.interact("enter smalllocker");
        inOrder.verify(client).reaction("You can not do this with this object!");
        story.interact("look smalllocker");
        inOrder.verify(client).reaction("A very nice locker!");
        story.interact("take key");
        inOrder.verify(client).reaction("You found a small key!");
        story.interact("open smalllocker");
        inOrder.verify(client).reaction("The very nice locker is open now!");
        story.interact("leave locker");
        inOrder.verify(client).reaction("You left the locker!");
    }
}
