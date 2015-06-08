
package org.tsugi.base;

import org.tsugi.Service;

/**
 * This is for a service entry for services with long-lived service 
 * end points.   This is not used for URI-Based endpoints that are
 * common in LTI 2.x.
 */
public abstract class BaseService implements Service {

    public Long id;
    public String URL;

    /**
     * The integer primary key for this result in the 'lti_service' table.
     */
    public Long getId()
    {
        return id;
    }

    /**
     * URL for the link
     */
    public String getURL()
    {
        return URL;
    }

}
