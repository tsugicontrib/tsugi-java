
package org.tsugi;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;

/**
 * This in effect the Tsugi "Launch Factory" that allows an application to 
 * setup the launch for each request/response cycle.
 */
public interface Tsugi {

   /**
     * Return a database connection for use by Tsugi.
     *
     * This should only be used for maintenance and non-launch 
     * related database access.  The normal path is to get the 
     * launch and then pull the connection from the Launch structure.
     * this way we don't make more connections than we need.
     */
    public Connection getConnection();

   /**
     * Get the launch information for the current session
     */
    public Launch getLaunch(HttpServletRequest req, HttpServletResponse res);

   /**
     * Get the launch information for the current session
     * 
     * This method should only be used in unit tests and will
     * throw a RuntimeException if used when unit tests are not
     * running.
     */
    public Launch getLaunch(Properties props);
}
