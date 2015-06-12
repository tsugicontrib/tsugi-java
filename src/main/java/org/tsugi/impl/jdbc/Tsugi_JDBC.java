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

public class Tsugi_JDBC extends BaseTsugi implements Tsugi
{

    private Log log = LogFactory.getLog(Tsugi_JDBC.class);

    // The prefix for all of the tables
    private String prefix = null;

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
        
        HttpSession session = null;
        if ( req != null ) {  // Allow for testing
            session = req.getSession();
        }

        BaseLaunch launch = new BaseLaunch();
        String x = null;

        Connection c = getConnection();
        if ( c == null ) {
            log.error("Unable to upen database connection");
            launch.error_message = "Unable to upen database connection";
            return launch;
        }

        if ( ! TsugiLTIUtils.isRequest(props) ) {
            System.out.println("TODO: Pull in from session");
            if ( session == null ) {  // Test harness
                launch.error_message = "This tool must be launched using LTI";
                return launch;
            }
            // Pull in the previous row if it is in the session
            Properties sess_row = (Properties) session.getAttribute("lti_row");
            if ( sess_row == null ) {
                launch.error_message = "This tool must be launched using LTI";
                return launch;
            }
            x = TsugiUtils.dumpProperties(sess_row);
            System.out.println("Session Properties:");
            System.out.println(x);
            buildLaunch(c, launch, req, res, sess_row);
            return launch;
        }

        // Start fresh
        if ( session != null ) session.removeAttribute("lti_row");
        // x = TsugiUtils.dumpProperties(props);
        // System.out.println("Input POST Properties:");
        // System.out.println(x);

        Properties post = extractPost(props);
        if ( post == null ) {
            log.error("Missing essential POST data");
            launch.error_message = "Missing essential POST data";
            return launch;
        }
        x = TsugiUtils.dumpProperties(post);
        System.out.println("Extracted POST Properties:");
        System.out.println(x);

        Properties row = loadAllData(c, post);
        if ( row == null ) {
            log.error("Key not found");
            launch.error_message = "Key not found";
            return launch;
        }

        x = TsugiUtils.dumpProperties(row);
        System.out.println("Database Properties:");
        System.out.println(x);

        launch.connection = c;
        if ( req != null ) {
            String key = StringUtils.stripToNull(row.getProperty("key_key"));
            String secret = StringUtils.stripToNull(row.getProperty("secret"));
            String new_secret = StringUtils.stripToNull(row.getProperty("new_secret"));
            boolean success = false;
            if ( new_secret != null ) {
                success = checkOAuthSignature(req, key, new_secret);
            }   
            if ( !success && secret != null ) {
                success = checkOAuthSignature(req, key, secret);
            }
            if ( !success ) {
                log.error("OAuth error: "+error_message);
                log.error("Base string: "+base_string);
                launch.error_message = error_message;
                launch.base_string = base_string;
                return launch;
            }
        } else {
            log.warn("HttpServletRequest is null - test only");
        }

        adjustData(c, row, post);

System.out.println("TODO: Make sure to do NONCE cleanup...");

        buildLaunch(c, launch, req, res, row);

        // TODO: Maybe not
        launch.database = new BaseDatabase(c, prefix);

        if ( session != null ) session.setAttribute("lti_row", row);
        return launch;
    }

    private void buildLaunch(Connection c, BaseLaunch launch, 
        HttpServletRequest req, HttpServletResponse res, Properties row)
    {
        launch.request = req;
        launch.response = res;
        // Create our new objects
        Service service = null;
        if ( StringUtils.isNotBlank(row.getProperty("service_id")) ) {
            service = new BaseService(row);
        }
        launch.result = new BaseResult(row, service);
        // TODO: Make settings real
        Settings linkSettings = new Settings_JDBC(c, row, prefix, "link", req);
        launch.link = new BaseLink(row, launch.result, linkSettings);
        Settings contextSettings = new Settings_JDBC(c, row, prefix, "context", req);
        launch.context = new BaseContext(row, contextSettings);
        launch.user = new BaseUser(row);
        launch.output = new BaseOutput(req, res);

        launch.valid = true;
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
            "c.context_id, c.title AS context_title, context_sha256, c.settings AS context_settings, c.settings_url AS context_settings_url,\n"+
            "l.link_id, l.title AS link_title, l.settings AS link_settings, l.settings_url AS link_settings_url,\n"+
            "u.user_id, u.displayname AS user_displayname, u.email AS user_email, user_key,\n"+
            "u.subscribe AS subscribe, u.user_sha256 AS user_sha256,\n"+
            "m.membership_id, m.role, m.role_override,\n"+
            "r.result_id, r.grade, r.result_url, r.sourcedid";

        if ( StringUtils.isNotBlank(post.getProperty("service")) ) {
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

        if ( StringUtils.isNotBlank(post.getProperty("service")) ) {
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
            stmt.setString(2, TsugiUtils.sha256(post.getProperty("context_key")));
            stmt.setString(3, TsugiUtils.sha256(post.getProperty("link_key")));
            stmt.setString(4, TsugiUtils.sha256(post.getProperty("user_key")));
            if ( StringUtils.isBlank(post.getProperty("service")) ) {
                stmt.setString(5, TsugiUtils.sha256(post.getProperty("key_key")));
            } else {
                stmt.setString(5, TsugiUtils.sha256(post.getProperty("service")));
                stmt.setString(6, TsugiUtils.sha256(post.getProperty("key_key")));
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return TsugiUtils.resultsetToProperties(rs);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Make sure that the data in our lti_ tables matches the POST data
     *
     * This routine compares the POST data with the data pulled from the
     * lti_ tables and goes through carefully INSERTing or UPDATING
     * all the nexessary data in the lti_ tables to make sure that
     * the lti_ table correctly match all the data from the incoming post.
     *
     * While this looks like a lot of INSERT and UPDATE statements,
     * the INSERT statements only run when we see a new user/course/link
     * for the first time and after that, we only update is something
     * changes.   So in a high percentage of launches we are not seeing
     * any new or updated data and so this code just falls through and
     * does absolutely no SQL.
     */
    public void adjustData(Connection c, Properties row, Properties post)
    {
        String sql = null;

        // Connect the user to the key
        String user_displayname = StringUtils.stripToNull(post.getProperty("user_displayname"));
        String user_email = StringUtils.stripToNull(post.getProperty("user_email"));

        if ( StringUtils.isBlank(row.getProperty("user_id")) &&
            StringUtils.isNotBlank(post.getProperty("link_key") )) {
            sql = "INSERT INTO {p}lti_user \n" +
                "( user_key, user_sha256, displayname, email, key_id, created_at, updated_at ) VALUES \n" +
                "( ?,        ?,           ?,           ?,     ?,      NOW(), NOW() )";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, post.getProperty("user_key"));
                stmt.setString(2, TsugiUtils.sha256(post.getProperty("user_key")));
                stmt.setString(3, user_displayname);
                stmt.setString(4, user_email);
                stmt.setString(5, row.getProperty("key_id"));

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Long user_id = rs.getLong(1);
                    System.out.println("Inserted user_id="+user_id);
                    row.setProperty("user_id",user_id+"");
                    TsugiUtils.copy(row, post, "link_title");
                    TsugiUtils.copy(row, post, "link_settings_url");
                } else {
                    throw new RuntimeException("Insert failed "+sql);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        // Connect the context to the key
        if ( StringUtils.isBlank(row.getProperty("context_id")) ) {
            sql = "INSERT INTO {p}lti_context \n" +
                " ( context_key, context_sha256, settings_url, title, key_id, created_at, updated_at ) VALUES \n" +
                " ( ?,            ?,               ?,             ?,      ?,       NOW(), NOW() )";
                //( :context_key, :context_sha256, :settings_url, :title, :key_id, NOW(), NOW() )";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, post.getProperty("context_key"));
                stmt.setString(2, TsugiUtils.sha256(post.getProperty("context_key")));
                stmt.setString(3, post.getProperty("context_settings_url"));
                stmt.setString(4, post.getProperty("context_title"));
                stmt.setString(5, row.getProperty("key_id"));

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Long context_id = rs.getLong(1);
                    System.out.println("Inserted context_id="+context_id);
                    row.setProperty("context_id",context_id+"");
                    TsugiUtils.copy(row, post, "context_title");
                    TsugiUtils.copy(row, post, "context_settings_url");
                } else {
                    throw new RuntimeException("Insert failed "+sql);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        // Connect the link to the context
        if ( StringUtils.isBlank(row.getProperty("link_id")) &&
            StringUtils.isNotBlank(post.getProperty("link_key") )) {
            sql = "INSERT INTO {p}lti_link \n" +
                "( link_key, link_sha256, settings_url, title, context_id, created_at, updated_at ) VALUES \n" +
                "( ?,        ?,           ?,            ?,     ?,          NOW(), NOW() )";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, post.getProperty("link_key"));
                stmt.setString(2, TsugiUtils.sha256(post.getProperty("link_key")));
                stmt.setString(3, post.getProperty("link_settings_url"));
                stmt.setString(4, post.getProperty("link_title"));
                stmt.setString(5, row.getProperty("context_id"));

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Long link_id = rs.getLong(1);
                    System.out.println("Inserted link_id="+link_id);
                    row.setProperty("link_id",link_id+"");
                    TsugiUtils.copy(row, post, "link_title");
                    TsugiUtils.copy(row, post, "link_settings_url");
                } else {
                    throw new RuntimeException("Insert failed "+sql);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        // Connect the user and context via the membership
        if ( StringUtils.isBlank(row.getProperty("membership_id")) &&
            StringUtils.isNotBlank(row.getProperty("context_id")) &&
            StringUtils.isNotBlank(row.getProperty("user_id")) ) {

            sql = "INSERT INTO {p}lti_membership \n" +
                "( context_id, user_id, role, created_at, updated_at ) VALUES \n" +
                "( ?,          ?,       ?,    NOW(), NOW() )";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, row.getProperty("context_id"));
                stmt.setString(2, row.getProperty("user_id"));
                stmt.setString(3, post.getProperty("role"));

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Long membership_id = rs.getLong(1);
                    System.out.println("Inserted membership_id="+membership_id);
                    row.setProperty("membership_id",membership_id+"");
                    TsugiUtils.copy(row, post, "role");
                } else {
                    throw new RuntimeException("Insert failed "+sql);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        // Connect an LTI 1.x service URL to the key
        String oldserviceid = StringUtils.stripToNull(row.getProperty("service_id"));
        if ( StringUtils.isNotBlank(row.getProperty("service")) ) {

            if ( oldserviceid == null ) {
                sql = "INSERT INTO {p}lti_service \n" +
                    "( service_key, service_sha256, key_id, created_at, updated_at ) VALUES \n" +
                    "( ?,           ?,              ?,      NOW(), NOW() )";
                sql = setPrefix(sql);
                log.debug(sql);

                try {
                    PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, post.getProperty("service"));
                    stmt.setString(2, TsugiUtils.sha256(post.getProperty("service")));
                    stmt.setString(3, row.getProperty("key_id"));

                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        Long service_id = rs.getLong(1);
                        System.out.println("Inserted service_id="+service_id);
                        row.setProperty("service_id",service_id+"");
                        TsugiUtils.copy(row, post, "service");
                    } else {
                        throw new RuntimeException("Insert failed "+sql);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return;
                }
            }

            // If we just created a new service entry but we already had a result entry, update it
            // This is for LTI 1.x only as service is not used for LTI 2.x
            if ( oldserviceid == null &&
                StringUtils.isNotBlank(row.getProperty("service_id")) &&
                StringUtils.isNotBlank(row.getProperty("result_id")) ) {
                sql = "UPDATE {p}lti_result SET service_id = ? WHERE result_id = ?";
                sql = setPrefix(sql);
                log.debug(sql);

                try {
                    PreparedStatement stmt = c.prepareStatement(sql);
                    stmt.setString(1, row.getProperty("service_id"));
                    stmt.setString(2, row.getProperty("result_id"));

                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }

        // We always insert a result row if we have a link - we will store
        // grades locally in this row - even if we cannot send grades
        if ( StringUtils.isBlank(row.getProperty("result_id")) &&
            StringUtils.isNotBlank(row.getProperty("link_id")) &&
            StringUtils.isNotBlank(row.getProperty("user_id")) ) {

            sql = "INSERT INTO {p}lti_result \n" +
                " ( link_id, user_id, created_at, updated_at ) VALUES \n" +
                " ( ?,       ?,       NOW(), NOW() )";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, row.getProperty("link_id"));
                stmt.setString(2, row.getProperty("user_id"));

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Long result_id = rs.getLong(1);
                    System.out.println("Inserted result_id="+result_id);
                    row.setProperty("result_id",result_id+"");
                } else {
                    throw new RuntimeException("Insert failed "+sql);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        // Here we handle mismatches of sourcedid or result_url
        // including if we just inserted the result row
        boolean mismatch = StringUtils.isNotBlank(post.getProperty("sourcedid")) &&
            ( ! StringUtils.equals(post.getProperty("sourcedid"), row.getProperty("sourcedid")) );
        mismatch = mismatch ||
                ( StringUtils.isNotBlank(post.getProperty("result_url")) &&
                    ( ! StringUtils.equals(post.getProperty("result_url"), row.getProperty("result_url")) )
                );
        mismatch = mismatch ||
                ( StringUtils.isNotBlank(post.getProperty("service_id")) &&
                    ( ! StringUtils.equals(post.getProperty("service_id"), row.getProperty("service_id")) )
                );

        if ( mismatch && StringUtils.isNotBlank(row.getProperty("result_id")) ) {
            sql = "UPDATE {p}lti_result \n" +
                "SET sourcedid = ?, result_url = ?, service_id = ? \n" +
                "WHERE result_id = ?";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1, StringUtils.stripToNull(post.getProperty("sourcedid")));
                stmt.setString(2, StringUtils.stripToNull(post.getProperty("result_url")));
                stmt.setString(3, StringUtils.stripToNull(row.getProperty("service_id")));
                stmt.setString(4, row.getProperty("result_id"));

                stmt.executeUpdate();
                TsugiUtils.copy(row, post, "sourcedid");
                TsugiUtils.copy(row, post, "result_url");
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        // Here we handle updates to context_title, link_title, user_displayname, user_email, or role
        if ( StringUtils.isNotBlank(row.getProperty("context_id")) &&
             StringUtils.isNotBlank(post.getProperty("context_title")) &&
            ! StringUtils.equals(row.getProperty("context_title"), post.getProperty("context_title")) ) {

            sql = "UPDATE {p}lti_context SET title = ? WHERE context_id = ?";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1, post.getProperty("context_title"));
                stmt.setString(2, row.getProperty("context_id"));

                stmt.executeUpdate();
                TsugiUtils.copy(row, post, "context_title");
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        if ( StringUtils.isNotBlank(row.getProperty("link_id")) &&
             StringUtils.isNotBlank(post.getProperty("link_title")) &&
            ! StringUtils.equals(row.getProperty("link_title"), post.getProperty("link_title")) ) {

            sql = "UPDATE {p}lti_link SET title = ? WHERE link_id = ?";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1, post.getProperty("link_title"));
                stmt.setString(2, row.getProperty("link_id"));

                stmt.executeUpdate();
                TsugiUtils.copy(row, post, "link_title");
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        if ( StringUtils.isNotBlank(row.getProperty("user_id")) &&
             StringUtils.isNotBlank(post.getProperty("user_displayname")) &&
            ! StringUtils.equals(row.getProperty("user_displayname"), post.getProperty("user_displayname") )) {

            sql = "UPDATE {p}lti_user SET displayname = ? WHERE user_id = ?";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1, post.getProperty("user_displayname"));
                stmt.setString(2, row.getProperty("user_id"));

                stmt.executeUpdate();
                TsugiUtils.copy(row, post, "user_displayname");
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }

        if ( StringUtils.isNotBlank(row.getProperty("user_id")) &&
             StringUtils.isNotBlank(post.getProperty("user_email")) &&
            ! StringUtils.equals(row.getProperty("user_email"), post.getProperty("user_email")) ) {

            sql = "UPDATE {p}lti_user SET email = ? WHERE user_id = ?";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1, post.getProperty("user_email"));
                stmt.setString(2, row.getProperty("user_id"));

                stmt.executeUpdate();
                TsugiUtils.copy(row, post, "user_email");
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
        }


        if ( StringUtils.isNotBlank(row.getProperty("membership_id")) &&
             StringUtils.isNotBlank(post.getProperty("role")) &&
            ! StringUtils.equals(row.getProperty("role"), post.getProperty("role")) ) {

            sql = "UPDATE {p}lti_membership SET role = ? WHERE membership_id = ?";
            sql = setPrefix(sql);
            log.debug(sql);

            try {
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1, post.getProperty("role"));
                stmt.setString(2, row.getProperty("membership_id"));

                stmt.executeUpdate();
                TsugiUtils.copy(row, post, "role");
            } catch (SQLException ex) {
                ex.printStackTrace();
                return;
            }
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

