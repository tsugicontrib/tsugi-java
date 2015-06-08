package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Properties;

import org.tsugi.util.TsugiUtils;

public class TsugiUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSystemProps() {
        String tsugiDataSource = System.getProperty("tsugi.datasource.url");
        System.out.println("System property tsugi.datasource.url="+tsugiDataSource);
    }
    
    @Test
    public void testUtils() {
        Properties props = TsugiUtils.loadProperties("/tsugi.properties");
        System.out.println("Properties loaded "+props);
    }

}
