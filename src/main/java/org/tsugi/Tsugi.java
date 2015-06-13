
package org.tsugi;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;

/**
 * This creates or retrieves the launch data on each request.
 *
 * This called during every request/response cycle.  When it 
 * receives an LTI launch request, it sets up all of the data
 * associated with the launch.  On successive requests, the 
 * launch data is restored from the session.
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
