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

public class StringLoaderTest {
    @Test
    public void load_validString_invokesParser() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        Loader loader = createLoader(parser);
        loader.load("a very exciting story");
        Mockito.verify(parser).fromStream(any(InputStream.class), anyBoolean());
    }

    @Test(expected = StoryException.class)
    public void load_emptyString_throwsException() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        Loader loader = createLoader(parser);
        loader.load("");
        Mockito.verify(parser).fromStream(any(InputStream.class), anyBoolean());
    }

    @Test(expected = StoryException.class)
    public void load_corruptStory_throwsException() throws StoryException, ParserException {
        Parser parser = Mockito.mock(Parser.class);
        when(parser.fromStream(any(InputStream.class), anyBoolean())).thenThrow(new ParserException("Invalid story."));
        StringLoader loader = (StringLoader) createLoader(parser);
        loader.load("a very exciting story");
    }


    private Loader createLoader(Parser parser){
        return new StringLoader(parser);
    }
}
