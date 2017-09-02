package net.ancientabyss.absimm.core;

import net.ancientabyss.absimm.parser.Parser;
import net.ancientabyss.absimm.parser.ParserException;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

public class LoaderTest {

    @Test
    public void fromFile_validFile_invokesParser() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        Loader loader = createLoader(parser);
        loader.fromFile("res/test_01.xml");
        Mockito.verify(parser).fromStream(any(InputStream.class), anyBoolean());
    }

    @Test(expected = StoryException.class)
    public void fromFile_nonexistentFile_throwsException() throws StoryException {
        Parser parser = Mockito.mock(Parser.class);
        Loader loader = createLoader(parser);
        loader.fromFile("i/do/not/exist");
    }

    @Test(expected = StoryException.class)
    public void fromFile_corruptStory_throwsException() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        when(parser.fromStream(any(InputStream.class), anyBoolean())).thenThrow(new ParserException("Invalid story."));
        Loader loader = createLoader(parser);
        loader.fromFile("res/test_01.xml");
    }

    @Test
    public void fromString_validString_invokesParser() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        Loader loader = createLoader(parser);
        loader.fromString("a very exciting story");
        Mockito.verify(parser).fromStream(any(InputStream.class), anyBoolean());
    }

    @Test(expected = StoryException.class)
    public void fromString_emptyString_throwsException() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        Loader loader = createLoader(parser);
        loader.fromString("");
        Mockito.verify(parser).fromStream(any(InputStream.class), anyBoolean());
    }

    @Test(expected = StoryException.class)
    public void fromString_corruptStory_throwsException() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        when(parser.fromStream(any(InputStream.class), anyBoolean())).thenThrow(new ParserException("Invalid story."));
        Loader loader = createLoader(parser);
        loader.fromString("a very exciting story");
    }


    private Loader createLoader(Parser parser) {
        return new Loader(parser);
    }
}
