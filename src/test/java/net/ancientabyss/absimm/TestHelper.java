package net.ancientabyss.absimm;

import net.ancientabyss.absimm.core.*;

import java.io.ByteArrayInputStream;

public class TestHelper {

    public static ByteArrayInputStream toStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static Story createDefaultStory(boolean useTxtFormat) {
        return new Story(new StateList(), createDefaultSettings(useTxtFormat));
    }

    private static Settings createDefaultSettings(boolean useTxtFormat) {
        Settings settings = new Settings();
        settings.addSetting("quit_command", "quit");
        settings.addSetting("hint_command", "hint");
        settings.addSetting("help_command", "help");
        settings.addSetting("save_command", "save");
        settings.addSetting("save_invalid_command", "Argument missing!");
        settings.addSetting("save_error", "Unable to save game.");
        settings.addSetting("save_success", "Game successfully saved.");
        settings.addSetting("load_command", "load");
        settings.addSetting("quit_message", "Thanks for playing, have a nice day!");
        settings.addSetting("load_invalid_command", "Argument missing!");
        settings.addSetting("load_error", "Unable to load game.");
        settings.addSetting("load_success", "Game successfully loaded.");
        settings.addSetting("invalid_command", "Invalid command!");
        settings.addSetting("empty_story_error", "Cannot tell empty story!");
        if (useTxtFormat) {
            settings.addSetting("object_error", "Nah.|Nope!");
            settings.addSetting("action_error", "Nah.|Nope!");
        } else {
            settings.addSetting("object_error", "No such object!");
            settings.addSetting("action_error", "You cannot do this with this object!");
        }
        settings.addSetting("initial_command_missing", "No initial command has been set!");
        settings.addSetting("help_message", "\\n-------------\\nType 'hint' if you are stuck and 'quit' if you want to stop playing.\\nUse 'save xx' to save your progress to the slot 'xx' and 'load xx' to load your progress from slot 'xx'.\\n'help' will bring this info up again.\\n-------------\\n");
        if (useTxtFormat) {
            settings.addSetting("initial_command", "enter main");
        }
        return settings;
    }

    public static Story createStoryWithParts() {
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory(false);
        expected.addPart(new Part("chapter_01", "in_chapter01", stateList));
        expected.addPart(new Part("chapter_02", "", stateList));
        return expected;
    }

    public static Story createStoryWithPartsTxt() {
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory(true);
        Part part = new Part("main", "NOT game_started", stateList);
        part.addAction(new Action("enter", "", "", "game_started AND in_intro", stateList, expected, "enter intro"));
        expected.addPart(part);
        part = new Part("intro", "in_intro", stateList);
        part.addAction(new Action("enter", "", "", "", stateList, expected));
        expected.addPart(part);
        part = new Part("chapter_01", "in_chapter_01", stateList);
        part.addAction(new Action("enter", "", "", "", stateList, expected));
        expected.addPart(part);
        part = new Part("chapter_02", "in_chapter_02", stateList);
        part.addAction(new Action("enter", "", "", "", stateList, expected));
        expected.addPart(part);
        return expected;
    }

    public static Story createStoryWithPartAndActions() {
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory(false);
        Part part = new Part("chapter_01", "", stateList);
        part.addAction(new Action("enter", "opened1", "has_lock1", "in_chapter01", stateList, expected));
        expected.addPart(part);
        part = new Part("chapter_02", "", stateList);
        part.addAction(new Action("enter", "opened2", "has_lock2", "in_chapter02", stateList, expected, "kill me"));
        expected.addPart(part);
        return expected;
    }

    public static Story createStoryWithPartAndActionsTxt() {
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory(true);
        Part part = new Part("main", "NOT game_started", stateList);
        part.addAction(new Action("enter", "", "", "game_started AND in_intro", stateList, expected, "enter intro"));
        expected.addPart(part);
        part = new Part("intro", "in_intro", stateList);
        part.addAction(new Action("enter", "welcome!\n- proceed", "", "", stateList, expected));
        Part dummyPart = new Part("", "", stateList);
        dummyPart.addAction(new Action("proceed", "", "", "in_chapter01 AND NOT in_intro", stateList, expected, "enter chapter01"));
        part.addPart(dummyPart);
        expected.addPart(part);
        part = new Part("chapter01", "in_chapter01", stateList);
        part.addAction(new Action("enter", "hello!", "", "", stateList, expected));
        expected.addPart(part);
        return expected;
    }

    public static Story createStoryWithPartAndActionsIncludingPeekPartsTxt() {
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory(true);
        Part part = new Part("main", "NOT game_started", stateList);
        part.addAction(new Action("enter", "", "", "game_started AND in_intro", stateList, expected, "enter intro"));
        expected.addPart(part);
        part = new Part("intro", "in_intro", stateList);
        part.addAction(new Action("enter", "welcome!\n- proceed", "", "", stateList, expected));
        Part dummyPart = new Part("", "", stateList);
        dummyPart.addAction(new Action("proceed", "", "", "in_chapter01 AND NOT in_intro", stateList, expected, "enter chapter01"));
        part.addPart(dummyPart);
        expected.addPart(part);
        part = new Part("chapter01", "in_chapter01", stateList);
        part.addAction(new Action("enter", "hello!", "", "NOT in_chapter01 AND in_intro", stateList, expected));
        expected.addPart(part);
        return expected;
    }

    public static Story createStoryWithPartAndActionsIncludingHiddenDecisionNode() {
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory(true);
        Part part = new Part("main", "NOT game_started", stateList);
        part.addAction(new Action("enter", "", "", "game_started AND in_intro", stateList, expected, "enter intro"));
        expected.addPart(part);
        part = new Part("intro", "in_intro", stateList);
        part.addAction(new Action("enter", "welcome!", "", "", stateList, expected));
        Part dummyPart = new Part("", "", stateList);
        dummyPart.addAction(new Action("proceed", "", "", "in_chapter01 AND NOT in_intro", stateList, expected, "enter chapter01"));
        part.addPart(dummyPart);
        expected.addPart(part);
        return expected;
    }

    public static Story createStoryWithPartsAndActions2(boolean includeInitialCommand) {
        StateList stateList = new StateList();
        Story expected = TestHelper.createDefaultStory(includeInitialCommand);
        expected.getSettings().addSetting("initial_command", "enter chapter_01");
        Part part = new Part("chapter_01", "", stateList);
        part.addAction(new Action("enter", "opened1", "has_lock1", "in_chapter01", stateList, expected));
        expected.addPart(part);
        part = new Part("chapter_02", "", stateList);
        part.addAction(new Action("enter", "opened2", "has_lock2", "in_chapter02", stateList, expected));
        expected.addPart(part);
        return expected;
    }

    public static Story createStoryWithSettings(boolean includeInitialCommand) {
        StateList stateList = new StateList();
        Story expected = new Story(stateList, TestHelper.createDefaultSettings(includeInitialCommand));
        expected.getSettings().addSetting("initial_command", "enter chapter_01");
        Part part = new Part("chapter_01", "", stateList);
        part.addAction(new Action("enter", "opened1", "has_lock1", "in_chapter01", stateList, expected));
        expected.addPart(part);
        part = new Part("chapter_02", "", stateList);
        part.addAction(new Action("enter", "opened2", "has_lock2", "in_chapter02", stateList, expected));
        expected.addPart(part);
        return expected;
    }
}
