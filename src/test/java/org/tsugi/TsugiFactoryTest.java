package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import org.tsugi.Tsugi;
import org.tsugi.TsugiFactory;


public class TsugiFactoryTest {

    Tsugi tsugi = TsugiFactory.getTsugi();

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testImpl() {
        System.out.println("YOYO "+tsugi.getVersion());
    }

}
