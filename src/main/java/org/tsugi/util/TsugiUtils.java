package org.tsugi.util;

import java.util.Properties;
import java.util.Enumeration;
import java.lang.StringBuffer;

import java.io.InputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities to avoid repeating ourselves.
 */
public class TsugiUtils {

    private static Log log = LogFactory.getLog(TsugiUtils.class);

    /**
     * Convienence method Compute the sha256 for a string
     *
     * TODO: Elminate this and just import commons.codec where appropriate.
     */
    public static String sha256(String input) 
    {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
    }

    /*
     * Indicate whether we are in the midst of unit tests.
     *
     * This is controlled by:
     *
     *   tsugi.unit.test=true
     */
    public static boolean unitTesting()
    {
        return "true".equals(System.getProperty("tsugi.unit.test"));
    }

    /*
     ** Load a properties file from the class path or return null if not found.
     */
    public static Properties loadProperties(String pathname)
    {

        Properties prop = new Properties();
        InputStream in = TsugiUtils.class.getResourceAsStream(pathname);
        if ( in == null ) {
            log.debug("Properties file not found: "+pathname);
            return null;
        } else {
            try {
                prop.load(in);
                in.close();
                log.debug("Loaded "+prop.size()+" properties from: "+pathname);
                return prop;
            } catch (IOException ex) {
                log.warn("Failed loading properties from: "+pathname,ex);
                return null;
            }
        }
    }

    /*
     ** Scan a result set and return a Properties object with entries for each column.
     */
    public static Properties resultsetToProperties(ResultSet rs)
        throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        Properties props = new Properties();
        for ( int i=1; i<=count; i++ ) {
            String key = rsmd.getColumnLabel(i);
            String value = rs.getString(i);
            if ( value == null ) value = "";
            props.setProperty(key,value);
        }
        return props;
    }

    /*
     ** Create a suitable toString() for Properties.
     */
    public static String dumpProperties(Properties p)
    {
        if ( p == null ) return "Null Properties";
        StringBuffer sb = new StringBuffer();

        Enumeration keys = p.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = (String)p.get(key);
            if ( key.indexOf("secret") >= 0 ) value = "** suppressed ("+value.length()+") **";
            if ( sb.length() > 0 ) sb.append("\n");
            sb.append(key);
            sb.append(": ");
            sb.append(value);
        }
        if ( sb.length() < 1 ) sb.append("Empty Properties");
        return sb.toString();
    }

    /*
     ** Copy a property from one list to another
     */
    public static void copy(Properties to, Properties from, String key) 
    {
        copy(to,key,from,key);
    }

    /**
     * Copy a property from one list to another
     */
    public static void copy(Properties to, String to_key, Properties from, String from_key) 
    {
        if ( from_key == null || to_key == null) return;
        String value = from.getProperty(from_key);
        if ( value == null ) {
            to.remove(to_key);
        } else {
            to.setProperty(to_key, value);
        }
    }

    /**
     * Allow the server's view of the URL to be overridden
     *
     * This can be configured from a global system property named
     * tsugi.server.url or from that property from the /tsugi.properties
     * file in the classpath.
     *
     * This is most valuable if our server is behind some kind of load balancer
     * that obscures the request URL.
     *
     * TODO: Move this to Output
     */
    public static String getOurServletPath(HttpServletRequest request)
    {
        String URLstr = request.getRequestURL().toString();
        String newPrefix = System.getProperty("tsugi.server.url");
        if ( newPrefix == null ) {
            Properties props = TsugiUtils.loadProperties("/tsugi.properties");
            if ( props != null ) {
                newPrefix = props.getProperty("tsugi.server.url");
            }
        }
    
        if ( newPrefix != null ) {
            URLstr = URLstr.replaceFirst("^https??://[^/]*",newPrefix);
        }
        return URLstr;
    }

}

