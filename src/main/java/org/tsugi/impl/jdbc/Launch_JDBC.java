package org.tsugi.impl.jdbc;

import org.tsugi.Launch;
import org.tsugi.User;
import org.tsugi.Context;
import org.tsugi.Link;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Launch_JDBC implements Launch 
{

    private Log log = LogFactory.getLog(Launch_JDBC.class);

   /**
     * Get the User associated with the launch.
     */
    public User getUser()
    {
        return null;
    }

   /**
     * Get the Context associated with the launch.
     */
    public Context getContext()
    {
        return null;
    }

   /**
     * Get the Link associated with the launch.
     */
    public Link getLink()
    {
        return null;
    }

    public Connection getConnection()
    {
        return null;
    }

    public String customGet(String varname, String def)
    {
        return "Yo";
    }

}

