package at.absoluteimmersion.core;

import junit.framework.Assert;
import org.junit.Test;

public class BasePartTest {
    @Test(expected = StoryException.class)
    public void find_noPartsAdded_throwsException() throws StoryException {
        BasePart part = new BasePart();
        part.find("somePart");
    }

    @Test()
    public void find_partsAdded_findsPart() throws StoryException {
        BasePart part = new BasePart();
        Part somePart = new Part("somePart");
        part.addPart(somePart);
        Assert.assertEquals(somePart, part.find("somePart"));
    }

}
