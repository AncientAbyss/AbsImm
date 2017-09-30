package net.ancientabyss.absimm.loader;

import net.ancientabyss.absimm.core.Story;
import net.ancientabyss.absimm.core.StoryException;
import net.ancientabyss.absimm.parser.Parser;

import java.io.ByteArrayInputStream;

public class StringLoader implements Loader {
    private Parser parser;

    public StringLoader(Parser parser) {
        this.parser = parser;
    }

    public Story load(String source) throws StoryException {
        if (source.isEmpty()) throw new StoryException("Empty string cannot be loaded!");
        try {
            return parser.fromStream(new ByteArrayInputStream(source.getBytes()), true);
        } catch (Exception e) {
            throw new StoryException(e);
        }
    }
}
