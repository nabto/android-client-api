package com.nabto.api;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class NabtoCApiWrapperStubControllerTest {

    @Test
    public void mapToStringHelperTest() {
        Map values = new HashMap<String, String>();
        values.put("a", "1");
        values.put("ab", "12");
        values.put("ab0", "12a");

        String str = NabtoCApiWrapperStubController.mapToString(values);

        assertEquals(17, str.length());
        assertThat(str, containsString("a=1"));
        assertThat(str, containsString("ab=12"));
        assertThat(str, containsString("ab0=12a"));
    }

    @Test
    public void stringToMapHelperTest() {
        String str = "a=1,ab=12,ab0=12a";

        Map values = NabtoCApiWrapperStubController.stringToMap(str);

        assertTrue(values.containsKey("a"));
        assertEquals("1", values.get("a"));

        assertTrue(values.containsKey("ab"));
        assertEquals("12", values.get("ab"));

        assertTrue(values.containsKey("ab0"));
        assertEquals("12a", values.get("ab0"));
    }
}