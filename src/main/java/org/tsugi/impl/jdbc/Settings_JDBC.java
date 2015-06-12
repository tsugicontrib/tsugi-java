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

    private HttpServletRequest req = null;
    private String prefix = null;

    // Which setting this is...
    private String which = null;

    // LTI 2.x settings URL
    private String resource = null;

    // Keep the whole row for later...
    private Properties row;

    public boolean valid = false;

    public Settings_JDBC(Properties row, String prefix, String which, HttpServletRequest req)
    {
        this.prefix = prefix;
        this.which = which;
        this.req = req;
        this.row = row;
        this.resource = StringUtils.stripToNull(row.getProperty(which+"_settings_url"));
System.out.println(which+" settingsUrl="+this.resource);
        String settingsJson = row.getProperty(which+"_settings");
System.out.println(which+" settingsJson="+settingsJson);
        if ( settingsJson == null || settingsJson.length() < 1 ) return;

        // Parse the existing settings
        try {
            setSettingsJson(settingsJson);
            valid = true;
        } catch (Exception e) {
            e.printStackTrace();
            valid = false;
        }
    }

    /**
     * Persist the settings wherever they need to go.
     *
     * We expect the extending class to override this.  If this is not overridden,
     * settings will be in-memory only.
     */
    @Override
    public boolean persistSettings()
    {
        return true;
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

