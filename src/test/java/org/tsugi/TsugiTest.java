package org.tsugi;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.tsugi.Tsugi;
import org.tsugi.TsugiFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;


public class TsugiTest {

    public final String unitTestKey = "unit-test-xyzzy-key";
    public final String unitTestKeySha256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(unitTestKey);
    Tsugi tsugi = null;
    Connection c = null;
    DatabaseMetaData meta = null;
    boolean localhost = false;
    Long key_id = null;;

    @Before
    public void setUp() throws Exception {
        tsugi = TsugiFactory.getTsugi();
        if ( tsugi == null ) return;
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
    public void testVersion() {
        System.out.println("YOYO "+tsugi.getVersion());
    }

}
