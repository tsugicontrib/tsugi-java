
package org.tsugi;

/**
 */
public interface Result {
    /**
     * The integer primary key for this result in the 'lti_result' table.
     */
    public Long getId();

    /**
     * Current grade for the link
     */
    public Double getGrade();

    /**
     * The result URL (LTI 2.x) for this result.  May be null.
     */
    public String getURL();

    /**
     * The Service associated with this result (LTI 1.x)
     */
    public Service getService();

}
