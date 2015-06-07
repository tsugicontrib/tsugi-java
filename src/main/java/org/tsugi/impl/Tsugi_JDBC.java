package org.tsugi.impl;

import org.tsugi.Tsugi;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Tsugi_JDBC implements Tsugi 
{

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

