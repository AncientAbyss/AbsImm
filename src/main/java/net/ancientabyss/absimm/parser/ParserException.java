package net.ancientabyss.absimm.parser;

public class ParserException extends Exception {
    public ParserException(String message) {
        super(message);
    }

    public ParserException(Exception e) {
        super(e);
    }
}
