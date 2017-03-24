package net.ancientabyss.absimm.parser;

import net.ancientabyss.absimm.core.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
        Story story = null;
        try {
            story = parseTxt(is, settings);
        } catch (ParserConfigurationException | SAXException | IOException | StoryException e) {
            throw new ParserException(e);
        }
        return story;
    }

    private Story parseTxt(InputStream is, Settings settings) throws ParserConfigurationException, SAXException, IOException, ParserException, StoryException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StateList stateList = new StateList();
        Story story = new Story(stateList, settings);
        boolean isMainPart = true;
        Part part = null;
        String text = "";
        while (reader.ready()) {
            String line = reader.readLine().trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("settings:")) parseSettings(reader, story);
            boolean isNewPart = line.endsWith(":") && !line.contains(" ");
            if (isMainPart || isNewPart) {
                // the story starts with a part, so create a dummy main part
                // which is needed to initiate the story (initial_command=enter main)
                if (isNewPart && isMainPart) {
                    part = new Part("main", "", stateList);
                    isMainPart = false;
                }
                // add the previous part
                if (part != null) {
                    addPart(stateList, story, part, text);
                    text = "";
                }
                // add a main part if the story does not start with a part
                if (isMainPart) {
                    part = new Part("main", "", stateList);
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
                    part.addAction(new Action(actionName, "", "", "in_" + actionName, stateList, story));
                } else {
                    // handle action texts
                    text += (text.isEmpty() ? "" : "\n") + line;
                }
            }
        }
        addPart(stateList, story, part, text);
        return story;
    }

    private void addPart(StateList stateList, Story story, Part part, String text) {
        part.addAction(new Action("enter", text, "", "", stateList, story, ""));
        story.addPart(part);
    }

    private void parseSettings(BufferedReader reader, Story story) throws IOException {
        while (reader.ready()) {
            String line = reader.readLine().trim();
            if (line.isEmpty()) return; // done with settings, move on
            int keyLength = line.indexOf("=");
            String key = line.substring(0, keyLength);
            String value = line.substring(keyLength + 1);
            story.getSettings().addSetting(key, value);
        }
    }
}
