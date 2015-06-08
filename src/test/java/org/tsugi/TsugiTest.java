package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.tsugi.Tsugi;

public class TsugiTest {

    Tsugi tsugi = null;

    @Before
    public void setUp() throws Exception {
        tsugi = new org.tsugi.impl.jdbc.Tsugi_JDBC();
    }
    
    @Test
    public void testImpl() {
        System.out.println("YOYO "+tsugi.getVersion());
    }

}
