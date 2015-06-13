
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Launch;
import org.tsugi.Result;
import org.tsugi.Service;

import org.apache.commons.lang3.StringUtils;

/**
 * The base implementation for the Result interface
 */
public class BaseResult implements Result {

    public Launch launch;
    public Long id;
    public Double grade;
    public String comment;
    public String URL;
    public String sourcedid;
    public Service service;

    /**
     * Constructor - requires result_id
     */
    public BaseResult(Launch launch, Properties row, Service service) 
    {
        this.launch = launch;
        id = new Long(row.getProperty("result_id"));
        String sgrade = StringUtils.stripToNull(row.getProperty("grade"));
        if ( sgrade == null ) {
            grade = null;
        } else {
            grade = new Double(sgrade);
        }
        String comment = StringUtils.stripToNull(row.getProperty("result_comment"));
        URL = StringUtils.stripToNull(row.getProperty("result_url"));
        sourcedid = StringUtils.stripToNull(row.getProperty("sourcedid"));
        this.service = service;
    }

    public Launch getLaunch()
    {
        return launch;
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
     * Current comment for the link
     */
    public String getComment()
    {
        return comment;
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
