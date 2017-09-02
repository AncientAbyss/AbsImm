package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.TestHelper;
import net.ancientabyss.absimm.core.Story;
import org.junit.Assert;
import org.junit.Test;

public class TxtParserTest extends ParserTestBase {

    @Test
    public void fromStream_storyWithParts_createsStoryWithParts() throws ParserException {
        Parser parser = createParser();
        Story expected = TestHelper.createStoryWithPartsTxt();
        Story actual = parser.fromStream(TestHelper.toStream("\nchapter_01:\nchapter_02:"), true);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void fromStream_withoutSettings_createsStoryWithoutSettings() throws ParserException {
        Parser parser = createParser();
        Story actual = parser.fromStream(TestHelper.toStream("\nchapter_01:\nchapter_02:"), false);
        Assert.assertNull(actual.getSettings().getSetting("help_message"));
    }

    @Test
    public void fromStream_storyWithPartsAndActions_createsStoryWithPartsAndActions() throws ParserException {
        Parser parser = createParser();
        Story expected = TestHelper.createStoryWithPartAndActionsTxt();
        Story actual = parser.fromStream(TestHelper.toStream("welcome!\n* proceed (chapter01)\nchapter01:\nhello!"), true);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void fromStream_storyWithPartsAndPeekActions_createsStoryWithPartsAndPeekParts() throws ParserException {
        Parser parser = createParser();
        Story expected = TestHelper.createStoryWithPartAndActionsIncludingPeekPartsTxt();
        Story actual = parser.fromStream(TestHelper.toStream("welcome!\n* proceed (chapter01)\nchapter01:\nhello!\n<<\n\n"), true);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void fromStream_storyWithPartsAndActions_createsStoryPreservingEmptyLines() throws ParserException {
        Parser parser = createParser();
        String chapter1Text = "hello!\n\nworld";
        Story actual = parser.fromStream(TestHelper.toStream("welcome!\n* proceed (chapter01)\nchapter01:\n" + chapter1Text), true);
        Assert.assertTrue(actual.toString().contains(chapter1Text));
    }

    @Test
    public void fromStream_includesSetting_shouldOverwriteSetting() throws ParserException {
        Parser parser = createParser();
        String helpMessage = "Try harder!";
        Story actual = parser.fromStream(TestHelper.toStream("settings:\nhelp_message=" + helpMessage + "\n\nwelcome!"), true);
        Assert.assertEquals(helpMessage, actual.getSettings().getSetting("help_message"));
    }

    protected Parser createParser() {
        return new TxtParser();
    }
}
