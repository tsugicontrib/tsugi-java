package org.tsugi.impl.jdbc;

import org.tsugi.Tsugi;
import org.tsugi.Launch;

import org.tsugi.util.TsugiUtils;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

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

   /**
     * Get a tsugi-suitable connection
     */
    public Connection getConnection()
    {
        String jdbc = System.getProperty("tsugi.datasource.url");
        String username = System.getProperty("tsugi.datasource.username");
        String password = System.getProperty("tsugi.datasource.password");
        String className = System.getProperty("tsugi.datasource.driverClassName");

        if ( jdbc == null | username == null || password == null || className == null )
        {
            Properties props = TsugiUtils.loadProperties("/tsugi.properties");
            if ( props != null ) {
                jdbc = props.getProperty("tsugi.datasource.url");
                username = props.getProperty("tsugi.datasource.username");
                password = props.getProperty("tsugi.datasource.password");
                className = props.getProperty("tsugi.datasource.driverClassName");
            }
        }

        if ( jdbc == null | username == null || password == null || className == null )
        {
            log.error("Please add to system properties or /tsugi.properties in classpath");
            log.error("tsugi.datasource.url="+jdbc);
            log.error("tsugi.datasource.username="+jdbc);
            log.error("tsugi.datasource.password=*****");
            log.error("tsugi.datasource.driverClassName="+className);
            throw new java.lang.RuntimeException("Missing essential Tsugi JDBC properties:");
        } 

        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("Missing JDBC Driver: "+className);
            throw new java.lang.RuntimeException("Missing JDBC Driver: "+className);
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbc, username, password);
    
        } catch (SQLException e) {
            log.error("Your database server may be down.  Or if it is up your database is missing or inaccessible.");
            log.error("Install the PHP version of Tsugi from www.tsugi.org and use that tool to provision an empty tsugi database.");
            throw new java.lang.RuntimeException("Database server is down or tsugi database is missing");
        }

System.out.println("Connection="+connection);
        return connection;

    }

    public String customGet(String varname, String def)
    {
        return "Yo";
    }

}

