
package org.tsugi;

/**
 * This is for a service entry for services with long-lived service 
 * end points.   This is not used for URI-Based endpoints that are
 * common in LTI 2.x.
 */
public interface Service {
    /**
     * The integer primary key for this result in the 'lti_service' table.
     */
    public Long getId();

    /**
     * URL for the link
     */
    public String getURL();

}
