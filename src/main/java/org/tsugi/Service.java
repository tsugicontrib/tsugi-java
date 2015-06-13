
package org.tsugi;

/**
 * This is for a service entry for services with long-lived service end points.   
 */
public interface Service {

    /**
     * Get the launch associated with this object
     */
    public Launch getLaunch();

    /**
     * The integer primary key for this service within this Tsugi instance
     */
    public Long getId();

    /**
     * URL for the service link - should not be null
     */
    public String getURL();

}
