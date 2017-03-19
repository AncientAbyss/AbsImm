package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.core.Story;

import java.io.InputStream;

public interface Parser {
    Story fromStream(InputStream is, boolean load_default) throws ParserException;
}
