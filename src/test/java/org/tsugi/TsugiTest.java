package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.tsugi.Tsugi;
import org.tsugi.impl.Tsugi_JDBC;


public class TsugiTest {

    Tsugi tsugi = null;

    @Before
    public void setUp() throws Exception {
        tsugi = new Tsugi_JDBC();
    }
    
    @Test
    public void testImpl() {
        System.out.println("YOYO "+tsugi.getVersion());
    }

}
