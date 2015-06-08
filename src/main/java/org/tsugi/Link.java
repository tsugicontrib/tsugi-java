
package org.tsugi;

/**
 */
public interface Link extends Settings {
    /**
     * The integer primary key for this user in the 'lti_link' table.
     */
    public Long getId();

    /**
     * The link title.
     */
    public String getTitle();

    /**
     * Current grade for the link
     */
    public Double getGrade();

    /**
     * The result associated with this link.
     */
    public Result getResult();

}
