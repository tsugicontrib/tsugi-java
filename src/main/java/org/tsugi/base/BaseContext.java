
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Launch;
import org.tsugi.Context;
import org.tsugi.Settings;

import org.apache.commons.lang3.StringUtils;

/**
 * This is a class to provide access to the resource context level data.
 *
 * This data comes from the LTI launch from the LMS. 
 * A context is the equivalent of a "class" or course.   A context
 * has a roster of users and each user has a role within the context.
 * A launch may or may not contain a context.  If there
 * is a link without a context, it is a "system-wide" link
 * like "view profile" or "show all courses"
 *
 */

public class BaseContext implements Context {

    // TODO: - $Context->lang - The context language choice.

    public Launch launch = null;
    public Long id;
    public String title;
    public Settings settings;

    /**
     * Constructor 
     */
    public BaseContext(Launch launch, Properties row, Settings settings)
    {
        this.launch = launch;
        id = new Long(row.getProperty("context_id"));
        title = StringUtils.stripToNull(row.getProperty("context_title"));
        this.settings = settings;
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

    public Settings getSettings()
    {
        return settings;
    }

}
