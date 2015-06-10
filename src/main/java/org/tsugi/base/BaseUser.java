
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.User;

import org.apache.commons.lang3.StringUtils;

/**
 */
public class BaseUser implements User {

    public Long id;
    public String email;
    public String displayname;
    public boolean instructor;

    /*
     * Constructor
     */
    public BaseUser(Properties row)
    {
        id = new Long(row.getProperty("user_id"));
        email = StringUtils.stripToNull(row.getProperty("user_email"));
        displayname = StringUtils.stripToNull(row.getProperty("user_displayname"));
        instructor = "1".equals(row.getProperty("role"));
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
