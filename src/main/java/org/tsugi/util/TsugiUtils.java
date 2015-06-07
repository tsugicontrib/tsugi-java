package org.tsugi.util;

import java.util.Properties;
import java.util.Enumeration;
import java.lang.StringBuffer;

import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TsugiUtils {

    private static Log log = LogFactory.getLog(TsugiUtils.class);

    public static Properties loadProperties(String filename)
    {

        Properties prop = new Properties();
        InputStream in = TsugiUtils.class.getResourceAsStream(filename);
        if ( in == null ) {
            log.debug("Properties file not found: "+filename);
            return null;
        } else {
            try {
                prop.load(in);
                in.close();
                log.debug("Loaded "+prop.size()+" properties from: "+filename);
                return prop;
            } catch (IOException ex) {
                log.warn("Failed loading properties from: "+filename,ex);
                return null;
            }
        }
    }

    public static String dumpProperties(Properties p)
    {
        StringBuffer sb = new StringBuffer();

        Enumeration keys = p.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = (String)p.get(key);
            if ( sb.length() > 0 ) sb.append("\n");
            sb.append(key);
            sb.append(": ");
            sb.append(value);
        }
        return sb.toString();
    }

}

