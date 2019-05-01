package com.mizo0203.twitter.timeline.talker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TwitterTimelineTalkerTest {

    @Test
    public void getUserNameWithoutContext_01() {
        String expected = TwitterTimelineTalker.getUserNameWithoutContext("みぞ@ユニットテスト");
        String actual = "みぞ";
        assertEquals(expected, actual);
    }

    @Test
    public void getUserNameWithoutContext_02() {
        String expected = TwitterTimelineTalker.getUserNameWithoutContext("みぞ＠ユニットテスト");
        String actual = "みぞ";
        assertEquals(expected, actual);
    }

    @Test
    public void getUserNameWithoutContext_03() {
        String expected = TwitterTimelineTalker.getUserNameWithoutContext("みぞ");
        String actual = "みぞ";
        assertEquals(expected, actual);
    }

    @Test
    public void getUserNameWithoutContext_04() {
        String expected = TwitterTimelineTalker.getUserNameWithoutContext("@みぞ");
        String actual = "@みぞ";
        assertEquals(expected, actual);
    }

    @Test
    public void getUserNameWithoutContext_05() {
        String expected = TwitterTimelineTalker.getUserNameWithoutContext("＠みぞ");
        String actual = "＠みぞ";
        assertEquals(expected, actual);
    }
}
