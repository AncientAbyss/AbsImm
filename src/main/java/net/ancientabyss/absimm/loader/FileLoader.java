package net.ancientabyss.absimm.loader;

import net.ancientabyss.absimm.core.Story;
import net.ancientabyss.absimm.core.StoryException;
import net.ancientabyss.absimm.parser.Parser;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileLoader implements Loader {
    private Parser parser;

    public FileLoader(Parser parser) {
        this.parser = parser;
    }

    public Story load(String fileName) throws StoryException {
        File file = new File(fileName);
        try {
            return parser.fromStream(new DataInputStream(new FileInputStream(file)), true);
        } catch (Exception e) {
            throw new StoryException(e);
        }
    }
}
