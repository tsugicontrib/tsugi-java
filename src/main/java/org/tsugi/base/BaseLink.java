
package org.tsugi.base;

import org.tsugi.Link;
import org.tsugi.Result;

/**
 */
public abstract class BaseLink implements Link {

    public Long id;
    public String title;
    public Result result;

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

}
