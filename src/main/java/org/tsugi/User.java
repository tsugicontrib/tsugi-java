
package org.tsugi;

/**
 */
public interface User {
    /**
     * The integer primary key for this user in the 'lti_user' table.
     */
    public Long getId();

    /**
     * The user's email
     */
    public String getEmail();

    /**
     * The user's display name
     */
    public String getDisplayname();

    /**
     * Is the user an instructor?
     */
    public boolean isInstructor();

}
