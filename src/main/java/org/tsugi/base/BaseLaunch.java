
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.*;
import org.tsugi.util.TsugiUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Connection;

/**
 * This an opinionated LTI class that defines how Tsugi tools 
 * interact with LTI.
 */

public class BaseLaunch implements Launch {

    public HttpServletRequest request = null;
    public HttpServletResponse response = null;
    public HttpSession session = null;

    public Connection connection = null;
    public String prefix = null;
    public User user = null;
    public Context context = null;
    public Link link = null;
    public Result result = null;
    public Output output = null;
    public Database database = null;

    public String base_string = null;
    public String error_message = null;

    public boolean valid = false;
    public boolean complete = false;

    /**
     * Basic constructor 
     */
    public BaseLaunch() { }

    /**
     * Get the HttpRequest associated with the launch.
     */
    public HttpServletRequest getRequest()
    {
        return request;
    }

    /**
     * Get the HttpResponse associated with the launch.
     */
    public HttpServletResponse getResponse()
    {
        return response;
    }

    /**
     * Get the HttpSession associated with the launch.
     */
    public HttpSession getSession()
    {
        if ( session != null ) return session;
        if ( request == null ) return null;
        session = request.getSession();
        return session;
    }

    /**
     * Get the User associated with the launch.
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Get the Context associated with the launch.
     */
    public Context getContext()
    {
        return context;
    }

    /**
     * Get the Link associated with the launch.
     */
    public Link getLink()
    {
        return link;
    }

    /**
     * Get the Result associated with the launch.
     */
    public Result getResult()
    {
        return result;
    }

    /**
     * Get the Service associated with the launch.
     */
    public Service getService()
    {
        if ( result == null ) return null;
        return result.getService();
    }

    /**
     * Return the database connection used by Tsugi.
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Get the database table prefix associated with the launch.
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Return the database helper used by Tsugi.
     */
    public Database DB()
    {
        return database;
    }

    /**
     * Return the output helper used by Tsugi.
     */
    public Output getOutput()
    {
        return output;
    }

    /**
     * Get the base string
     */
    public String getBaseString()
    {
        return base_string;
    }

    /**
     * Get the error message
     */
    public String getErrorMessage()
    {
        return error_message;
    }

    /**
     * Indicate if this request is completely handled
     */
    public boolean isComplete()
    {
        return complete;
    }

    /**
     * Indicate if this request is valid
     */
    public boolean isValid()
    {
        return valid;
    }


}
