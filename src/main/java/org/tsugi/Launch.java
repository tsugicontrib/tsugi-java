
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
     * Return the database connection used by Tsugi.
     */
    public Connection getConnection();

   /**
     * Pull out a custom variable from the Launch session. Do not
     * include the "custom_" prefix - this is automatic.
     */
    public String customGet(String varname, String def);

}
