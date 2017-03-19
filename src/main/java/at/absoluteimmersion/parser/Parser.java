package at.absoluteimmersion.parser;

import at.absoluteimmersion.core.Story;

import java.io.InputStream;

public interface Parser {
    Story fromStream(InputStream is, boolean load_default) throws ParserException;
}
