package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Properties;

import org.tsugi.base.BaseSettings;

public class TsugiSettingsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSystemProps() {
        BaseSettings settings = new BaseSettings();
        String json = null;
        Properties props = null;

        // Check emptyness
        String emptyJson = settings.getSettingsJson();
        assertEquals(emptyJson,"{}");
        props = settings.getSettings();
        assertEquals(props.size(),0);

        // Make sure to catch syntax errors
        json = "parse this!";
        try {
            settings.setSettingsJson(json);
            System.out.println("Did not fail on bad JSON");
            System.out.println(json);
            assertTrue(false);
        } catch (Exception e) {
            // As per expectations
        }

        // Make sure to ignore sub objects
        json = "{ \"x\" : \"y\", \"z\" : { \"a\": \"b\" } }";
        try {
            settings.setSettingsJson(json);
        } catch (Exception e) {
            System.out.println("Unexpected error parsing JSON");
            System.out.println(json);
            e.printStackTrace();
            assertTrue(false);
        }
        props = settings.getSettings();
        assertEquals(props.size(),1);
        assertEquals(props.getProperty("x"), "y");
        assertNull(props.getProperty("y"));
        assertNull(props.getProperty("z"));
        assertNull(props.getProperty("a"));
        assertNull(props.getProperty("b"));

        // Make sure to ignore an array
        json = "[ \"a\", \"b\", \"c\"]";
        try {
            settings.setSettingsJson(json);
        } catch (Exception e) {
            System.out.println("Unexpected error parsing JSON");
            System.out.println(json);
            e.printStackTrace();
            assertTrue(false);
        }
        props = settings.getSettings();
        assertNull(props.getProperty("a"));
        assertNull(props.getProperty("b"));
        assertNull(props.getProperty("c"));

        // Normal case
        json = "{ \"x\" : \"y\" }";
        try {
            settings.setSettingsJson(json);
        } catch (Exception e) {
            System.out.println("Unexpected error parsing JSON");
            System.out.println(json);
            e.printStackTrace();
            assertTrue(false);
        }
        props = settings.getSettings();
        assertEquals(props.size(),1);
        assertEquals(props.getProperty("x"), "y");

        // Pull back the settings
        String newJson = settings.getSettingsJson();
        assertEquals(newJson,"{\"x\":\"y\"}");


    }
    
}
