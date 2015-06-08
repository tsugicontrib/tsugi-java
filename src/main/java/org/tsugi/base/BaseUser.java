
package org.tsugi.base;

import org.tsugi.User;
/**
 */
public abstract class BaseUser implements User {

    public Long id;
    public String email;
    public String displayname;
    public boolean instructor;

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
