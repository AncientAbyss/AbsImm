package net.ancientabyss.absimm.loader;

import net.ancientabyss.absimm.core.StoryException;
import net.ancientabyss.absimm.parser.Parser;
import net.ancientabyss.absimm.parser.ParserException;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

public class FileLoaderTest {

    @Test
    public void load_validFile_invokesParser() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        Loader loader = createLoader(parser);
        loader.load("res/test_01.xml");
        Mockito.verify(parser).fromStream(any(InputStream.class), anyBoolean());
    }

    @Test(expected = StoryException.class)
    public void load_nonexistentFile_throwsException() throws StoryException {
        Parser parser = Mockito.mock(Parser.class);
        Loader loader = createLoader(parser);
        loader.load("i/do/not/exist");
    }

    @Test(expected = StoryException.class)
    public void load_corruptStory_throwsException() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        when(parser.fromStream(any(InputStream.class), anyBoolean())).thenThrow(new ParserException("Invalid story."));
        Loader loader = createLoader(parser);
        loader.load("res/test_01.xml");
    }

    private Loader createLoader(Parser parser) {
        return new FileLoader(parser);
    }
}
