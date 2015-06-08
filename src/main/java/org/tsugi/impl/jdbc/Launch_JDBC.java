package org.tsugi.impl.jdbc;

import org.tsugi.Tsugi;
import org.tsugi.Launch;
import org.tsugi.User;
import org.tsugi.Context;
import org.tsugi.Link;

import org.tsugi.base.BaseLaunch;
import org.tsugi.util.TsugiUtils;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Launch_JDBC extends BaseLaunch 
{
    private Log log = LogFactory.getLog(Launch_JDBC.class);

    public Launch_JDBC(Tsugi tsugi)
    {
        super();
        connection = tsugi.getConnection();
    }

    public String customGet(String varname, String def)
    {
        return "Yo";
    }

}

