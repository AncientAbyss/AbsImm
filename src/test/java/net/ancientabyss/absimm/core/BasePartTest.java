package net.ancientabyss.absimm.core;

import org.junit.Assert;
import org.junit.Test;

public class BasePartTest {

    @Test()
    public void findAll_partsAdded_findParts() throws StoryException {
        BasePart part = new BasePart();
        part.addPart(new Part("somePart"));
        part.addPart(new Part("somePart"));
        Assert.assertEquals(2, part.findAll("somePart").size());
    }

    @Test()
    public void findAll_partsAdded_findAllParts() throws StoryException {
        BasePart part = new BasePart();
        part.addPart(new Part("somePart"));
        part.addPart(new Part("somePart"));
        part.addPart(new Part(""));
        part.addPart(new Part("x"));
        Assert.assertEquals(4, part.findAll().size());
    }
}
