package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.tsugi.Tsugi;
import org.tsugi.TsugiFactory;

import java.sql.Connection;

public class TsugiTest {

    Tsugi tsugi = null;

    @Before
    public void setUp() throws Exception {
        tsugi = TsugiFactory.getTsugi();
    }
    
    @Test
    public void testVersion() {
        if ( tsugi == null ) {
            assertTrue(false);
            return;
        }
        System.out.println("YOYO "+tsugi.getVersion());
    }

    @Test
    public void testImpl() {
        if ( tsugi == null ) return;
        Connection c = tsugi.getConnection();
        System.out.println("tsugi.getConnection="+c);
    }

}
