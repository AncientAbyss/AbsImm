package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.TestHelper;
import net.ancientabyss.absimm.core.DefaultStory;
import net.ancientabyss.absimm.core.Story;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XmlParserTest extends ParserTestBase {

    @Test
    public void fromStream_storyOnly_createsStory() throws ParserException {
        Parser parser = createParser();
        Story expected = TestHelper.createDefaultStory(false);
        Story actual = parser.fromStream(TestHelper.toStream("<story></story>"), true);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void fromStream_withoutSettings_createsStoryWithoutSettings() throws ParserException {
        Parser parser = createParser();
        DefaultStory story = parser.fromStream(TestHelper.toStream("<story></story>"), false);
        Assert.assertNull(story.getSettings().getSetting("help_message"));
    }

    @Test(expected = ParserException.class)
    public void fromStream_invalidElementAsChildOfStory_throwsException() throws ParserException, FileNotFoundException {
        Parser parser = createParser();
        Story story = parser.fromStream(new FileInputStream("res/test_07.xml"), true);
        Assert.assertEquals(TestHelper.createDefaultStory(false), story);
    }

    @Test(expected = ParserException.class)
    public void fromStream_invalidElementAsChildOfSettings_throwsException() throws ParserException, FileNotFoundException {
        Parser parser = createParser();
        Story story = parser.fromStream(new FileInputStream("res/test_08.xml"), true);
        Assert.assertEquals(TestHelper.createDefaultStory(false), story);
    }

    @Test
    public void fromStream_storyWithParts_createsStoryWithParts() throws ParserException {
        Parser parser = createParser();
        Story expected = TestHelper.createStoryWithParts();
        Story actual = parser.fromStream(TestHelper.toStream("<story><part name='chapter_01' condition='in_chapter01'/><part name='chapter_02'/></story>"), true);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
     public void fromStream_storyWithPartsAndActions_createsStoryWithPartsAndActions() throws ParserException, FileNotFoundException {
        Parser parser = createParser();
        Story expected = TestHelper.createStoryWithPartsAndActions2(false);
        Story actual = parser.fromStream(new FileInputStream("res/test_01.xml"), true);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void fromStream_storySettings_createsStoryWithSettings() throws ParserException, FileNotFoundException {
        Parser parser = createParser();
        Story expected = TestHelper.createStoryWithSettings(false);
        Story actual = parser.fromStream(new FileInputStream("res/test_05.xml"), true);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    protected XmlParser createParser() {
        return new XmlParser();
    }
}
