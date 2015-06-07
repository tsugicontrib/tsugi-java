package org.tsugi.impl;

import org.tsugi.Tsugi;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

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

    public Connection getConnection()
    {
        return null;
    }

}

