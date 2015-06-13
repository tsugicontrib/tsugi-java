
package org.tsugi;

/**
 * This is a class to provide access to the data for the logged-in user
 *
 * This data comes from the LTI launch from the LMS. 
 * If this is an anonymous launch the User will be null.
 */
public interface User {
    /**
     * The integer primary key for this user in this instance of Tsugi.
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
