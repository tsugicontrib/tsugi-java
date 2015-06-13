
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Launch;
import org.tsugi.Service;

import org.apache.commons.lang3.StringUtils;

/**
 * The base implementation for the Service interface
 */
public class BaseService implements Service {

    public Launch launch;
    public Long id;
    public String URL;

    /**
     * Constructor
     */
    public BaseService(Launch launch, Properties row)
    {
        this.launch = launch;
        // Note - not all rows will have a service - this may throw NPE
        id = new Long(row.getProperty("service_id"));
        URL = StringUtils.stripToNull(row.getProperty("service"));
    }

    public Launch getLaunch()
    {
        return launch;
    }

    public Long getId()
    {
        return id;
    }

    public String getURL()
    {
        return URL;
    }

}
