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

    // ID to update
    private Long id = null;

    // Connection
    private Connection c = null;

    // Database prefix
    private String prefix = null;

    // Which setting this is...
    private String which = null;

    // LTI 2.x settings URL
    private String resource = null;

    // Keep the whole row for later...
    private Properties row;

    public boolean valid = false;

    public Settings_JDBC(Connection c, Properties row, String prefix, String which, HttpServletRequest req)
    {
        this.c = c;
        this.id = new Long(row.getProperty(which+"_id"));
        this.prefix = prefix;
        this.which = which;
        this.req = req;
        this.row = row;
        this.resource = StringUtils.stripToNull(row.getProperty(which+"_settings_url"));

        String settingsJson = row.getProperty(which+"_settings");
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
     * Persist the settings into the right table and update the LMS
     */
    @Override
    public boolean persistSettings()
    {
        if ( id == null ) return false;

        // Persist settings to the database
        String json = getSettingsJson();
        String sql = "UPDATE {p}lti_"+which+
                " SET settings = ? WHERE "+which+"_id = ?";
        sql = setPrefix(sql);
        log.debug(sql);

        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, json);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

        // Persist settings to the row in the session for 
        // the next request/response cycle
        
        if ( req != null ) {  // Allow for testing
            HttpSession session = req.getSession();
            Properties sess_row = (Properties) session.getAttribute("lti_row");
            if ( sess_row != null ) {
                sess_row.setProperty(which+"_settings",json);
System.out.println("Persisted settings in session for "+which);
            }
        }

        // TODO: Persist resource on LMS
System.out.println("TODO: Need to persist settings to resource="+resource);
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

