package org.tsugi;

import java.util.Properties;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.tsugi.Tsugi;
import org.tsugi.TsugiFactory;
import org.tsugi.Launch;
import org.tsugi.Context;
import org.tsugi.User;
import org.tsugi.Link;
import org.tsugi.Result;
import org.tsugi.Service;
import org.tsugi.Database;
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
        c = tsugi.getConnection();
        if ( c == null ) return;
        Properties post = fakePost1();
        // Clean up old data from previous launches
        String query = "DELETE FROM lti_context WHERE context_key = ?;";
        PreparedStatement stmt = c.prepareStatement(query);
        stmt.setString(1, post.getProperty("context_id"));
        stmt.executeUpdate();
        
        launch = tsugi.getLaunch(post);
        meta = c.getMetaData();
        String URL = meta.getURL();
        localhost = URL.indexOf("//localhost") > 0 || URL.startsWith("jdbc:h2:");
    }

    @Test
    public void testMetaData() throws Exception {
        // We will blow up one test if the database is not connected
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
        assumeNotNull(launch);
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
    public void testFirstLaunchBasics() {
        assumeNotNull(launch);
        Properties f = fakePost1();
        assertTrue(launch.getContext().getId() > 0 );
        assertTrue(launch.getUser().getId() > 0 );
        assertTrue(launch.getLink().getId() > 0 );
        assertTrue(launch.getResult().getId() > 0 );
        assertTrue(launch.getService().getId() > 0 );
        assertTrue(launch.getUser().isInstructor());
        assertFalse(launch.getUser().isTenantAdmin());
        assertFalse(launch.getUser().isRootAdmin());
        assertNull(launch.getResult().getURL());
        assertEquals(f.getProperty("context_title"), launch.getContext().getTitle());
        assertEquals(f.getProperty("lis_person_contact_email_primary"), launch.getUser().getEmail());
        assertEquals(f.getProperty("resource_link_title"), launch.getLink().getTitle());
        assertEquals(f.getProperty("lis_result_sourcedid"), launch.getResult().getSourceDID());
        assertEquals(f.getProperty("lis_outcome_service_url"), launch.getService().getURL());
    }

    @Test
    public void testSecondLaunchBasics() {
        Properties f = fakePost1();

        Launch launch2 = tsugi.getLaunch(f);
        assertNotNull(launch2);
        assertFalse(launch2.getUser().isTenantAdmin());
        assertFalse(launch2.getUser().isRootAdmin());

        assertEquals(launch.getContext().getId(), launch2.getContext().getId() );
        assertEquals(launch.getUser().getId(), launch2.getUser().getId() );
        assertEquals(launch.getLink().getId(), launch2.getLink().getId() );
        assertEquals(launch.getResult().getId(), launch2.getResult().getId() );
        assertEquals(launch.getService().getId(), launch2.getService().getId() );
        assertEquals(launch.getUser().isInstructor(), launch2.getUser().isInstructor());
        assertEquals(f.getProperty("context_title"), launch.getContext().getTitle());
        assertEquals(f.getProperty("lis_person_contact_email_primary"), launch.getUser().getEmail());
        assertEquals(f.getProperty("resource_link_title"), launch.getLink().getTitle());
        assertEquals(f.getProperty("lis_result_sourcedid"), launch.getResult().getSourceDID());
        assertEquals(f.getProperty("lis_outcome_service_url"), launch.getService().getURL());
    }

    @Test
    public void testLTI2LaunchEquivalence() {
        Properties f1 = fakePost1();
        Properties f2 = fakePost2();

        System.out.println("Starting LTI 2.0 Launch test");
        Launch launch2 = tsugi.getLaunch(f2);
        assertNotNull(launch2);
        assertFalse(launch2.getUser().isTenantAdmin());
        assertFalse(launch2.getUser().isRootAdmin());
        assertEquals(launch.getContext().getId(), launch2.getContext().getId() );
        assertEquals(launch.getUser().getId(), launch2.getUser().getId() );
        assertEquals(launch.getLink().getId(), launch2.getLink().getId() );
        assertEquals(launch.getResult().getId(), launch2.getResult().getId() );
        assertEquals(f1.getProperty("context_title"), launch.getContext().getTitle());
        assertEquals(f1.getProperty("lis_person_contact_email_primary"), launch.getUser().getEmail());
        assertEquals(f1.getProperty("resource_link_title"), launch.getLink().getTitle());
        assertEquals(f2.getProperty("custom_result_url"), launch2.getResult().getURL());
        assertEquals(launch.getUser().isInstructor(), launch2.getUser().isInstructor());
        // assertEquals(f1.getProperty("resource_link_description"), launch.getLink().getDescription());
    }

    @Test
    public void testSettings() {
        assumeNotNull(launch);
        Settings contextSettings = launch.getContext().getSettings();
        contextSettings.setSetting("zap", "1234");
        Settings linkSettings = launch.getLink().getSettings();
        try {
            linkSettings.setSettingsJson("{ \"abc\" : 123 }");
        } catch (IOException ex) {
            System.out.println("Unexpected exception parsing JSON");
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    public static Properties fakePost1() {
        Properties f = fakePostCore();
        f.setProperty("context_id", "456434513");
        f.setProperty("context_label", "SI106");
        f.setProperty("context_title", "Introduction to Programming");
        f.setProperty("lis_person_name_full", "Jane Instructor");
        f.setProperty("lis_person_name_family", "Instructor");
        f.setProperty("lis_person_name_given", "Jane");
        f.setProperty("lis_person_contact_email_primary", "inst@ischool.edu");
        f.setProperty("lis_person_sourcedid", "ischool.edu:inst");
        f.setProperty("lis_result_sourcedid", "e10e575674e68bbcd873e2962f5a138b");
        f.setProperty("lis_outcome_service_url", "http://localhost:8888/tsugi/common/tool_consumer_outcome.php?b64=MTIzNDU6OjpzZWNyZXQ6Ojo=");
        f.setProperty("resource_link_title", "Activity: attend");
        f.setProperty("resource_link_description", "A weekly blog.");
        f.setProperty("roles", "Instructor");
        return f;
    }

    public static Properties fakePost2() {
        Properties f = fakePostCore();
        f.setProperty("custom_courseoffering_sourcedid", "456434513");
        f.setProperty("custom_courseoffering_label", "SI106");
        f.setProperty("custom_courseoffering_title", "Introduction to Programming");
        f.setProperty("custom_person_name_full", "Jane Instructor");
        f.setProperty("custom_person_name_family", "Instructor");
        f.setProperty("custom_person_name_given", "Jane");
        f.setProperty("custom_person_contact_email_primary", "inst@ischool.edu");
        f.setProperty("custom_person_sourcedid", "ischool.edu:inst");
        f.setProperty("custom_result_url", "http://localhost:8888/tsugi/common/result.php?id=1234567");
        f.setProperty("custom_resourcelink_title", "Activity: attend");
        f.setProperty("custom_resourcelink_description", "A weekly blog.");
        f.setProperty("custom_result_comment", "Nice work");
        f.setProperty("custom_result_resultscore", "0.9");
        f.setProperty("custom_membership_role", "Instructor");
        return f;
    }
   
    public static Properties fakePostCore() {
        Properties f = new Properties();
        f.setProperty("user_id", "292832126");
        f.setProperty("resource_link_id", "667587732");
        f.setProperty("tool_consumer_info_product_family_code", "ims");
        f.setProperty("tool_consumer_info_version", "1.1");
        f.setProperty("tool_consumer_instance_guid", "lmsng.ischool.edu");
        f.setProperty("tool_consumer_instance_description", "University of Information");
        f.setProperty("custom_assn", "mod/attend/index.php");
        f.setProperty("custom_due", "2016-12-12 10:00:00.5");
        f.setProperty("custom_timezone", "Pacific/Honolulu");
        f.setProperty("custom_penalty_time", "86400");
        f.setProperty("custom_penalty_cost", "0.2");
        f.setProperty("oauth_callback", "about:blank");
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
        f.setProperty("ext_submit", "Finish Launch");
        return f;
    }

}
