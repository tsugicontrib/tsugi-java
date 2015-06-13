
package org.tsugi;

/**
 * The data structure for a grade.
 *
 * Tsugi insures that this always exists with a local way of
 * persisting a grade even if the server that launched us has
 * no way of handling grades from us.
 */
public interface Result {

    /**
     * Get the launch associated with this object
     */
    public Launch getLaunch();

    /**
     * The integer primary key for this result within this Tsugi instance
     */
    public Long getId();

    /**
     * Current grade for the resource link
     */
    public Double getGrade();

    /**
     * Current comment for the resource link
     */
    public String getComment();

    /**
     * The result URL (LTI 2.x) for this result.  May be null.
     *
     * @return String This is either the fully-qualified resource URL
     * or null if there is no LTI 2.x resource URL available for the
     * launch.
     */
    public String getURL();

    /**
     * The SourceDID associated with this result (LTI 1.x)
     *
     * @return String This is either the LTI 1.x SourceDID
     * or null.
     */
    public String getSourceDID();

    /**
     * The Service associated with this result (LTI 1.x)
     * @return Service This is either the LTI 1.x Result service
     * or null.
     */
    public Service getService();

}
