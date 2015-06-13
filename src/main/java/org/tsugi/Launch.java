
package org.tsugi;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;;

/**
 * This captures all of the data associates with
 * interact with LTI.  This
 */

public interface Launch {

    /**
     * Get the HttpRequest associated with the launch.
     */
    public HttpServletRequest getRequest();

    /**
     * Get the HttpResponse associated with the launch.
     */
    public HttpServletResponse getResponse();

    /**
     * Get the HttpSession associated with the launch.
     */
    public HttpSession getSession();

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

    // Deprecated for now
    // /**
    //  * Return the database helper class used by Tsugi.
    //  */
    // public Database DB();

    /**
     * Return the database helper class used by Tsugi.
     */
    public Output getOutput();

    /**
     * Get the base string from the launch.
     *
     * @return This is null if it is not the original launch.
     * it is not restored when the launch is restored from 
     * the session.
     */
    public String getBaseString();

    /**
     * Get the error message if something went wrong with the setup
     */
    public String getErrorMessage();

    /**
     * Indicate if this request is completely handled
     *
     * This is used as follows:
     *
     *      public void doPost (...)
     *      Launch launch = tsugi.getLaunch(req, res);
     *      if ( launch.isComplete() ) return;
     *
     * This allows the Tsugi framework to do things like redirect back
     * to itself.
     */
    public boolean isComplete();

    /**
     * Indicate if this request is valid
     *
     * This fails if the LTI Launch was malformed or the session data
     * is corrupted.
     */
    public boolean isValid();

}
