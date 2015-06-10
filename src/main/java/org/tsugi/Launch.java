
package org.tsugi;

import java.sql.Connection;

/**
 * This an opinionated LTI class that defines how Tsugi tools 
 * interact with LTI.
 */

public interface Launch {

   /**
     * Get the User associated with the launch.
     */
    public User getUser();

   /**
     * Get the Context associated with the launch.
     */
    public Context getContext();

   /**
     * Get the Link associated with the launch.
     */
    public Link getLink();

   /**
     * Get the Result associated with the launch.
     */
    public Result getResult();

   /**
     * Return the database connection used by Tsugi.
     */
    public Connection getConnection();

}
