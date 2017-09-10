package net.ancientabyss.absimm.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StringUtilsTest {
    @Test
    public void join_emptyList_shouldReturnEmtpyString() {
        List<String> list = new ArrayList<>();
        String actual = StringUtils.join(list, ", ");
        Assert.assertEquals("", actual);
    }

    @Test
    public void join_oneElement_shouldReturnThisElement() {
        List<String> list = new ArrayList<>();
        list.add("test");
        String actual = StringUtils.join(list, ", ");
        Assert.assertEquals("test", actual);
    }

    @Test
    public void join_multipleElements_shouldReturnElementsSeparatedUsingSeparator() {
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        String actual = StringUtils.join(list, ", ");
        Assert.assertEquals("hello, world", actual);
    }
}
