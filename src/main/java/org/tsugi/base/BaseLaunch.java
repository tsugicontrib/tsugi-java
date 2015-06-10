
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.*;
import org.tsugi.util.TsugiUtils;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;
import net.oauth.signature.OAuthSignatureMethod;

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
     * Check the OAuth Signature
     */
    public boolean checkOAuthSignature(HttpServletRequest request, String oauth_secret, String oauth_consumer_key)
    {
        String URL = TsugiUtils.getOurServletPath(request);
        OAuthMessage oam = OAuthServlet.getMessage(request, URL);
        OAuthValidator oav = new SimpleOAuthValidator();
        OAuthConsumer cons = new OAuthConsumer("about:blank#OAuth+CallBack+NotUsed", oauth_consumer_key,oauth_secret, null);

        OAuthAccessor acc = new OAuthAccessor(cons);

        base_string = null;
        error_message = null;
        try {
            base_string = OAuthSignatureMethod.getBaseString(oam);
        } catch (Exception e) {
            base_string = null;
        }

        try {
            oav.validateMessage(oam, acc);
        } catch (Exception e) {
            error_message = "Provider failed to validate message";
            return false;
        }
        return true;
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
