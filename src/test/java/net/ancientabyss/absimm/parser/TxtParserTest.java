package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.TestHelper;
import net.ancientabyss.absimm.core.DefaultStory;
import net.ancientabyss.absimm.core.ReactionClient;
import net.ancientabyss.absimm.core.Story;
import net.ancientabyss.absimm.core.StoryException;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        DefaultStory actual = parser.fromStream(TestHelper.toStream("\nchapter_01:\nchapter_02:"), false);
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
        DefaultStory actual = parser.fromStream(TestHelper.toStream("settings:\nhelp_message=" + helpMessage + "\n\nwelcome!"), true);
        Assert.assertEquals(helpMessage, actual.getSettings().getSetting("help_message"));
    }

    @Test
    public void fromStream_includesSetting_shouldKeepStoryIntact() throws ParserException, StoryException {
        Parser parser = createParser();
        String helpMessage = "Try harder!";
        Story actual = parser.fromStream(TestHelper.toStream("settings:\nhelp_message=" + helpMessage + "\n\nwelcome!"), true);
        ReactionClient client = mock(ReactionClient.class);
        actual.addClient(client);
        actual.tell();
        verify(client).onReact("welcome!");
    }

    @Test
    public void fromStream_storyWithPartsAndHiddenDecisionNode_createsStory() throws ParserException {
        Parser parser = createParser();
        Story expected = TestHelper.createStoryWithPartAndActionsIncludingHiddenDecisionNode();
        Story actual = parser.fromStream(TestHelper.toStream("welcome!\n\n- proceed (chapter01)"), true);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = ParserException.class)
    public void fromStream_malformedDecisionNode_throwsException() throws ParserException, StoryException {
        Parser parser = createParser();
        parser.fromStream(TestHelper.toStream("welcome!\n\n- proceed (chapter01) mep"), true);
    }

    @Test
    public void fromStream_storyfileWithComments_createsStoryWithoutComments() throws ParserException, StoryException {
        Parser parser = createParser();
        String chapter1Text = "hello!\n# c3 \n\nworld";
        Story actual = parser.fromStream(TestHelper.toStream("welcome!\n\n#c1\n\n* proceed (chapter01)\n# c2\nchapter01:\n" + chapter1Text), true);
        ReactionClient client = mock(ReactionClient.class);
        actual.addClient(client);
        actual.tell();
        verify(client).onReact("welcome!\n- proceed");
        actual.interact("proceed");
        verify(client).onReact("hello!\nworld");
    }

    protected Parser createParser() {
        return new TxtParser();
    }
}
