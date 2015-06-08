package org.tsugi.base;

import org.tsugi.Tsugi;
import org.tsugi.Launch;
import org.tsugi.util.TsugiUtils;
import org.tsugi.util.TsugiLTIUtils;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

import org.imsglobal.basiclti.BasicLTIUtil;

/*
 ** Implement the code that is common across implementations here. 
 */
public abstract class BaseTsugi implements Tsugi 
{

    private Log log = LogFactory.getLog(BaseTsugi.class);

    private String version = "201506070900";

    public String getVersion()
    {
        return version;
    }

   /**
     * Get the launch information for the current session
     */
    public Launch getLaunch(HttpServletRequest req, HttpServletResponse res)
    {
        Properties props = TsugiLTIUtils.getParameterProperties(req);
        return getLaunch(req, props, res);
    }

   /**
     * Get the launch information for the current session with only properties
     */
    public Launch getLaunch(Properties props)
    {
        return getLaunch(null, props, null);
    }

   /**
     * This should be private
     */
    private Launch getLaunch(HttpServletRequest req, Properties props, HttpServletResponse res)
    {
        if ( ! TsugiLTIUtils.isRequest(props) ) {
            return null;
        }
        return null;
    }


    public String customGet(String varname, String def)
    {
        return "Yo";
    }

}

