package org.tsugi.impl.jdbc;

import org.tsugi.*;

import org.tsugi.base.*;

import org.tsugi.util.TsugiUtils;
import org.tsugi.util.TsugiLTIUtils;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;  

import org.apache.commons.lang3.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Settings_JDBC extends BaseSettings implements Settings
{

    private Log log = LogFactory.getLog(Settings_JDBC.class);

    private String prefix = null;

    // Name of this in session
    private String sessionName = null;

    // Table where this is stored
    private String tableName = null;

    public boolean valid = false;

    public Settings_JDBC(Properties row, String prefix, String sessionName, String tableName)
    {
        this.prefix = prefix;
        this.sessionName = sessionName;
        this.tableName = tableName;
        try {
            setSettingsJson(null);
            valid = true;
        } catch (Exception e) {
            e.printStackTrace();
            valid = false;
        }
    }

   /**
     * Retrieve an array of all of the settings
     *
     * If there are no settings, return an empty array.  
     */
    public Properties getAllSettings()
    {
        return null;
    }

    /**
     * Set all of the settings.
     *
     * @param props Properties that are serialized in JSON and stored.
     * Replaces existing settings.  If this is an empty array, this effectively
     * empties out all the settings.
     */
    public boolean setAllSettings(Properties props)
    {
        return false;
    }

    /**
     * Set or update a number of keys to new values in link settings.
     *
     * @param props An array of key value pairs that are to be updated / added
     * in the settings.
     */
    public boolean updateAllSettings(Properties props)
    {
        return false;
    }

    /**
     * Retrieve a particular key from the link settings.
     *
     * Returns the value found in settings or false if the key was not found.
     *
     * @param key - The key to get from the settings.
     * @param def - What to return if the key is not present
     */
    public String getSetting(String key, String def)
    {
        return null;
    }

    /**
     * Set or update a key to a new value in link settings.
     *
     * @params key The key to set in settings.
     * @params value The value to set for that key
     */
    public boolean setSetting(String key, String value)
    {
        return false;
    }

    /*
     ** Fix the prefix {p} inside of a TSUGI SQL query
     */
    public String setPrefix(String sql)
    {
        if ( prefix == null ) {
            return sql.replace("{p}", "");
        }
        return sql.replace("{p}", prefix);
    }
}

