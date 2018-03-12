
package org.tsugi;

/**
 * This is a class to provide access to the data for the logged-in user
 *
 * This data comes from the LTI launch from the LMS. 
 * If this is an anonymous launch the User will be null.
 */
public interface User {

    public final int LEARNER_ROLE = 0;
    public final int INSTRUCTOR_ROLE = 1000;
    public final int TENANT_ADMIN_ROLE = 5000;
    public final int ROOT_ADMIN_ROLE = 10000;

    /**
     * Get the launch associated with this object
     * @return The launch object
     */
    public Launch getLaunch();

    /**
     * The integer primary key for this user in this instance of Tsugi.
     * @return The identifier for the user of the instance
     */
    public Long getId();

    /**
     * The user's email
     * @return The email address of the user
     */
    public String getEmail();

    /**
     * The user's display name
     * @return The display name of the user
     */
    public String getDisplayname();

    /**
     * Is the user a Mentor? (TBD)
     * @return True if the user is a mentor
     */
    public boolean isMentor();

    /**
     * Is the user an instructor?
     * @return True if the user is an instructor
     */
    public boolean isInstructor();

    /**
     * Is the user a Tenant Administrator?
     * @return True if the user is a tenant administrator
     */
    public boolean isTenantAdmin();

    /**
     * Is the user a Tsugi-wide Administrator?
     * @return True if the user is a Tsugi-wide administrator
     */
    public boolean isRootAdmin();

}
