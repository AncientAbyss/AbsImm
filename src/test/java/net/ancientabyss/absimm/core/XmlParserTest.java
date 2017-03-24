package net.ancientabyss.absimm.core;

import net.ancientabyss.absimm.parser.XmlParser;
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
        Assert.assertEquals(TestHelper.createDefaultStory(false), story);
    }

    @Test
    public void fromString_storyWithParts_createsStoryWithParts() throws StoryException {
        Loader loader = createLoader();
        Story expected = TestHelper.createStoryWithParts();
        Story actual = loader.fromString("<story><part name='chapter_01' condition='in_chapter01'/><part name='chapter_02'/></story>");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fromString_storyWithPartsAndActions_createsStoryWithPartsAndActions() throws StoryException {
        Loader loader = createLoader();
        Story expected = TestHelper.createStoryWithPartAndActions();
        Story actual = loader.fromString("<story><part name='chapter_01'><action name='enter' state='in_chapter01' condition='has_lock1' text='opened1'/></part><part name='chapter_02'><action name='enter' state='in_chapter02' condition='has_lock2' text='opened2' command='kill me'/></part></story>");
        Assert.assertEquals(expected, actual);
    }

    @Test
     public void fromFile_storyWithPartsAndActions_createsStoryWithPartsAndActions() throws StoryException {
        Loader loader = createLoader();
        Story expected = TestHelper.createStoryWithPartsAndActions2(false);
        Story actual = loader.fromFile("res/test_01.xml");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fromFile_storySettings_createsStoryWithSettings() throws StoryException {
        Loader loader = createLoader();
        Story expected = TestHelper.createStoryWithSettings(false);
        Story actual = loader.fromFile("res/test_05.xml");
        Assert.assertEquals(expected, actual);
    }

    private Loader createLoader() {
        return new Loader(new XmlParser());
    }
}
