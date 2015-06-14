
package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Launch;
import org.tsugi.User;

import org.apache.commons.lang3.StringUtils;

/**
 * The base implementation for the User interface
 */
public class BaseUser implements User {

    public Launch launch;
    public Long id;
    public String email;
    public String displayname;
    public Integer role;
    public boolean mentor;  // Not yet supported
    public boolean instructor;
    public boolean tenantAdmin;
    public boolean rootAdmin;

    /*
     * Constructor
     */
    public BaseUser(Launch launch, Properties row)
    {
        this.launch = launch;
        id = new Long(row.getProperty("user_id"));
        email = StringUtils.stripToNull(row.getProperty("user_email"));
        displayname = StringUtils.stripToNull(row.getProperty("user_displayname"));
        role = new Integer(row.getProperty("role"));
        mentor = false;
        instructor = role >= INSTRUCTOR_ROLE;;
        tenantAdmin = role >= TENANT_ADMIN_ROLE;
        rootAdmin = role >= ROOT_ADMIN_ROLE;
    }

    public Launch getLaunch()
    {
        return launch;
    }

    public Long getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    public String getDisplayname()
    {
        return displayname;
    }

    public boolean isMentor()
    {
        return mentor;
    }

    public boolean isInstructor()
    {
        return instructor;
    }

    public boolean isTenantAdmin()
    {
        return tenantAdmin;
    }

    public boolean isRootAdmin()
    {
        return rootAdmin;
    }


}
