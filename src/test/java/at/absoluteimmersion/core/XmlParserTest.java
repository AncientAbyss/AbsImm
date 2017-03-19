package at.absoluteimmersion.core;

import at.absoluteimmersion.parser.XmlParser;
import org.junit.Assert;
import org.junit.Test;

public class XmlParserTest {

    @Test(expected = StoryException.class)
    public void fromString_emptyString_throwsException() throws StoryException {
        Loader loader = createLoader();
        loader.fromString("");
    }

    @Test
    public void fromString_storyOnly_createsStory() throws StoryException {
        Loader loader = createLoader();
        Story story = loader.fromString("<story></story>");
        Assert.assertEquals(TestHelper.createDefaultStory(), story);
    }

    @Test
    public void fromString_storyWithParts_createsStoryWithParts() throws StoryException {
        Loader loader = createLoader();
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory();
        expected.addPart(new Part("chapter_01", "in_chapter01", stateList));
        expected.addPart(new Part("chapter_02", "", stateList));
        Story actual = loader.fromString("<story><part name='chapter_01' condition='in_chapter01'/><part name='chapter_02'/></story>");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fromString_storyWithPartsAndActions_createsStoryWithPartsAndActions() throws StoryException {
        Loader loader = createLoader();
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory();
        Part part = new Part("chapter_01", "", stateList);
        part.addAction(new Action("enter", "opened1", "has_lock1", "in_chapter01", stateList, expected));
        expected.addPart(part);
        part = new Part("chapter_02", "", stateList);
        part.addAction(new Action("enter", "opened2", "has_lock2", "in_chapter02", stateList, expected, "kill me"));
        expected.addPart(part);
        Story actual = loader.fromString("<story><part name='chapter_01'><action name='enter' state='in_chapter01' condition='has_lock1' text='opened1'/></part><part name='chapter_02'><action name='enter' state='in_chapter02' condition='has_lock2' text='opened2' command='kill me'/></part></story>");
        Assert.assertEquals(expected, actual);
    }

    @Test
     public void fromFile_storyWithPartsAndActions_createsStoryWithPartsAndActions() throws StoryException {
        Loader loader = createLoader();
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory();
        expected.getSettings().addSetting("initial_command", "enter chapter_01");
        Part part = new Part("chapter_01", "", stateList);
        part.addAction(new Action("enter", "opened1", "has_lock1", "in_chapter01", stateList, expected));
        expected.addPart(part);
        part = new Part("chapter_02", "", stateList);
        part.addAction(new Action("enter", "opened2", "has_lock2", "in_chapter02", stateList, expected));
        expected.addPart(part);
        Story actual = loader.fromFile("res/test_01.xml");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fromFile_storySettings_createsStoryWithSettings() throws StoryException {
        Loader loader = createLoader();
        StateList stateList = new StateList();
        Story expected = new Story(stateList, TestHelper.createDefaultSettings());
        expected.getSettings().addSetting("initial_command", "enter chapter_01");
        Part part = new Part("chapter_01", "", stateList);
        part.addAction(new Action("enter", "opened1", "has_lock1", "in_chapter01", stateList, expected));
        expected.addPart(part);
        part = new Part("chapter_02", "", stateList);
        part.addAction(new Action("enter", "opened2", "has_lock2", "in_chapter02", stateList, expected));
        expected.addPart(part);
        Story actual = loader.fromFile("res/test_05.xml");
        Assert.assertEquals(expected, actual);
    }

    private Loader createLoader() {
        return new Loader(new XmlParser());
    }
}
