package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.core.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TxtParser implements Parser {

    private static final String commentPrefix = "#";

    private Map<String, String> parentParts = new HashMap<>();

    @Override
    public DefaultStory fromStream(InputStream is, boolean loadDefault) throws ParserException {
        Settings settings = new Settings();

        if (loadDefault) {
            // TODO: check if exists
            DefaultStory default_story = fromStream(new DataInputStream(getClass().getResourceAsStream("/default_settings.txt")), false);
            settings = default_story.getSettings();
        }
        DefaultStory story;
        try {
            story = parseTxt(is, settings);
        } catch (IOException e) {
            throw new ParserException(e);
        }
        return story;
    }

    private DefaultStory parseTxt(InputStream is, Settings settings) throws IOException, ParserException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StateList stateList = new StateList();
        DefaultStory story = new DefaultStory(stateList, settings);
        Part mainPart = new Part("main", "NOT game_started", stateList);
        mainPart.addAction(new Action("enter", "", "", "game_started AND in_intro", stateList, story, "enter intro"));
        story.addPart(mainPart);
        boolean isMainPart = true;
        int numEmptyLinesToAdd = 0;
        Part part = null;
        StringBuilder text = new StringBuilder();
        boolean isPeekPart = false;
        boolean containsSettings = false;
        boolean isComment = false;
        while (reader.ready()) {
            String line = reader.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) {
                ++numEmptyLinesToAdd;
                continue;
            }
            if (line.startsWith(commentPrefix)) {
                isComment = true;
                continue;
            }
            if (line.startsWith("settings:")) {
                parseSettings(reader, story);
                containsSettings = true;
                continue;
            }
            boolean isNewPart = line.endsWith(":") && !line.contains(" ");
            if (isMainPart || isNewPart) {
                // the story starts with a part, so create a dummy main part
                // which is needed to initiate the story (initial_command=enter main)
                if (isNewPart && isMainPart) {
                    part = createPart("intro", stateList);
                    isMainPart = false;
                }
                // add the previous part
                if (part != null) {
                    addPart(stateList, story, part, text.toString(), isPeekPart);
                    text = new StringBuilder();
                    isPeekPart = false;
                }
                // add a main part if the story does not start with a part
                if (isMainPart) {
                    part = createPart("intro", stateList);
                    isMainPart = false;
                } else {
                    // add parts
                    String name = StringUtils.removeEnd(line, ":");
                    part = createPart(name, stateList);
                }
                numEmptyLinesToAdd = 0;
            }
            if (!isNewPart) {
                if (numEmptyLinesToAdd > 0) {
                    if (line.startsWith("- ")) numEmptyLinesToAdd = 0; // strip whitespace between section and hidden decision node
                    if (isComment) {
                        numEmptyLinesToAdd = 0;
                        isComment = false;
                    }
                    for (int i = 0; i < numEmptyLinesToAdd; ++i) {
                        appendText(text, "");
                    }
                    numEmptyLinesToAdd = 0;
                }
                // handle actions
                if (line.startsWith("* ") || line.startsWith("- ")) {
                    String action = StringUtils.removeStart(StringUtils.removeStart(line, "*"), "-").trim();
                    int keyLength = action.indexOf("(");
                    String actionLabel = action.substring(0, keyLength).trim();
                    String actionName = action.substring(keyLength + 1).trim();
                    if (!action.endsWith(")")) throw new ParserException("Decision node needs to end with ')': " + line);
                    actionName = StringUtils.stripEnd(actionName, ")");
                    Part dummyPart = new Part("", "", stateList);
                    String state = String.format("in_%s %s %s in_%s", actionName, StateList.AND, StateList.NOT, part.getName());
                    dummyPart.addAction(new Action(actionLabel, "", "", state, stateList, story, "enter " + actionName));
                    part.addPart(dummyPart);
                    parentParts.put(actionName, part.getName());
                    if (!line.startsWith("- ")) {
                        appendText(text, "- " + actionLabel);
                    }
                } else if (line.startsWith("<<")) {
                    isPeekPart = true;
                } else {
                    // handle action texts
                    appendText(text, line);
                }
            }
        }
        if (part != null || !containsSettings) { // can be null for e.g. default_settings.txt
            addPart(stateList, story, part, text.toString(), isPeekPart);
        }
        return story;
    }

    private Part createPart(String name, StateList stateList) {
        return new Part(name,"in_" + name, stateList);
    }

    private void appendText(StringBuilder text, String line) {
        text.append((text.length() == 0) ? "" : "\n").append(line);
    }

    private void addPart(StateList stateList, DefaultStory story, Part part, String text, boolean isPeekPart) throws ParserException {
        if (part == null) throw new ParserException("Attempted adding an empty part.");
        part.addAction(new Action("enter", text, "", isPeekPart ?
                String.format("%s in_%s %s in_%s", StateList.NOT, part.getName(), StateList.AND, parentParts.get(part.getName())) : "", stateList, story, ""));
        story.addPart(part);
    }

    private void parseSettings(BufferedReader reader, DefaultStory story) throws IOException {
        while (reader.ready()) {
            String line = reader.readLine();
            if (line == null) return;
            line = line.trim();
            if (line.isEmpty()) return; // done with settings, move on
            int keyLength = line.indexOf("=");
            String key = line.substring(0, keyLength);
            String value = line.substring(keyLength + 1);
            story.getSettings().addSetting(key, value);
        }
    }
}
