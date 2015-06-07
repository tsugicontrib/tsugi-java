package org.tsugi.util;

import java.util.Properties;

import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class TsugiUtils {

    private static Log log = LogFactory.getLog(TsugiUtils.class);

    public static Properties loadProperties(String filename)
    {

        Properties prop = new Properties();
        InputStream in = TsugiUtils.class.getResourceAsStream(filename);
        if ( in == null ) {
            log.error("Properties file not found: "+filename);
            return null;
        } else {
            try {
                prop.load(in);
                in.close();
                log.error("Loaded "+prop.size()+" properties from: "+filename);
                return prop;
            } catch (IOException ex) {
                log.warn("Failed loading properties from: "+filename,ex);
                return null;
            }
        }
    }

}

