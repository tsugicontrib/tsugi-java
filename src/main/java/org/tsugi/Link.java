
package org.tsugi;

/**
 * This captures the information around the LTI resource_link
 */
public interface Link {

    /**
     * Get the launch associated with this object
     * @return The launch object
     */
    public Launch getLaunch();

    /**
     * The integer primary key for the user within this instance of Tsugi
     * @return The identifier for the user within the instance
     */
    public Long getId();

    /**
     * The link title.
     * @return The link title
     */
    public String getTitle();

    /**
     * The result associated with this link.
     * @return The result associated with this link
     */
    public Result getResult();

    /**
     * The link settings
     * @return The link settings
     */
    public Settings getSettings();

}
