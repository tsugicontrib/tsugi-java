
package org.tsugi;

/**
 */
public interface Link {
    /**
     * The integer primary key for this user in the 'lti_link' table.
     */
    public Long getId();

    /**
     * The link title.
     */
    public String getTitle();

    /**
     * The result associated with this link.
     */
    public Result getResult();

    /**
     * The link settings
     */
    public Settings getSettings();

}
