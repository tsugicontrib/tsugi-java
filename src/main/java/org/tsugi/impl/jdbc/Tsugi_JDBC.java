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
import java.sql.Statement;
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
        String x = null;
        // x = TsugiUtils.dumpProperties(props);
        // System.out.println("Input POST Properties:");
        // System.out.println(x);

        Properties post = extractPost(props);
        if ( post == null ) {
            log.error("Missing essential POST data");
            return null;
        }
        x = TsugiUtils.dumpProperties(post);
        System.out.println("Extracted POST Properties:");
        System.out.println(x);

        Connection c = getConnection();
        if ( c == null ) {
            log.error("Unable to upen database connection");
            return null;
        }
        Properties row = loadAllData(c, post);
        if ( row == null ) {
            log.error("Key not found");
            return null;
        }

        // TODO: Check OAuth
        System.out.println("TODO: OAUTH Checking...");

        x = TsugiUtils.dumpProperties(row);
        System.out.println("Database Properties:");
        System.out.println(x);

        adjustData(c, row, post);

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
            stmt.setString(2, TsugiUtils.sha256(post.getProperty("context_key")));
            stmt.setString(3, TsugiUtils.sha256(post.getProperty("link_key")));
            stmt.setString(4, TsugiUtils.sha256(post.getProperty("user_key")));
            if ( post.getProperty("service") == null) {
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

        if ( row.getProperty("context_id").length() == 0) {
            sql = "INSERT INTO {p}lti_context \n" +
                " ( context_key, context_sha256, settings_url, title, key_id, created_at, updated_at ) VALUES \n" +
                " ( ?,            ?,               ?,             ?,      ?,       NOW(), NOW() )";
                //( :context_key, :context_sha256, :settings_url, :title, :key_id, NOW(), NOW() )";
            sql = setPrefix(sql);
            log.error(sql);

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

/*

        if ( row["link_id"] === null && isset(post["link_id"]) ) {
            sql = "INSERT INTO {p}lti_link
                ( link_key, link_sha256, settings_url, title, context_id, created_at, updated_at ) VALUES
                    ( :link_key, :link_sha256, :settings_url, :title, :context_id, NOW(), NOW() )";
            PDOX->queryDie(sql, array(
                ":link_key" => post["link_id"],
                ":link_sha256" => lti_sha256(post["link_id"]),
                ":settings_url" => post["link_settings_url"],
                ":title" => post["link_title"],
                ":context_id" => row["context_id"]));
            row["link_id"] = PDOX->lastInsertId();
            row["link_title"] = post["link_title"];
            row["link_settings_url"] = post["link_settings_url"];
            actions[] = "=== Inserted link id=".row["link_id"]." ".row["link_title"];
        }

        user_displayname = isset(post["user_displayname"]) ? post["user_displayname"] : null;
        user_email = isset(post["user_email"]) ? post["user_email"] : null;
        if ( row["user_id"] === null && isset(post["user_id"]) ) {
            sql = "INSERT INTO {p}lti_user
                ( user_key, user_sha256, displayname, email, key_id, created_at, updated_at ) VALUES
                ( :user_key, :user_sha256, :displayname, :email, :key_id, NOW(), NOW() )";
            PDOX->queryDie(sql, array(
                ":user_key" => post["user_id"],
                ":user_sha256" => lti_sha256(post["user_id"]),
                ":displayname" => user_displayname,
                ":email" => user_email,
                ":key_id" => row["key_id"]));
            row["user_id"] = PDOX->lastInsertId();
            row["user_email"] = user_email;
            row["user_displayname"] = user_displayname;
            row["user_key"] = post["user_id"];
            actions[] = "=== Inserted user id=".row["user_id"]." ".row["user_email"];
        }

        if ( row["membership_id"] === null && row["context_id"] !== null && row["user_id"] !== null ) {
            sql = "INSERT INTO {p}lti_membership
                ( context_id, user_id, role, created_at, updated_at ) VALUES
                ( :context_id, :user_id, :role, NOW(), NOW() )";
            PDOX->queryDie(sql, array(
                ":context_id" => row["context_id"],
                ":user_id" => row["user_id"],
                ":role" => post["role"]));
            row["membership_id"] = PDOX->lastInsertId();
            row["role"] = post["role"];
            actions[] = "=== Inserted membership id=".row["membership_id"]." role=".row["role"].
                " user=".row["user_id"]." context=".row["context_id"];
        }

        if ( isset(post["service"])) {
            // We need to handle the case where the service URL changes but we already have a sourcedid
            // This is for LTI 1.x only as service is not used for LTI 2.x
            oldserviceid = row["service_id"];
            if ( row["service_id"] === null && post["service"] ) {
                sql = "INSERT INTO {p}lti_service
                    ( service_key, service_sha256, key_id, created_at, updated_at ) VALUES
                    ( :service_key, :service_sha256, :key_id, NOW(), NOW() )";
                PDOX->queryDie(sql, array(
                    ":service_key" => post["service"],
                    ":service_sha256" => lti_sha256(post["service"]),
                    ":key_id" => row["key_id"]));
                row["service_id"] = PDOX->lastInsertId();
                row["service"] = post["service"];
                actions[] = "=== Inserted service id=".row["service_id"]." ".post["service"];
            }

            // If we just created a new service entry but we already had a result entry, update it
            // This is for LTI 1.x only as service is not used for LTI 2.x
            if ( oldserviceid === null && row["result_id"] !== null && row["service_id"] !== null && post["service"] ) {
                sql = "UPDATE {p}lti_result SET service_id = :service_id WHERE result_id = :result_id";
                PDOX->queryDie(sql, array(
                    ":service_id" => row["service_id"],
                    ":result_id" => row["result_id"]));
                actions[] = "=== Updated result id=".row["result_id"]." service=".row["service_id"];
            }
        }

        // We always insert a result row if we have a link - we will store
        // grades locally in this row - even if we cannot send grades
        if ( row["result_id"] === null && row["link_id"] !== null && row["user_id"] !== null ) {
            sql = "INSERT INTO {p}lti_result
                ( link_id, user_id, created_at, updated_at ) VALUES
                ( :link_id, :user_id, NOW(), NOW() )";
            PDOX->queryDie(sql, array(
                ":link_id" => row["link_id"],
                ":user_id" => row["user_id"]));
            row["result_id"] = PDOX->lastInsertId();
            actions[] = "=== Inserted result id=".row["result_id"];
       }

        // Set these values to null if they were not in the post
        if ( ! isset(post["sourcedid"]) ) post["sourcedid"] = null;
        if ( ! isset(post["service"]) ) post["service"] = null;
        if ( ! isset(row["service"]) ) row["service"] = null;
        if ( ! isset(post["result_url"]) ) post["result_url"] = null;

        // Here we handle updates to sourcedid or result_url including if we
        // just inserted the result row
        if ( row["result_id"] != null &&
            (post["sourcedid"] != row["sourcedid"] || post["result_url"] != row["result_url"] ||
            post["service"] != row["service"] )
        ) {
            sql = "UPDATE {p}lti_result
                SET sourcedid = :sourcedid, result_url = :result_url, service_id = :service_id
                WHERE result_id = :result_id";
            PDOX->queryDie(sql, array(
                ":result_url" => post["result_url"],
                ":sourcedid" => post["sourcedid"],
                ":service_id" => row["service_id"],
                ":result_id" => row["result_id"]));
            row["sourcedid"] = post["sourcedid"];
            row["service"] = post["service"];
            row["result_url"] = post["result_url"];
            actions[] = "=== Updated result id=".row["result_id"]." result_url=".row["result_url"].
                " sourcedid=".row["sourcedid"]." service_id=".row["service_id"];
        }

        // Here we handle updates to context_title, link_title, user_displayname, user_email, or role
        if ( isset(post["context_title"]) && post["context_title"] != row["context_title"] ) {
            sql = "UPDATE {p}lti_context SET title = :title WHERE context_id = :context_id";
            PDOX->queryDie(sql, array(
                ":title" => post["context_title"],
                ":context_id" => row["context_id"]));
            row["context_title"] = post["context_title"];
            actions[] = "=== Updated context=".row["context_id"]." title=".post["context_title"];
        }

        if ( isset(post["link_title"]) && post["link_title"] != row["link_title"] ) {
            sql = "UPDATE {p}lti_link SET title = :title WHERE link_id = :link_id";
            PDOX->queryDie(sql, array(
                ":title" => post["link_title"],
                ":link_id" => row["link_id"]));
            row["link_title"] = post["link_title"];
            actions[] = "=== Updated link=".row["link_id"]." title=".post["link_title"];
        }

        if ( isset(post["user_displayname"]) && post["user_displayname"] != row["user_displayname"] && strlen(post["user_displayname"]) > 0 ) {
            sql = "UPDATE {p}lti_user SET displayname = :displayname WHERE user_id = :user_id";
            PDOX->queryDie(sql, array(
                ":displayname" => post["user_displayname"],
                ":user_id" => row["user_id"]));
            row["user_displayname"] = post["user_displayname"];
            actions[] = "=== Updated user=".row["user_id"]." displayname=".post["user_displayname"];
        }

        if ( isset(post["user_email"]) && post["user_email"] != row["user_email"] && strlen(post["user_email"]) > 0 ) {
            sql = "UPDATE {p}lti_user SET email = :email WHERE user_id = :user_id";
            PDOX->queryDie(sql, array(
                ":email" => post["user_email"],
                ":user_id" => row["user_id"]));
            row["user_email"] = post["user_email"];
            actions[] = "=== Updated user=".row["user_id"]." email=".post["user_email"];
        }

        if ( isset(post["role"]) && post["role"] != row["role"] ) {
            sql = "UPDATE {p}lti_membership SET role = :role WHERE membership_id = :membership_id";
            PDOX->queryDie(sql, array(
                ":role" => post["role"],
                ":membership_id" => row["membership_id"]));
            row["role"] = post["role"];
            actions[] = "=== Updated membership=".row["membership_id"]." role=".post["role"];
        }

        // Restore ERRMODE
        PDOX->setAttribute(\PDO::ATTR_ERRMODE, errormode);
        return actions;
*/
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

