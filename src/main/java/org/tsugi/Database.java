
package org.tsugi;

import java.util.List;

import java.util.Properties;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is a set of database convienence methods
 * 
 * This is a reimplementation of the PDOX library in the PHP Tsugi
 * but it might not be the best solution since the JDBC 
 * PreparedStatement is more elegant and may not benefit from this
 * layer.  So hold off using these wrappers for a bit.
 */
public interface Database {

    /**
     * Get the launch associated with this object
     * @return The launch object
     */
    public Launch getLaunch();

    /**
     * Prepare and execute a SELECT query or die() in the attempt.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return \ResultSet  This is the ResultSet from the query or
     * or the error is logged and a RuntimeException is thrown.
     */
    public ResultSet selectDie(String sql, List<String> arr);

    /**
     * Prepare and execute a SELECT query and return the error
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return \ResultSet  This is either the real ResultSet from the query call
     * or an SQLErrror is thrown.
     * @throws An SQLException
     */
    public ResultSet selectReturnError(String sql, List<String> arr) throws SQLException;

    /**
     * Prepare and execute an INSERT primary key query and return the new primary key.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return Long This is the new primary key of the inserted row if the insert was successful.
     * If anything goes wrong the errors are logged and a runtime exception is thrown.
     */
    public Long insertDie(String sql, List<String> arr);

    /**
     * Prepare and execute an INSERT primary key query and return the new primary key.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return Long This is the new primary key of the inserted row if the insert was successful.
     * @throws An SQLException
     */
    public Long insertReturnError(String sql, List<String> arr) throws SQLException;

    /**
     * Prepare and execute an UPDATE, DELETE or INSERT without a primary key 
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return int The number of rows affected (may be zero)
     * If anything goes wrong the errors are logged and a runtime exception is thrown.
     */
    public int updateDie(String sql, List<String> arr);

    /**
     * Prepare and execute an UPDATE, DELETE or INSERT without a primary key 
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return int The number of rows affected (may be zero)
     * @throws An SQLException
     */
    public int updateReturnError(String sql, List<String> arr) throws SQLException;

    /**
     * Prepare and execute an SQL query and retrieve a single row.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return Properties This is either the row that was returned or null if no row was returned.
     * If anything goes wrong the errors are logged and a runtime exception is thrown.
     */
    public Properties rowDie(String sql, List<String> arr);

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
     * @return List This is either the rows that was returned or null if no rows were returned.
     * If anything goes wrong the errors are logged and a runtime exception is thrown.
     */
    public List<Properties> allRowsDie(String sql, List<String> arr);
}
