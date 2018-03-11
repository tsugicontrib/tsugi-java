
package org.tsugi;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;;

/**
 * This captures all of the data associated with the LTI Launch.
 */

public interface Launch {

    /**
     * Get the HttpRequest associated with the launch.
     * @return The HttpRequest associated with the launch
     */
    public HttpServletRequest getRequest();

    /**
     * Get the HttpResponse associated with the launch.
     * @return The HttpResponse associated with the launch
     */
    public HttpServletResponse getResponse();

    /**
     * Get the HttpSession associated with the launch.
     * @return The HttpSession associated with the launch
     */
    public HttpSession getSession();

    /**
     * Get the User associated with the launch.
     * @return The User associated with the launch 
     */
    public User getUser();

    /**
     * Get the Context associated with the launch.
     * @return The Context associated with the launch 
     */
    public Context getContext();

    /**
     * Get the Link associated with the launch.
     * @return The Link associated with the launch
     */
    public Link getLink();

    /**
     * Get the Result associated with the launch.
     * @return The Result associated with the launch
     */
    public Result getResult();

    /**
     * Get the Service associated with the launch.
     * @return The Service associated with the launch
     */
    public Service getService();

    /**
     * Return the database connection used by Tsugi.
     * @return The database connection
     */
    public Connection getConnection();

    /**
     * Return the database prefix used by Tsugi.
     * @return The database prefix
     */
    public String getPrefix();

    // Deprecated for now
    // /**
    //  * Return the database helper class used by Tsugi.
    //  * @return The database helper class
    //  */
    // public Database DB();

    /**
     * Return the database helper class used by Tsugi.
     * @return The database helper class
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
     * @return The error message if something went wrong with the setup
     */
    public String getErrorMessage();

    /**
     * Indicate if this request is completely handled
     *
     * This is used as follows:
     *
     *<pre><code>
     *      public void doPost (...)
     *      Launch launch = tsugi.getLaunch(req, res);
     *      if ( launch.isComplete() ) return;
     *</code></pre>
     *
     * This allows the Tsugi framework to do things like redirect back
     * to itself.
     * @return True if this request is completely handled
     */
    public boolean isComplete();

    /**
     * Indicate if this request is valid
     *
     * This fails if the LTI Launch was malformed or the session data
     * is corrupted.
     * @return True if this request is valid
     */
    public boolean isValid();

    /** 
     * Get a GET URL to the current servlet
     *
     * We abstract this in case the framework needs to 
     * point to a URL other than the URL in the request
     * object.  This URL should be used for AJAX calls
     * to dynamic data in JavaScript.
     * @param path Specify the path for the GET URL
     * @return A GET URL to the current servlet
     **/
    public String getGetUrl(String path);

    /** 
     * Get a POST URL to the current servlet
     *
     * We abstract this in case the framework needs to 
     * point to a URL other than the URL in the request
     * object.
     * @param path Specify the path for the POST URL
     * @return A POST URL to the current servlet
     **/
    public String getPostUrl(String path);

    /** 
     * Redirect to a path - can be null
     * @param path Specify the path for the URL
     * @return Redirect to a path 
     **/
    public void postRedirect(String path);

    /** 
     * Get any needed hidden form fields
     *
     * This will be properly formatted HTML - most likely one
     * or more input type="hidden" fields - the framework
     * may use this to help it maintain context across
     * request / response cycles.
     *
     * @return String Text to include in a form.  May be the 
     * empty string if nothing is needed by the framework.
     **/
    public String getHidden();

    /** 
     * Get a URL to the 'static' folder within this servlet
     *
     * We abstract this because this might be stored in a
     * CDN for this application. 
     * TODO: Define the property for this
     * @return A URL to the 'static' folder within this servlet
     **/
    public String getStaticUrl();

    /** 
     * Get a URL to a system-wide spinner image
     * @return A URL to a system-wide spinner image
     **/
    public String getSpinnerUrl();

}
