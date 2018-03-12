
package org.tsugi;

/**
 * This is for a service entry for services with long-lived service end points.   
 */
public interface Service {

    /**
     * Get the launch associated with this object
     * @return The launch object
     */
    public Launch getLaunch();

    /**
     * The integer primary key for this service within this Tsugi instance
     * @return The identifier for this service of the instance
     */
    public Long getId();

    /**
     * URL for the service link - should not be null
     * @return The URL for the service link
     */
    public String getURL();

}
