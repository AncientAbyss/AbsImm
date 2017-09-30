package net.ancientabyss.absimm.loader;

import net.ancientabyss.absimm.core.Story;
import net.ancientabyss.absimm.core.StoryException;

/**
 * Use the Loader to initialize a story from an AbsImm storyfile.
 * Loader make use of a Parser to interpret the story file.
 */
public interface Loader {
    Story load(String fileName) throws StoryException;
}
