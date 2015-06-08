package org.tsugi.impl.jdbc;

import org.tsugi.Tsugi;
import org.tsugi.Launch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

public class Tsugi_JDBC implements Tsugi 
{

    private Log log = LogFactory.getLog(Tsugi_JDBC.class);

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
        return null;
    }

}

