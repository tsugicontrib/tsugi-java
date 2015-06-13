
package org.tsugi;

/**
 */
public interface Link {
    /**
     * The integer primary key for the user within this instance of Tsugi
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
