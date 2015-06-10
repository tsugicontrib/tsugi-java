
package org.tsugi.base;

import java.util.Properties;
import java.util.List;

import org.tsugi.Database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseDatabase implements Database {

    private Log log = LogFactory.getLog(BaseDatabase.class);

    Connection c = null;
    String prefix = null;

    /**
     * Constructor 
     */
    public BaseDatabase(Connection c, String prefix)
    {
        this.c = c;
        this.prefix = prefix;
    }

    /**
     *
     */
    public ResultSet selectReturnError(String sql, List<String> arr)
        throws SQLException
    {
        sql = setPrefix(sql);
        log.trace(sql);

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            boolean inserting = StringUtils.trim(sql).toLowerCase().startsWith("insert");

            stmt = c.prepareStatement(sql);

            if ( arr != null ) for ( int i=0; i < arr.size(); i++) {
                stmt.setString(i+1, arr.get(i));
            }

            rs = stmt.executeQuery();
            stmt.close();
            return rs;
        // If the closes generate throws, we let them happen
        // http://stackoverflow.com/questions/321418/where-to-close-java-preparedstatements-and-resultsets
        } catch(SQLException ex) {
            if ( stmt != null ) stmt.close();
            if ( rs != null ) rs.close();
            throw ex;
        }
    }

    /**
     *
     */
    public Long insertReturnError(String sql, List<String> arr)
        throws SQLException
    {
        sql = setPrefix(sql);
        log.trace(sql);

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if ( arr != null ) for ( int i=0; i < arr.size(); i++) {
                stmt.setString(i+1, arr.get(i));
            }

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            Long retval = null;
            if (rs.next()) {
                retval = rs.getLong(1);
                System.out.println("Inserted new key="+retval);
            }
            rs.close();
            return retval;

        // If the closes generate throws, we let them happen
        // http://stackoverflow.com/questions/321418/where-to-close-java-preparedstatements-and-resultsets
        } catch(SQLException ex) {
            if ( stmt != null ) stmt.close();
            if ( rs != null ) rs.close();
            throw ex;
        }
    }

    /**
     *
     */
    public int updateReturnError(String sql, List<String> arr)
        throws SQLException
    {
        sql = setPrefix(sql);
        log.trace(sql);

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if ( arr != null ) for ( int i=0; i < arr.size(); i++) {
                stmt.setString(i+1, arr.get(i));
            }

            int retval = stmt.executeUpdate();
            stmt.close();
            return retval;
        // If the closes generate throws, we let them happen
        // http://stackoverflow.com/questions/321418/where-to-close-java-preparedstatements-and-resultsets
        } catch(SQLException ex) {
            if ( stmt != null ) stmt.close();
            if ( rs != null ) rs.close();
            throw ex;
        }
    }

    /**
     *
     */
    public ResultSet selectDie(String sql, List<String> arr)
    {
        try {
            ResultSet rs = selectReturnError(sql, arr);
            return rs;
        } catch ( SQLException e ) {
            log.error("Failed SQL: "+sql);
            throw new RuntimeException("Failed SQL: "+sql);
        }
    }

    /**
     *
     */
    public Long insertDie(String sql, List<String> arr)
    {
        try {
            Long retval = insertReturnError(sql,arr);
            if ( retval == null ) {
                throw new RuntimeException("INSERT did not produce key:"+sql);
            }
            return retval;
        } catch (SQLException ex) {
            throw new RuntimeException("RecordSet close failed: "+sql);
        }
    }

    /**
     *
     */
    public int updateDie(String sql, List<String> arr)
    {
        try {
            return updateReturnError(sql,arr);
        } catch (SQLException ex) {
            throw new RuntimeException("Update failed: "+sql);
        }
    }

    /**
     *
     */
    public Properties rowDie(String sql, List<String> arr)
    {
        ResultSet rs = selectDie(sql, arr);
        try {
            if (rs.next()) {
                Properties retval =  resultsetToProperties(rs);
                rs.close();
                return retval;
            } else {
                rs.close();
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("RecordSet close failed:"+sql);
        }
    }

    /**
     *
     */
    public List<Properties> allRowsDie(String sql, List<String> arr)
    {
        return null;
    }


    /**
     * Scan a result set and return a Properties object with entries for each column.
     */
    public static Properties resultsetToProperties(ResultSet rs)
        throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        Properties props = new Properties();
        for ( int i=1; i<=count; i++ ) {
            String key = rsmd.getColumnLabel(i);
            String value = rs.getString(i);
            if ( value == null ) value = "";
            props.setProperty(key,value);
        }
        return props;
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
