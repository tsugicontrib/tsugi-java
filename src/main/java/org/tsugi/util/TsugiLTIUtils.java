package org.tsugi.util;

import java.util.Properties;
import java.util.Enumeration;
import java.util.Map;
import java.lang.StringBuffer;

import java.io.InputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tsugi.basiclti.BasicLTIConstants;

/**
 * This holds capabililities that are low level LTI utilities
 */
public class TsugiLTIUtils {

    private static Log log = LogFactory.getLog(TsugiLTIUtils.class);

    /*
     ** Return the parameters as Properties
     */

    public static Properties getParameterProperties(HttpServletRequest req) {
        Properties p = new Properties();
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = req.getParameter(name);
            if ( value != null ) p.setProperty(name,value);
        }
        return p;
    }

    /*
     ** Returns true if this is an LTI message with minimum values to meet the protocol
     */
    public static boolean isRequest(Properties reqProps) {
        String vers = reqProps.getProperty(BasicLTIConstants.LTI_VERSION);
        String mtype = reqProps.getProperty(BasicLTIConstants.LTI_MESSAGE_TYPE);
        if ( BasicLTIConstants.LTI_VERSION_1.equals(vers) || BasicLTIConstants.LTI_VERSION_2.equals(vers) ) {
            // Good
        } else {
            return false;
        }
        if ( BasicLTIConstants.LTI_MESSAGE_TYPE_TOOLPROXYREGISTRATIONREQUEST.equals(mtype) || 
             BasicLTIConstants.LTI_MESSAGE_TYPE_TOOLPROXY_RE_REGISTRATIONREQUEST.equals(mtype) || 
             BasicLTIConstants.LTI_MESSAGE_TYPE_BASICLTILAUNCHREQUEST.equals(mtype) ) {
            // Good
        } else { 
            return false;
        }
        return true;
    }
}

