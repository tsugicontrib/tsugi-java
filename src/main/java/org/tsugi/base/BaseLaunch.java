
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.*;

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

}
