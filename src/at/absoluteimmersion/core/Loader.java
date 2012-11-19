package at.absoluteimmersion.core;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;

public class Loader {

    public Story fromFile(String fileName) throws StoryException {
        File file = new File(fileName);
        try {
            return fromStream(new DataInputStream(new FileInputStream(file)), true);
        } catch (Exception e) {
            throw new StoryException(e);
        }
    }

    public Story fromString(String source) throws StoryException {
        if (source.isEmpty()) throw new StoryException("Empty string cannot be loaded!");
        try {
            return fromStream(new ByteArrayInputStream(source.getBytes()), true);
        } catch (Exception e) {
            throw new StoryException(e);
        }
    }

    private Story fromStream(InputStream is, boolean load_default) throws SAXException, IOException, StoryException, ParserConfigurationException {
        Settings settings = new Settings();

        if (load_default) {
            Story default_story = fromStream(new DataInputStream(getClass().getResourceAsStream("/default_settings.xml")), false);
            settings = default_story.getSettings();
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(is);
        Node node = document.getFirstChild();
        if (!node.getNodeName().equals("story")) throw new StoryException("Root node needs to be a story!");
        StateList stateList = new StateList();
        Story story = new Story(stateList, settings);
        parseChildNodes(story, stateList, node.getChildNodes(), story);
        return story;
    }

    private static boolean isWhitespaceNode(Node n) {
        if (n.getNodeType() == Node.TEXT_NODE) {
            String val = n.getNodeValue();
            return val.trim().length() == 0;
        } else {
            return false;
        }
    }

    private void parseChildNodes(BasePart parent, StateList stateList, NodeList children, Story story) throws StoryException {
        for (int i = 0; i < children.getLength(); ++i) {
            Node n = children.item(i);
            if (isWhitespaceNode(n)) continue;
            if (n.getParentNode().getNodeName() == "story" || n.getParentNode().getNodeName() == "part") {
                if (n.getNodeName().equals("part")) {
                    String name = "";
                    Node nameNode = n.getAttributes().getNamedItem("name");
                    if (nameNode != null) name = nameNode.getNodeValue();
                    String condition = "";
                    Node conditionNode = n.getAttributes().getNamedItem("condition");
                    if (conditionNode != null) condition = conditionNode.getNodeValue();
                    Part part = new Part(name, condition, stateList);
                    parent.addPart(part);
                    parseChildNodes(part, stateList, n.getChildNodes(), story);
                } else if (n.getNodeName().equals("action")) {
                    String name = "";
                    Node nameNode = n.getAttributes().getNamedItem("name");
                    if (nameNode != null) name = nameNode.getNodeValue();
                    String condition = "";
                    Node conditionNode = n.getAttributes().getNamedItem("condition");
                    if (conditionNode != null) condition = conditionNode.getNodeValue();
                    String state = "";
                    Node stateNode = n.getAttributes().getNamedItem("state");
                    if (stateNode != null) state = stateNode.getNodeValue();
                    String text = "";
                    Node textNode = n.getAttributes().getNamedItem("text");
                    if (textNode != null) text = textNode.getNodeValue();
                    String command = "";
                    Node commandNode = n.getAttributes().getNamedItem("command");
                    if (commandNode != null) command = commandNode.getNodeValue();
                    ((Part) parent).addAction(new Action(name, text, condition, state, stateList, story, command));
                } else if (n.getNodeName().equals("settings")) {
                    parseChildNodes(null, stateList, n.getChildNodes(), story);
                } else {
                    throw new StoryException("Story can only contain part, action and settings!");
                }
            } else if (n.getParentNode().getNodeName() == "settings") {
                if (n.getNodeName().equals("setting")) {
                    String name = "";
                    Node nameNode = n.getAttributes().getNamedItem("name");
                    if (nameNode != null) name = nameNode.getNodeValue();
                    String value = "";
                    Node valueNode = n.getAttributes().getNamedItem("value");
                    if (valueNode != null) value = valueNode.getNodeValue();
                    story.getSettings().addSetting(name, value);
                } else {
                    throw new StoryException("Settings can only contain setting!");
                }
            }

        }
    }
}
