package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.core.DefaultStory;

import java.io.InputStream;

/**
 * Parser are used by the Loader to handle the available story file formats.
 */
public interface Parser {
    DefaultStory fromStream(InputStream is, boolean load_default) throws ParserException;
}
