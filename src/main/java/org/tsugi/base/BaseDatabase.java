
package org.tsugi;

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
    public void BaseDatabase(Connection c, String prefix)
    {
        this.c = c;
        this.prefix = prefix;
    }

    /**
     * Prepare and execute an SQL query and return the error
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @param error_log Indicates whether or not errors are to be logged. Default is TRUE.
     * @return \ResultSet  This is either the real ResultSet from the query call
     * or an SQLErrror is thrown.
     */
    public ResultSet queryReturnError(String sql, List<String> arr, boolean error_log)
        throws SQLException
    {
        sql = setPrefix(sql);
        log.trace(sql);

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            boolean inserting = StringUtils.trim(sql).toLowerCase().startsWith("insert");

            if ( inserting ) {
                stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                stmt = c.prepareStatement(sql);
            }

            if ( arr != null ) for ( int i=0; i < arr.size(); i++) {
                stmt.setString(i+1, arr.get(i));
            }

            if ( inserting ) {
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
            } else {
                rs = stmt.executeQuery();
            }
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
     * Prepare and execute an SQL query or die() in the attempt.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return \ResultSet  This is either the real PDOStatement from the prepare() call
     * or the error is logged and a RuntimeException is thrown.
     */
    public ResultSet queryDie(String sql, List<String> arr)
    {
        try {
            ResultSet rs = queryReturnError(sql, arr, true);
            return rs;
        } catch ( SQLException e ) {
            log.error("Failed SQL: "+sql);
            throw new RuntimeException("Failed SQL: "+sql);
        }
    }

    /**
     * Prepare and execute an SQL insert query and return the new primary key.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return Long This is the new primary key of the inserted row if the insert was successful.
     * If anything goes wrong the errors are logged and a runtime exception is thrown.
     */
    public Long insertDie(String sql, List<String> arr)
    {
        ResultSet rs = queryDie(sql, arr);
        try {
            if (rs.next()) {
                Long retval = rs.getLong(1);
                System.out.println("Inserted new key="+retval);
                rs.close();
                return retval;
            } else {
                throw new RuntimeException("Insert failed: "+sql);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("RecordSet close failed:"+sql);
        }
    }

    /**
     * Prepare and execute an SQL query and retrieve a single row.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return Properties This is either the row that was returned or null if no row was returned.
     * If anything goes wrong the errors are logged and a runtime exception is thrown.
     */
    public Properties rowDie(String sql, List<String> arr)
    {
        ResultSet rs = queryDie(sql, arr);
        try {
            if (rs.next()) {
                Properties retval =  resultsetToProperties(rs);
                rs.close();
                return retval;
            } else {
                rs.close();
                return new Properties();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("RecordSet close failed:"+sql);
        }
    }

    /**
     * Prepare and execute an SQL query and retrieve all the rows as an array
     *
     * While this might seem like a bad idea, the coding style for Tsugi is
     * to make every query a paged query with a limited number of records to
     * be retrieved to in most cases, it is quite reasonable to retrieve
     * 10-30 rows into an array.
     *
     * If code wants to stream the results of a query, they should do their
     * own query and loop through the rows in their own code.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @param error_log Indicates whether or not errors are to be logged. Default is TRUE.
     * @return List<Propertiesi> This is either the rows that was returned or null if no rows were returned.
     * If anything goes wrong the errors are logged and a runtime exception is thrown.
     */
    public List<Properties> allRowsDie(String sql, List<String> arr)
    {
        return null;
    }


    /*
     ** Scan a result set and return a Properties object with entries for each column.
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
