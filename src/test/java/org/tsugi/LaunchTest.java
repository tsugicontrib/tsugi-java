package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.tsugi.Launch;

public class LaunchTest {

    Launch launch = null;

    @Before
    public void setUp() throws Exception {
        try {
            launch = new org.tsugi.impl.jdbc.Launch_JDBC();
        } catch(Exception e) {
            System.out.println("Unable to setup LaunchTest - test stopped");
            launch = null;
        }
    }
    
    @Test
    public void testImpl() {
        if ( launch == null ) {
            assertTrue(false);
            return;
        }
        System.out.println("YOYO "+launch.customGet("a","b"));
    }

}
