
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Launch;
import org.tsugi.User;

import org.apache.commons.lang3.StringUtils;

/**
 * The base implementation for the User interface
 */
public class BaseUser implements User {

    public Launch launch;
    public Long id;
    public String email;
    public String displayname;
    public boolean instructor;

    /*
     * Constructor
     */
    public BaseUser(Launch launch, Properties row)
    {
        this.launch = launch;
        id = new Long(row.getProperty("user_id"));
        email = StringUtils.stripToNull(row.getProperty("user_email"));
        displayname = StringUtils.stripToNull(row.getProperty("user_displayname"));
        instructor = "1".equals(row.getProperty("role"));
    }

    public Launch getLaunch()
    {
        return launch;
    }

    /**
     * The integer primary key for this user in the 'lti_user' table.
     */
    public Long getId()
    {
        return id;
    }

    /**
     * The user's email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * The user's display name
     */
    public String getDisplayname()
    {
        return displayname;
    }

    /**
     * Is the user an instructor?
     */
    public boolean isInstructor()
    {
        return instructor;
    }

}
