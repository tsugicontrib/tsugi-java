
package org.tsugi.base;

import org.tsugi.Context;
import org.tsugi.User;
import org.tsugi.Link;
import org.tsugi.Launch;

import java.sql.Connection;

/**
 * This an opinionated LTI class that defines how Tsugi tools 
 * interact with LTI.
 */

public abstract class BaseLaunch implements Launch {

    public User user;
    public Link link;
    public Context context;
    public Connection connection;

    public BaseLaunch()
    {
        user = null;
        link = null;
        context = null;
        connection = null;
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
     * Return the database connection used by Tsugi.
     */
    public Connection getConnection()
    {
        return connection;
    }

}
