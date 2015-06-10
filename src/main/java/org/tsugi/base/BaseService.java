
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Service;

import org.apache.commons.lang3.StringUtils;

/**
 * This is for a service entry for services with long-lived service 
 * end points.   This is not used for URI-Based endpoints that are
 * common in LTI 2.x.
 */
public class BaseService implements Service {

    public Long id;
    public String URL;

    /**
     * Constructor
     */
    public BaseService(Properties row)
    {
        // Note - not all rows will have a service - this may throw NPE
        id = new Long(row.getProperty("service_id"));
        URL = StringUtils.stripToNull(row.getProperty("service_url"));
    }

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
