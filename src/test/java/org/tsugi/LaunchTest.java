package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.tsugi.Launch;

public class LaunchTest {

    Launch launch = null;

    @Before
    public void setUp() throws Exception {
        launch = new org.tsugi.impl.jdbc.Launch_JDBC();
    }
    
    @Test
    public void testImpl() {
        System.out.println("YOYO "+launch.customGet("a","b"));
    }

}
