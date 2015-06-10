
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
     * Get the Service associated with the launch.
     */
    public Service getService();

    /**
     * Return the database connection used by Tsugi.
     */
    public Connection getConnection();

    /**
     * Return the database helper class used by Tsugi.
     */
    public Database DB();

    /**
     * Get the base string
     */
    public String getBaseString();

    /**
     * Get the error message
     */
    public String getErrorMessage();

    /**
     * Indicate if this request is completely handled
     */
    public boolean isComplete();

    /**
     * Indicate if this request is valid
     */
    public boolean isValid();

}
