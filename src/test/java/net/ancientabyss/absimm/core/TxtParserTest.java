package net.ancientabyss.absimm.core;

import net.ancientabyss.absimm.parser.TxtParser;
import org.junit.Assert;
import org.junit.Test;

public class TxtParserTest {

    @Test(expected = StoryException.class)
    public void fromString_emptyString_throwsException() throws StoryException {
        Loader loader = createLoader();
        loader.fromString("");
    }

    @Test
    public void fromString_storyWithParts_createsStoryWithParts() throws StoryException {
        Loader loader = createLoader();
        Story expected = TestHelper.createStoryWithPartsTxt();
        Story actual = loader.fromString("\nchapter_01:\nchapter_02:");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fromString_storyWithPartsAndActions_createsStoryWithPartsAndActions() throws StoryException {
        Loader loader = createLoader();
        Story expected = TestHelper.createStoryWithPartAndActionsTxt();
        Story actual = loader.fromString("welcome!\n* proceed (chapter01)\nchapter01:\nhello!");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fromString_storyWithPartsAndPeekActions_createsStoryWithPartsAndPeekParts() throws StoryException {
        Loader loader = createLoader();
        Story expected = TestHelper.createStoryWithPartAndActionsIncludingPeekPartsTxt();
        Story actual = loader.fromString("welcome!\n* proceed (chapter01)\nchapter01:\nhello!\n<<\n\n");
        Assert.assertEquals(expected, actual);
    }

    private Loader createLoader() {
        return new Loader(new TxtParser());
    }
}
