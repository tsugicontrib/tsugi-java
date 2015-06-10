package org.tsugi;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.tsugi.Tsugi;
import org.tsugi.TsugiFactory;
import org.tsugi.Launch;
import org.tsugi.Context;
import org.tsugi.User;
import org.tsugi.Link;
import org.tsugi.Result;
import org.tsugi.Service;
import org.tsugi.util.TsugiUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;


public class TsugiTest {

    public static boolean setupDone = false;
    public static final String unitTestKey = "unit-test-xyzzy-key";
    public static final String unitTestKeySha256 = TsugiUtils.sha256(unitTestKey);
    static Tsugi tsugi = null;
    static Connection c = null;
    static DatabaseMetaData meta = null;
    static boolean localhost = false;
    static Long key_id = null;;
    static Launch launch = null;

    @BeforeClass
    public static void setup() throws Exception {
        tsugi = TsugiFactory.getTsugi();
        if ( tsugi == null ) return;
        launch = tsugi.getLaunch(fakePost());
        c = tsugi.getConnection();
        if ( c == null ) return;
        meta = c.getMetaData();
        String URL = meta.getURL();
        localhost = URL.indexOf("//localhost") > 0 || URL.startsWith("jdbc:h2:");
    }

    @Test
    public void testMetaData() throws Exception {
        if ( c == null || meta == null ) {
            assertTrue(false);
            return;
        }

        String productName = meta.getDatabaseProductName();
        String productVersion = meta.getDatabaseProductVersion();
        String URL = meta.getURL();
        System.out.println("Connection product=" + productName+" version=" + productVersion);
        System.out.println("Connection URL=" + URL + ((localhost) ? " (localhost)" : ""));
    }

    @Test
    public void testKey() throws Exception {
        String query = "SELECT key_id FROM lti_key WHERE key_sha256 = ? LIMIT 1;";
        PreparedStatement stmt = c.prepareStatement(query);
        stmt.setString(1, unitTestKeySha256);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            key_id = rs.getLong("key_id");
            System.out.println("Found key_id="+key_id);
            return;
        }

        if ( ! localhost ) {
            System.out.println("Will not insert test key into non-local database");
            assertTrue(false);
            return;
        }

        query= "INSERT INTO lti_key (key_sha256, key_key, secret) VALUES ( ?, ?, ? )";
        stmt = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, unitTestKeySha256);
        stmt.setString(2, unitTestKey);
        stmt.setString(3, "secret");
        stmt.executeUpdate();
        rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            Long key_id = rs.getLong(1);
            System.out.println("Inserted key_id="+key_id);
        }


    }
    
    @Test
    public void testThing() {
        Properties f = fakePost();
        assertEquals(f.getProperty("context_title"), launch.getContext().getTitle());
        assertEquals(f.getProperty("lis_person_contact_email_primary"), launch.getUser().getEmail());
        assertEquals(f.getProperty("resource_link_title"), launch.getLink().getTitle());
    }

    public static Properties fakePost() {
        Properties f = new Properties();
        f.setProperty("custom_assn", "mod/attend/index.php");
        f.setProperty("lis_person_name_full", "Jane Instructor");
        f.setProperty("lis_person_name_family", "Instructor");
        f.setProperty("lis_person_name_given", "Jane");
        f.setProperty("lis_person_contact_email_primary", "inst@ischool.edu");
        f.setProperty("lis_person_sourcedid", "ischool.edu:inst");
        f.setProperty("user_id", "292832126");
        f.setProperty("roles", "Instructor");
        f.setProperty("resource_link_id", "667587732");
        f.setProperty("resource_link_title", "Activity: attend");
        f.setProperty("resource_link_description", "A weekly blog.");
        f.setProperty("context_id", "456434513");
        f.setProperty("context_label", "SI106");
        f.setProperty("context_title", "Introduction to Programming");
        f.setProperty("tool_consumer_info_product_family_code", "ims");
        f.setProperty("tool_consumer_info_version", "1.1");
        f.setProperty("tool_consumer_instance_guid", "lmsng.ischool.edu");
        f.setProperty("tool_consumer_instance_description", "University of Information");
        f.setProperty("custom_due", "2016-12-12 10:00:00.5");
        f.setProperty("custom_timezone", "Pacific/Honolulu");
        f.setProperty("custom_penalty_time", "86400");
        f.setProperty("custom_penalty_cost", "0.2");
        f.setProperty("lis_result_sourcedid", "e10e575674e68bbcd873e2962f5a138b");
        f.setProperty("oauth_callback", "about:blank");
        f.setProperty("lis_outcome_service_url", "http://localhost:8888/tsugi/common/tool_consumer_outcome.php?b64=MTIzNDU6OjpzZWNyZXQ6Ojo=");
        f.setProperty("launch_presentation_css_url", "http://localhost:8888/tsugi/lms.css");
        f.setProperty("lti_version", "LTI-1p0");
        f.setProperty("lti_message_type", "basic-lti-launch-request");
        f.setProperty("ext_submit", "Finish Launch");
        f.setProperty("oauth_version", "1.0");
        f.setProperty("oauth_nonce", "e5c4e475c39eb4d4223a232f99fbd39f");
        f.setProperty("oauth_timestamp", "1433793103");
        f.setProperty("oauth_consumer_key", unitTestKey);
        f.setProperty("oauth_signature_method", "HMAC-SHA1");
        f.setProperty("oauth_signature", "y21x1iiVNp2UmDNJRp/MYLsgkEM=");
        f.setProperty("context_id", "456434513");
        f.setProperty("context_label", "SI106");
        f.setProperty("context_title", "Introduction to Programming");
        f.setProperty("custom_assn", "mod/attend/index.php");
        f.setProperty("custom_due", "2016-12-12 10:00:00.5");
        f.setProperty("custom_penalty_cost", "0.2");
        f.setProperty("custom_penalty_time", "86400");
        f.setProperty("custom_timezone", "Pacific/Honolulu");
        f.setProperty("ext_submit", "Finish Launch");
        f.setProperty("launch_presentation_css_url", "http://localhost:8888/tsugi/lms.css");
        f.setProperty("lis_outcome_service_url", "http://localhost:8888/tsugi/common/tool_consumer_outcome.php?b64=MTIzNDU6OjpzZWNyZXQ6Ojo=");
        f.setProperty("lis_person_contact_email_primary", "inst@ischool.edu");
        f.setProperty("lis_person_name_family", "Instructor");
        f.setProperty("lis_person_name_full", "Jane Instructor");
        f.setProperty("lis_person_name_given", "Jane");
        f.setProperty("lis_person_sourcedid", "ischool.edu:inst");
        f.setProperty("lis_result_sourcedid", "e10e575674e68bbcd873e2962f5a138b");
        f.setProperty("lti_message_type", "basic-lti-launch-request");
        f.setProperty("lti_version", "LTI-1p0");
        f.setProperty("oauth_callback", "about:blank");
        f.setProperty("oauth_consumer_key", "12345");
        f.setProperty("oauth_nonce", "e5c4e475c39eb4d4223a232f99fbd39f");
        f.setProperty("oauth_signature", "y21x1iiVNp2UmDNJRp/MYLsgkEM=");
        f.setProperty("oauth_signature_method", "HMAC-SHA1");
        f.setProperty("oauth_timestamp", "1433793103");
        f.setProperty("oauth_version", "1.0");
        f.setProperty("resource_link_description", "A weekly blog.");
        f.setProperty("resource_link_id", "667587732");
        f.setProperty("resource_link_title", "Activity: attend");
        f.setProperty("roles", "Instructor");
        f.setProperty("tool_consumer_info_product_family_code", "ims");
        f.setProperty("tool_consumer_info_version", "1.1");
        f.setProperty("tool_consumer_instance_description", "University of Information");
        f.setProperty("tool_consumer_instance_guid", "lmsng.ischool.edu");
        f.setProperty("user_id", "292832126");
        return f;
    }

}
