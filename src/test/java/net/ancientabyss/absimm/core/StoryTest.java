package net.ancientabyss.absimm.core;

import net.ancientabyss.absimm.parser.TxtParser;
import net.ancientabyss.absimm.parser.XmlParser;
import net.ancientabyss.absimm.util.AbsimFile;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class StoryTest {

    private String testFilename = "test.abs";

    @Test(expected = StoryException.class)
    public void interact_noContent_throwsException() throws StoryException {
        Story story = new Story(new StateList(), new Settings());
        story.interact("test");
    }

    @Test
    public void setState_validState_setsState() throws StoryException {
        StateList stateList = new StateList();
        stateList.add("s1");
        stateList.add("s2");
        Story story = new Story(stateList, new Settings());
        String expected = story.getState();
        story.setState("");
        Assert.assertEquals("", stateList.serialize());
        story.setState(expected);
        Assert.assertEquals(expected, stateList.serialize());
    }

    @Test(expected = StoryException.class)
    public void tell_noContent_throwsException() throws StoryException {
        Story story = new Story(new StateList(), new Settings());
        story.tell();
    }

    @Test(expected = StoryException.class)
    public void tell_noInitialCommandAction_throwsException() throws StoryException {
        Story story = new Story(new StateList(), new Settings());
        story.addPart(new Part("chapter"));
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
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
        Settings settings = new Settings();
        settings.addSetting("initial_command", "enter chapter01");
        settings.addSetting("object_error", "No such object!");
        settings.addSetting("action_error", "You cannot do this with this object!");
        settings.addSetting("save_command", "save");
        settings.addSetting("save_error", "save error");
        settings.addSetting("save_invalid_command", "save invalid");
        settings.addSetting("save_success", "save success");
        settings.addSetting("load_command", "load");
        settings.addSetting("load_error", "load error");
        settings.addSetting("load_invalid_command", "load invalid");
        settings.addSetting("load_success", "load success");
        settings.addSetting("quit_command", "quit");
        Story story = new Story(new StateList(), settings);
        Part part = new Part("chapter01");
        Action action = new Action("enter", part.getName(), mock(StateList.class), story);
        Action action2 = new Action("enter", "introduction", mock(StateList.class), story);
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
            open = new Action("open", "Locker is open now!", "have_key", "locker_open", list, new Story(list, new Settings()));
            Action locked = new Action("open", "You need a key!", "NOT have_key", "", list, new Story(list, new Settings()));
            locker.addAction(locked);
        } else {
            open = new Action("open", "Locker is open now!", mock(StateList.class), new Story(list, new Settings()));
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
        when(locker1.conditionsMet()).thenReturn(true);
        when(locker1.execute()).thenReturn("");
        locker.addAction(locker1);
        part.addPart(locker);
        part = new Part("chapter02");
        locker = new Part("locker");
        Action locker2 = mock(Action.class);
        when(locker2.getName()).thenReturn("open");
        when(locker2.conditionsMet()).thenReturn(true);
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
        Settings settings = new Settings();
        settings.addSetting("initial_command", "enter chapter01");
        Story story = new Story(stateList, settings);
        Part part = new Part("chapter01", "", stateList);
        Action action = new Action("enter", "introduction", "", "in_chapter01", stateList, story);
        part.addAction(action);
        story.addPart(part);
        Part locker = new Part("locker");
        Action locker1 = mock(Action.class);
        when(locker1.getName()).thenReturn("open");
        when(locker1.conditionsMet()).thenReturn(true);
        when(locker1.execute()).thenReturn("");
        locker.addAction(locker1);
        part.addPart(locker);
        part = new Part("chapter02", "in_chapter02", stateList);
        locker = new Part("locker");
        Action locker2 = mock(Action.class);
        when(locker2.getName()).thenReturn("open");
        when(locker2.conditionsMet()).thenReturn(true);
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
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_02.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("You cannot do this with this object!");
    }

    @Test
    public void interact_spacesInActionNames_work() throws StoryException {
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_04.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        story.interact("take key");
        verify(client).reaction("You found a small key!");
        story.interact("use small locker with key blah");
        verify(client).reaction("No such object!");
        story.interact("use small locker with blah key");
        verify(client).reaction("You cannot do this with this object!");
        story.interact("use small locker with key");
        verify(client).reaction("The very nice locker is unlocked now!");
    }

    @Test
    public void interact_hint_returnsAllPossibleActions() throws StoryException {
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_03.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter_01");
        verify(client).reaction("Entered chapter 01!");
        story.interact("hint");
        verify(client).reaction("locker (look)\nkey (take)");
        story.interact("take key");
        verify(client).reaction("You found a key!");
        story.interact("hint");
        verify(client).reaction("locker (look, open)");
        story.interact("open locker");
        verify(client).reaction("The locker is open now!");
        story.interact("hint");
        verify(client).reaction("locker (look, open, enter)");
    }

    @Test
    public void interact_hint_doesNotShowInternalActions() throws StoryException {
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_03.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter_01");
        verify(client).reaction("Entered chapter 01!");
        story.interact("take key");
        verify(client).reaction("You found a key!");
        story.interact("open locker");
        verify(client).reaction("The locker is open now!");
        story.interact("enter locker");
        verify(client).reaction("You entered the locker!");
        verify(client).reaction("chapter_02");
        story.interact("hint");
        verify(client).reaction("small locker (look)\nlocker (leave)\nkey (take)");
    }

    @Test
    public void interact_help_showsHelp() throws StoryException {
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_03.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client).reaction("chapter_01");
        verify(client).reaction("Entered chapter 01!");
        story.interact("help");
        verify(client).reaction("\\n-------------\\nType 'hint' if you are stuck and 'quit' if you want to stop playing.\\nUse 'save xx' to save your progress to the slot 'xx' and 'load xx' to load your progress from slot 'xx'.\\n'help' will bring this info up again.\\n-------------\\n");
    }

    @Test
    public void interact_nonexistentObject_returnsErrorMessage() throws StoryException {
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_02.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        story.interact("look blah");
        verify(client).reaction("No such object!");
    }

    @Test
    public void interact_onlyOneWord_throwsException() throws StoryException {
        Loader loader = createLoader();
        Story story = null;
        ReactionClient client = mock(ReactionClient.class);
        try {
            story = loader.fromFile("res/test_03.xml");
            story.addClient(client);
            story.tell();
        } catch (Exception e) {
            Assert.fail();
        }
        story.interact("ranz");
        InOrder inOrder = inOrder(client);
        inOrder.verify(client).reaction("chapter_01");
        inOrder.verify(client).reaction("No such object!");
    }

    @Test
    public void tell_multipleChaptersWithConditions_playsGameWithoutProblem() throws StoryException {
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_03.xml");
        ReactionClient client = mock(ReactionClient.class);
        InOrder inOrder = inOrder(client);
        story.addClient(client);
        story.tell();
        inOrder.verify(client).reaction("chapter_01");
        inOrder.verify(client).reaction("Entered chapter 01!");
        story.interact("open locker");
        inOrder.verify(client).reaction("You cannot do this with this object!");
        story.interact("enter locker");
        inOrder.verify(client).reaction("You cannot do this with this object!");
        story.interact("look locker");
        inOrder.verify(client).reaction("A nice locker!");
        story.interact("take key");
        inOrder.verify(client).reaction("You found a key!");
        story.interact("open locker");
        inOrder.verify(client).reaction("The locker is open now!");
        story.interact("enter locker");
        inOrder.verify(client).reaction("chapter_02");
        inOrder.verify(client).reaction("You entered the locker!");
        story.interact("open small locker");
        inOrder.verify(client).reaction("You cannot do this with this object!");
        story.interact("enter small locker");
        inOrder.verify(client).reaction("You cannot do this with this object!");
        story.interact("look small locker");
        inOrder.verify(client).reaction("A very nice locker!");
        story.interact("take key");
        inOrder.verify(client).reaction("You found a small key!");
        story.interact("open small locker");
        inOrder.verify(client).reaction("The very nice locker is open now!");
        story.interact("leave locker");
        inOrder.verify(client).reaction("You left the locker!");
    }

    @Test()
    public void interact_save_savesStateList() throws StoryException, IOException {
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_03.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.tell();
        story.interact("take key");
        story.addClient(client);
        story.interact("save 01.dat");
        verify(client).reaction("Game successfully saved.");
        Assert.assertEquals("game_started,in_chapter01,have_key", AbsimFile.readFileAsString("01.dat"));
    }

    @Test()
    public void interact_load_loadsStateList() throws StoryException, IOException {
        Loader loader = createLoader();
        Story story = loader.fromFile("res/test_03.xml");
        ReactionClient client = mock(ReactionClient.class);
        story.tell();
        story.interact("take key");
        story.interact("save 01.dat");
        story = loader.fromFile("res/test_03.xml");
        story.addClient(client);
        story.interact("load 01.dat");
        verify(client).reaction("Game successfully loaded.");
        story.interact("open locker");
        verify(client).reaction("The locker is open now!");
    }

    @Test()
    public void interact_txtFile_shouldAddActions() throws StoryException, IOException {
        Loader loader = new Loader(new TxtParser());
        Story story = loader.fromFile("res/test_06.txt");
        ReactionClient client = mock(ReactionClient.class);
        story.tell();
        story.addClient(client);
        story.interact("a");
        verify(client).reaction("a!");
    }

    @Test()
    public void interact_txtFile_shouldShowHints() throws StoryException, IOException {
        Loader loader = new Loader(new TxtParser());
        Story story = loader.fromFile("res/test_06.txt");
        ReactionClient client = mock(ReactionClient.class);
        story.tell();
        story.addClient(client);
        story.interact("hint");
        verify(client).reaction("a\nb");
    }

    @Test()
    public void save_noFilenameGiven_returnsError() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(anyString());
        story.interact("save");
        verify(client).reaction("save invalid");
    }

    @Test()
    public void save_invalidFilename_returnsError() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(anyString());
        story.interact("save /i/do/not/exist");
        verify(client).reaction("save error");
    }

    @Test()
    public void save_validFilename_returnsSuccess() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(anyString());
        story.interact("save " + testFilename);
        verify(client).reaction("save success");
        //noinspection ResultOfMethodCallIgnored
        new File(testFilename).delete();
    }

    @Test()
    public void load_noFilenameGiven_returnsError() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(anyString());
        story.interact("load");
        verify(client).reaction("load invalid");
    }

    @Test()
    public void load_invalidFilename_returnsError() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(anyString());
        story.interact("load /i/do/not/exist");
        verify(client).reaction("load error");
    }

    @Test()
    public void load_validFilename_returnsSuccess() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(anyString());
        story.interact("save " + testFilename);
        verify(client).reaction("save success");
        story.interact("load " + testFilename);
        verify(client).reaction("load success");
        //noinspection ResultOfMethodCallIgnored
        new File(testFilename).delete();
    }

    @Test
    public void interact_quit_doesNothing() throws StoryException {
        Story story = createStory();
        ReactionClient client = mock(ReactionClient.class);
        story.addClient(client);
        story.tell();
        verify(client, times(2)).reaction(anyString());
        story.interact("quit");
    }

    private Loader createLoader() {
        return new Loader(new XmlParser());
    }
}
