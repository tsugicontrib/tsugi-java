
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Link;
import org.tsugi.Result;
import org.tsugi.Settings;

import org.apache.commons.lang3.StringUtils;

/**
 */
public class BaseLink implements Link {

    public Long id;
    public String title;
    public Result result;
    public Settings settings;

    /**
     * Constructor
     */
    public BaseLink(Properties row, Result result, Settings settings)
    {
        id = new Long(row.getProperty("link_id"));
        title = StringUtils.stripToNull(row.getProperty("link_title"));
        this.result = result;
        this.settings = settings;
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
