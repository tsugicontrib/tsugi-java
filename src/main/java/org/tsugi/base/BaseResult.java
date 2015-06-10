
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Result;
import org.tsugi.Service;

import org.apache.commons.lang3.StringUtils;

/**
 */
public class BaseResult implements Result {

    Long id;
    Double grade;
    String URL;
    String sourcedid;
    Service service;

    /**
     * Constructor - requires result_id
     */
    public BaseResult(Properties row, Service service) 
    {
        id = new Long(row.getProperty("result_id"));
        String sgrade = StringUtils.stripToNull(row.getProperty("grade"));
        if ( sgrade == null ) {
            grade = null;
        } else {
            grade = new Double(sgrade);
        }
        URL = StringUtils.stripToNull(row.getProperty("result_url"));
        sourcedid = StringUtils.stripToNull(row.getProperty("sourcedid"));
        this.service = service;
    }

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
     * The result URL (LTI 2.x) for this result.  May be null.
     */
    public String getSourceDID()
    {
        return sourcedid;
    }

    /**
     * The Service associated with this result (LTI 1.x)
     */
    public Service getService()
    {
        return service;
    }

}
