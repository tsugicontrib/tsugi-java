package org.tsugi.impl.jdbc;

import org.tsugi.Tsugi;
import org.tsugi.Launch;
import org.tsugi.base.BaseTsugi;

import org.tsugi.util.TsugiUtils;
import org.tsugi.util.TsugiLTIUtils;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

public class Tsugi_JDBC extends BaseTsugi implements Tsugi
{

    private Log log = LogFactory.getLog(Tsugi_JDBC.class);

    private String version = "201506070900";

    // The prefix for all of the tables
    private String prefix = null;

    public String getVersion()
    {
        return version;
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
        prefix = System.getProperty("tsugi.datasource.tablePrefix");

        if ( jdbc == null | username == null || password == null || className == null )
        {
            Properties props = TsugiUtils.loadProperties("/tsugi.properties");
            if ( props != null ) {
                jdbc = props.getProperty("tsugi.datasource.url");
                username = props.getProperty("tsugi.datasource.username");
                password = props.getProperty("tsugi.datasource.password");
                className = props.getProperty("tsugi.datasource.driverClassName");
                prefix = props.getProperty("tsugi.datasource.tablePrefix");
            }
        }

        if ( jdbc == null | username == null || password == null || className == null )
        {
            log.error("Please add to system properties or /tsugi.properties in classpath");
            log.error("tsugi.datasource.url="+jdbc);
            log.error("tsugi.datasource.username="+jdbc);
            log.error("tsugi.datasource.password=*****");
            log.error("tsugi.datasource.driverClassName="+className);
            log.error("tsugi.datasource.tablePrefix="+prefix);
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

        return connection;

    }


   /**
     * Fire up the launch.  If req is null - we behave as if this is a test.
     */
    public Launch getLaunch(HttpServletRequest req, Properties props, HttpServletResponse res)
    {
        if ( ! TsugiLTIUtils.isRequest(props) ) {
            return null;
        }
        Properties post = extractPost(props);
        String x = TsugiUtils.dumpProperties(post);
        System.out.println("Properties:");
        System.out.println(x);

        Connection c = getConnection();
        Properties row = loadAllData(c, post);
        x = TsugiUtils.dumpProperties(row);
        System.out.println("Properties:");
        System.out.println(x);
        return null;
    }


    public String customGet(String varname, String def)
    {
        return "Yo";
    }

    /**
     * Load the data from our lti_ tables using one long LEFT JOIN
     *
     * This data may or may not exist - hence the use of the long
     * LEFT JOIN.
     */
    public Properties loadAllData(Connection c, Properties post) 
    {
        String sql = 
            "SELECT k.key_id, k.key_key, k.secret, k.new_secret, c.settings_url AS key_settings_url, \n" +
            "n.nonce, \n" +
            "c.context_id, c.title AS context_title, context_sha256, c.settings_url AS context_settings_url,\n"+
            "l.link_id, l.title AS link_title, l.settings AS link_settings, l.settings_url AS link_settings_url,\n"+
            "u.user_id, u.displayname AS user_displayname, u.email AS user_email, user_key,\n"+
            "u.subscribe AS subscribe, u.user_sha256 AS user_sha256,\n"+
            "m.membership_id, m.role, m.role_override,\n"+
            "r.result_id, r.grade, r.result_url, r.sourcedid";

        if ( post.getProperty("service") != null ) {
            sql += ",\n"+
            "s.service_id, s.service_key AS service";
        }

        sql +="\nFROM {p}lti_key AS k\n"+
            "LEFT JOIN {p}lti_nonce AS n ON k.key_id = n.key_id AND n.nonce = ?\n" + // :nonce 1
            "LEFT JOIN {p}lti_context AS c ON k.key_id = c.key_id AND c.context_sha256 = ?\n" + // :context 2
            "LEFT JOIN {p}lti_link AS l ON c.context_id = l.context_id AND l.link_sha256 = ?\n" + // :link 3
            "LEFT JOIN {p}lti_user AS u ON k.key_id = u.key_id AND u.user_sha256 = ?\n" + // :user 4
            "LEFT JOIN {p}lti_membership AS m ON u.user_id = m.user_id AND c.context_id = m.context_id\n" +
            "LEFT JOIN {p}lti_result AS r ON u.user_id = r.user_id AND l.link_id = r.link_id";

        if ( post.getProperty("service") != null) {
            sql += "\n"+
            "LEFT JOIN {p}lti_service AS s ON k.key_id = s.key_id AND s.service_sha256 = ?"; // :service 5
        }

        sql += "\nWHERE k.key_sha256 = ? LIMIT 1\n";  // :key 6 or 5

        sql = setPrefix(sql);
        log.trace(sql);

        try {
            // Note that extractPost() insures these fields all exist in post
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, post.getProperty("nonce"));
            stmt.setString(2, TsugiUtils.sha256(post.getProperty("context_id")));
            stmt.setString(3, TsugiUtils.sha256(post.getProperty("link_id")));
            stmt.setString(4, TsugiUtils.sha256(post.getProperty("user_id")));
            if ( post.getProperty("service") == null) {
                stmt.setString(5, TsugiUtils.sha256(post.getProperty("key")));
            } else {
                stmt.setString(5, TsugiUtils.sha256(post.getProperty("service")));
                stmt.setString(6, TsugiUtils.sha256(post.getProperty("key")));
            }

            ResultSet rs = stmt.executeQuery();
            Properties row = null;
            if (rs.next()) {
                row = TsugiUtils.resultsetToProperties(rs);
            }
            return row;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
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

