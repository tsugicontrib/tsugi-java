package org.tsugi.base;

import org.tsugi.Tsugi;
import org.tsugi.Launch;
import org.tsugi.util.TsugiUtils;
import org.tsugi.util.TsugiLTIUtils;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

import org.imsglobal.basiclti.BasicLTIUtil;

/*
 ** Implement the code that is common across implementations here. 
 */
public abstract class BaseTsugi implements Tsugi 
{

    private Log log = LogFactory.getLog(BaseTsugi.class);

    private String version = "201506070900";

    public String getVersion()
    {
        return version;
    }

   /**
     * Get the launch information for the current session
     */
    public Launch getLaunch(HttpServletRequest req, HttpServletResponse res)
    {
        Properties props = TsugiLTIUtils.getParameterProperties(req);
        return getLaunch(req, props, res);
    }

   /**
     * Get the launch information for the current session with only properties
     */
    public Launch getLaunch(Properties props)
    {
        return getLaunch(null, props, null);
    }

    public abstract Launch getLaunch(HttpServletRequest req, Properties props, HttpServletResponse res);


    /**
     * Pull the LTI POST data into our own data structure
     *
     * We follow our naming conventions that match the column names in
     * our lti_ tables.
     */
    public Properties extractPost(Properties i) {
        Properties o = new Properties();

        TsugiUtils.copy(o,"key_key",i,"oauth_consumer_key");
        TsugiUtils.copy(o,"nonce",i,"oauth_nonce");
        TsugiUtils.copy(o,"context_key",i,"context_id");
        TsugiUtils.copy(o,"link_key",i,"resource_link_id");
        TsugiUtils.copy(o,"user_key",i,"user_id");

        // Test for the required parameters.
        if ( o.getProperty("key_key") != null && o.getProperty("nonce") != null && o.getProperty("context_key") != null &&
            o.getProperty("link_key") != null  && o.getProperty("user_key") != null  ) {
            // OK To Continue
        } else {
            return null;
        }

        // LTI 1.x settings and Outcomes
        TsugiUtils.copy(o,"service",i,"lis_outcome_service_url");
        TsugiUtils.copy(o,"sourcedid",i,"lis_result_sourcedid");

        // LTI 2.x settings and Outcomes
        TsugiUtils.copy(o,"result_url",i,"custom_result_url");
        TsugiUtils.copy(o,"link_settings_url",i,"custom_link_settings_url");
        TsugiUtils.copy(o,"context_settings_url",i,"custom_context_settings_url");

        TsugiUtils.copy(o,"context_title",i,"context_title");
        TsugiUtils.copy(o,"link_title",i,"resource_link_title");

        // Getting email from LTI 1.x and LTI 2.x
        String email = i.getProperty("lis_person_contact_email_primary");
        if ( email == null ) i.getProperty("custom_person_email_primary");
        if ( email != null ) o.setProperty("user_email", email);

        // Displayname from LTI 2.x
        if ( i.getProperty("person_name_full") != null ) {
            TsugiUtils.copy(o,"user_displayname",i,"custom_person_name_full");
        } else if ( i.getProperty("custom_person_name_given") != null && i.getProperty("custom_person_name_family") != null ) {
            o.setProperty("user_displayname",i.getProperty("custom_person_name_given")+" "+i.getProperty("custom_person_name_family"));
        } else if ( i.getProperty("custom_person_name_given") != null ) {
            TsugiUtils.copy(o,"user_displayname",i,"custom_person_name_given");
        } else if ( i.getProperty("custom_person_name_family") != null ) {
            TsugiUtils.copy(o,"user_displayname",i,"custom_person_name_family");

        // Displayname from LTI 1.x
        } else if ( i.getProperty("lis_person_name_full") != null ) {
            TsugiUtils.copy(o,"user_displayname",i,"lis_person_name_full");
        } else if ( i.getProperty("lis_person_name_given") != null && i.getProperty("lis_person_name_family") != null ) {
            o.setProperty("user_displayname",i.getProperty("lis_person_name_given")+" "+i.getProperty("lis_person_name_family"));
        } else if ( i.getProperty("lis_person_name_given") != null ) {
            TsugiUtils.copy(o,"user_displayname",i,"lis_person_name_given");
        } else if ( i.getProperty("lis_person_name_family") != null ) {
            TsugiUtils.copy(o,"user_displayname",i,"lis_person_name_family");
        }

        // Trim out repeated spaces and/or weird whitespace from the user_displayname
        // if ( o.getProperty("user_displayname") ) {
            // TsugiUtils.copy(o,"user_displayname"] = trim(preg_replace("/\s+/", " ",$retval["user_displayname"]));
        // }

        // Get the role
        o.setProperty("role", "0");
        String roles = "";
        if ( i.getProperty("custom_membership_role") != null ) { // From LTI 2.x
            roles = i.getProperty("custom_membership_role");
        } else if ( i.getProperty("roles") != null ) { // From LTI 1.x
            roles = i.getProperty("roles");
        }

        if ( roles.length() > 0 ) {
            roles = roles.toLowerCase();
            if ( roles.indexOf("instructor") > 0 ) o.setProperty("role", "1");
            if ( roles.indexOf("administrator") > 0 ) o.setProperty("role", "1");
        }
        return o;
    }

}

