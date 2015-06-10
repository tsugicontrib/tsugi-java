
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.*;
import org.tsugi.util.TsugiUtils;

import java.sql.Connection;

/**
 * This an opinionated LTI class that defines how Tsugi tools 
 * interact with LTI.
 */

public class BaseLaunch implements Launch {

    public Connection connection;
    public User user;
    public Context context;
    public Link link;
    public Result result;
    public Database database;

    public String base_string = null;
    public String error_message = null;

    public boolean valid = false;
    public boolean complete = false;

    public BaseLaunch(Connection connection, Database database, User user, Context context, Link link, Result result)
    {
        this.connection = connection;
        this.database = database;
        this.user = user;
        this.context = context;
        this.link = link;
        this.result = result;
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
     * Return the database helper used by Tsugi.
     */
    public Database DB()
    {
        return database;
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
