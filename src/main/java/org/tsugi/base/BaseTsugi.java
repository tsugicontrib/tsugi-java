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

   /**
     * This should be private
     */
    private Launch getLaunch(HttpServletRequest req, Properties props, HttpServletResponse res)
    {
        if ( ! TsugiLTIUtils.isRequest(props) ) {
            return null;
        }
        Properties row = extractPost(props);
        String x = TsugiUtils.dumpProperties(row);
        System.out.println("Prpoperties:");
        System.out.println(x);
        return null;
    }


    public String customGet(String varname, String def)
    {
        return "Yo";
    }


    /**
     * Pull the LTI POST data into our own data structure
     *
     * We follow our naming conventions that match the column names in
     * our lti_ tables.
     */
    public Properties extractPost(Properties i) {
        Properties o = new Properties();

        setp(o,"key",i.getProperty("oauth_consumer_key"));
        setp(o,"nonce",i.getProperty("oauth_nonce"));
        setp(o,"context_id",i.getProperty("context_id"));
        setp(o,"link_id",i.getProperty("resource_link_id"));
        setp(o,"user_id",i.getProperty("user_id"));

        if ( o.getProperty("key") != null && o.getProperty("nonce") != null && o.getProperty("context_id") != null &&
            o.getProperty("link_id") != null  && o.getProperty("user_id") != null  ) {
            // OK To Continue
        } else {
            return null;
        }

        // LTI 1.x settings and Outcomes
        setp(o,"service",i.getProperty("lis_outcome_service_url"));
        setp(o,"sourcedid",i.getProperty("lis_result_sourcedid"));

        // LTI 2.x settings and Outcomes
        setp(o,"result_url",i.getProperty("custom_result_url"));
        setp(o,"link_settings_url",i.getProperty("custom_link_settings_url"));
        setp(o,"context_settings_url",i.getProperty("custom_context_settings_url"));

        setp(o,"context_title",i.getProperty("context_title"));
        setp(o,"link_title",i.getProperty("resource_link_title"));

        // Getting email from LTI 1.x and LTI 2.x
        String email = i.getProperty("lis_person_contact_email_primary");
        if ( email == null ) i.getProperty("custom_person_email_primary");
        setp(o,"user_email",email);

        // Displayname from LTI 2.x
        if ( i.getProperty("person_name_full") != null ) {
            setp(o,"user_displayname",i.getProperty("custom_person_name_full"));
        } else if ( i.getProperty("custom_person_name_given") != null && i.getProperty("custom_person_name_family") != null ) {
            setp(o,"user_displayname",i.getProperty("custom_person_name_given")+" "+i.getProperty("custom_person_name_family"));
        } else if ( i.getProperty("custom_person_name_given") != null ) {
            setp(o,"user_displayname",i.getProperty("custom_person_name_given"));
        } else if ( i.getProperty("custom_person_name_family") != null ) {
            setp(o,"user_displayname",i.getProperty("custom_person_name_family"));

        // Displayname from LTI 1.x
        } else if ( i.getProperty("lis_person_name_full") != null ) {
            setp(o,"user_displayname",i.getProperty("lis_person_name_full"));
        } else if ( i.getProperty("lis_person_name_given") != null && i.getProperty("lis_person_name_family") != null ) {
            setp(o,"user_displayname",i.getProperty("lis_person_name_given")+" "+i.getProperty("lis_person_name_family"));
        } else if ( i.getProperty("lis_person_name_given") != null ) {
            setp(o,"user_displayname",i.getProperty("lis_person_name_given"));
        } else if ( i.getProperty("lis_person_name_family") != null ) {
            setp(o,"user_displayname",i.getProperty("lis_person_name_family"));
        }

        // Trim out repeated spaces and/or weird whitespace from the user_displayname
        // if ( o.getProperty("user_displayname") ) {
            // setp(o,"user_displayname"] = trim(preg_replace("/\s+/", " ",$retval["user_displayname"]));
        // }

        // Get the role
        setp(o,"role","0");
        String roles = "";
        if ( i.getProperty("custom_membership_role") != null ) { // From LTI 2.x
            roles = i.getProperty("custom_membership_role");
        } else if ( i.getProperty("roles") != null ) { // From LTI 1.x
            roles = i.getProperty("roles");
        }

        if ( roles.length() > 0 ) {
            roles = roles.toLowerCase();
            if ( roles.indexOf("instructor") > 0 ) setp(o,"role","1");
            if ( roles.indexOf("administrator") > 0 ) setp(o,"role","1");
        }
        return o;
    }

    public static void setp(Properties o, String key, String value ) {
        if ( value == null ) return;
        o.setProperty(key, value);
    }

}

