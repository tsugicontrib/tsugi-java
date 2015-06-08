
package org.tsugi.base;

import org.tsugi.Result;
import org.tsugi.Service;

/**
 */
public abstract class BaseResult implements Result {

    Long id;
    Double grade;
    String URL;
    Service service;

    /**
     * The integer primary key for this result in the 'lti_result' table.
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Current grade for the link
     */
    public Double getGrade()
    {
        return grade;
    }

    /**
     * The result URL (LTI 2.x) for this result.  May be null.
     */
    public String getURL()
    {
        return URL;
    }

    /**
     * The Service associated with this result (LTI 1.x)
     */
    public Service getService()
    {
        return service;
    }

}
