
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Context;
import org.tsugi.User;
import org.tsugi.Link;
import org.tsugi.Result;
import org.tsugi.Launch;

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

    public BaseLaunch(Connection connection, User user, Context context, Link link, Result result)
    {
        this.user = user;
        this.connection = connection;
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
     * Return the database connection used by Tsugi.
     */
    public Connection getConnection()
    {
        return connection;
    }

}
