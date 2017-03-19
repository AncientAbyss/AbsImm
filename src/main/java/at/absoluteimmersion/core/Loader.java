package at.absoluteimmersion.core;

import at.absoluteimmersion.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Loader {
    Parser parser;

    public Loader(Parser parser) {
        this.parser = parser;
    }

    public Story fromFile(String fileName) throws StoryException {
        File file = new File(fileName);
        try {
            return parser.fromStream(new DataInputStream(new FileInputStream(file)), true);
        } catch (Exception e) {
            throw new StoryException(e);
        }
    }

    public Story fromString(String source) throws StoryException {
        if (source.isEmpty()) throw new StoryException("Empty string cannot be loaded!");
        try {
            return parser.fromStream(new ByteArrayInputStream(source.getBytes()), true);
        } catch (Exception e) {
            throw new StoryException(e);
        }
    }
}
