package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.core.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class TxtParser implements Parser {
    @Override
    public Story fromStream(InputStream is, boolean load_default) throws ParserException {
        Settings settings = new Settings();

        if (load_default) {
            // TODO: check if exists
            Story default_story = fromStream(new DataInputStream(getClass().getResourceAsStream("/default_settings.txt")), false);
            settings = default_story.getSettings();
        }
        Story story;
        try {
            story = parseTxt(is, settings);
        } catch (IOException e) {
            throw new ParserException(e);
        }
        return story;
    }

    private Story parseTxt(InputStream is, Settings settings) throws IOException  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StateList stateList = new StateList();
        Story story = new Story(stateList, settings);
        boolean isMainPart = true;
        Part part = null;
        StringBuilder text = new StringBuilder();
        boolean isPeekPart = false;
        while (reader.ready()) {
            String line = reader.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("settings:")) parseSettings(reader, story);
            boolean isNewPart = line.endsWith(":") && !line.contains(" ");
            if (isMainPart || isNewPart) {
                // the story starts with a part, so create a dummy main part
                // which is needed to initiate the story (initial_command=enter main)
                if (isNewPart && isMainPart) {
                    part = createMainPart(stateList);
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
                    part = createMainPart(stateList);
                    isMainPart = false;
                } else {
                    // add parts
                    String name = StringUtils.removeEnd(line, ":");
                    part = new Part(name, "in_" + name, stateList);
                }
            }
            if (!isNewPart) {
                // handle actions
                if (line.startsWith("* ")) {
                    String action = StringUtils.removeStart(line, "*").trim();
                    int keyLength = action.indexOf("(");
                    String actionLabel = action.substring(0, keyLength).trim();
                    String actionName = StringUtils.stripEnd(action.substring(keyLength + 1).trim(), ")");
                    Part dummyPart = new Part("", "", stateList);
                    String state = "in_" + actionName + (part.getName().equals("main") ? " AND started" : "");
                    dummyPart.addAction(new Action(actionLabel, "", "", state, stateList, story, "enter " + actionName));
                    part.addPart(dummyPart);
                    appendText(text, "- " + actionLabel);
                } else if (line.startsWith("<<")) {
                    isPeekPart = true;
                } else {
                    // handle action texts
                    appendText(text, line);
                }
            }
        }
        addPart(stateList, story, part, text.toString(), isPeekPart);
        return story;
    }

    private Part createMainPart(StateList stateList) {
        return new Part("main", "NOT started", stateList);
    }

    private void appendText(StringBuilder text, String line) {
        text.append((text.length() == 0) ? "" : "\n").append(line);
    }

    private void addPart(StateList stateList, Story story, Part part, String text, boolean isPeekPart) {
        part.addAction(new Action("enter", text, "", isPeekPart ? (BasePart.NOT + " " + part.getCondition()) : "", stateList, story, ""));
        story.addPart(part);
    }

    private void parseSettings(BufferedReader reader, Story story) throws IOException {
        while (reader.ready()) {
            String line = reader.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) return; // done with settings, move on
            int keyLength = line.indexOf("=");
            String key = line.substring(0, keyLength);
            String value = line.substring(keyLength + 1);
            story.getSettings().addSetting(key, value);
        }
    }
}
