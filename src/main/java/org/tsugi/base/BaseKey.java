
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Launch;
import org.tsugi.Key;

import org.apache.commons.lang3.StringUtils;

public class BaseKey implements Key {

    public Launch launch = null;
    public Long id;
    public String title;

    /**
     * Constructor 
     * @param launch Represents a Launch object
     * @param row Represents a Properties object
     */
    public BaseKey(Launch launch, Properties row)
    {
        this.launch = launch;
        id = new Long(row.getProperty("key_id"));
        title = null;
    }

    public Launch getLaunch()
    {
        return launch;
    }

    public Long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

}
