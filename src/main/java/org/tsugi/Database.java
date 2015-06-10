
package org.tsugi;

import java.util.List;

import java.util.Properties;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is a set of database convienence methods
 *
 * The PDOX class adds a number of non-trivial convienence methods
 * to the underlying PHP PDO class.   These methods combine several
 * PDO calls into a single call for common patterns and add far more
 * extensive error checking and simpler error handling.
 *
 * The primary value is in the queryReturnError() function which
 * combines prepare() and execute() as well as adding in extensive
 * error checking.
 * It turns out that to properly check all of the return values
 * and possible errors which can happen using prepare() and execute()
 * is really challenging and not even obvious from the PDO documentation.
 * So we have collected all that wisdom into this method and then use
 * it throughout Tsugi.
 *
 * The rest of the methods are convienence methods to combine common
 * multi-step operations into a single call to make tool code more readable.
 *
 * While this seems to be bending over backwards, It makes the calling
 * code very succinct as follows:
 *
 *     $stmt = $PDOX->queryDie(
 *         "INSERT INTO .... ",
 *         array('SHA' => $userSHA, ... )
 *     );
 *     if ( $stmt->success) $profile_id = $PDOX->lastInsertId();
 *
 * Whilst many of these methods seem focused on calling the die() function,
 * the only time that die() is called is when there is an SQL syntax error.
 * Not finding a record is non-fatal.  In general SQL syntax errors only
 * happen during development (if you are doing it right) so you might as
 * well die() if there is an SQL syntax error as it most likely indicates
 * a coding bug rather than a runtime user error or missing data problem.
 *
 */
public interface Database {

    /**
     * Prepare and execute an SQL query and return the error
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @param error_log Indicates whether or not errors are to be logged. Default is TRUE.
     * @return \ResultSet  This is either the real ResultSet from the query call
     * or an SQLErrror is thrown.
     */
    public Object queryReturnError(String sql, String[] arr, boolean error_log)
        throws SQLException;

    /**
     * Prepare and execute an SQL query or die() in the attempt.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return \ResultSet  This is either the real PDOStatement from the prepare() call
     * or the error is logged and a RuntimeException is thrown.
     */
    public ResultSet queryDie(String sql, String[] arr);

    /**
     * Prepare and execute an SQL query and retrieve a single row.
     *
     * @param sql The SQL to execute in a string.  If the SQL is badly formed this function will die.
     * @param arr An optional array of the substitition values if needed by the query
     * @return Properties This is either the row that was returned or null if no row was returned.
     * If anything goes wrong the errors are logged and a runtime exception is thrown.
     */
    public Properties rowDie(String sql, String[] arr);

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
    public List<Properties> allRowsDie(String sql, String[] arr);
}
