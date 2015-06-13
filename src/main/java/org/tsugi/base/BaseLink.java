
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Launch;
import org.tsugi.Link;
import org.tsugi.Result;
import org.tsugi.Settings;

import org.apache.commons.lang3.StringUtils;

/**
 * The base implementation for the Link interface
 */
public class BaseLink implements Link {

    public Launch launch;
    public Long id;
    public String title;
    public Result result;
    public Settings settings;

    /**
     * Constructor
     */
    public BaseLink(Launch launch, Properties row, Result result, Settings settings)
    {
        id = new Long(row.getProperty("link_id"));
        title = StringUtils.stripToNull(row.getProperty("link_title"));
        this.result = result;
        this.settings = settings;
    }

    /**
     * Get the launch associated with this object
     */
    public Launch getLaunch()
    {
        return launch;
    }

    /**
     * The integer primary key for this user in the 'lti_link' table.
     */
    public Long getId()
    {
        return id;
    }

    /**
     * The link title.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * The result associated with this link.
     */
    public Result getResult()
    {
        return result;
    }

    /**
     * The context Settings
     */
    public Settings getSettings()
    {
        return settings;
    }

}
