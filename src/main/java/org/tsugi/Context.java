
package org.tsugi;

/**
 * This is a class to provide access to the context level data.
 *
 * This data comes from the LTI launch from the LMS. 
 * A context is the equivalent of a "class" or course.   A context
 * has a roster of users and each user has a role within the context.
 * A launch may or may not contain a context.  If there
 * is a link without a context, it is a "system-wide" link
 * like "view profile" or "show all courses"

 */
public interface Context {

    // TODO: - $Context->lang - The context language choice.

    /**
     * Get the launch associated with this object
     * @return The launch object
     */
    public Launch getLaunch();

    /**
     * The integer primary key for this context across this Tsugi instance
     * @return The instance identifier
     */
    public Long getId();

    /**
     * The context title
     * @return The context title
     */
    public String getTitle();

    /**
     * The context settings
     * @return The context settings
     */
    public Settings getSettings();
}
