package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.TestHelper;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.when;

public abstract class ParserTestBase {

    @Test(expected = ParserException.class)
    public void fromString_emptyString_throwsException() throws ParserException {
        Parser parser = createParser();
        parser.fromStream(TestHelper.toStream(""), true);
    }

    @Test(expected = ParserException.class)
    public void fromFile_invalidStream_throwsException() throws IOException, ParserException {
        InputStream in = Mockito.mock(InputStream.class);
        when(in.available()).thenReturn(1);
        Parser parser = createParser();
        parser.fromStream(in, true);
    }

    protected abstract Parser createParser();
}
