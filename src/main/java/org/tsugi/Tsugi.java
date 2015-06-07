
package org.tsugi;

import java.sql.Connection;

/**
 * This in effect the Tsugi "Session Context" that provides applications with 
 * the implementations of the Tsugi APIs once properly provisioned and set up.
 */
public interface Tsugi {
   /**
     * Return the version of this application.
     */
    public String getVersion();

   /**
     * Return the database connection used by Tsugi.
     */
    public Connection getConnection();
}
