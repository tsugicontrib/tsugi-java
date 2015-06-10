package org.tsugi.base;

import java.util.Properties;

import org.tsugi.Tsugi;
import org.tsugi.Launch;
import org.tsugi.util.TsugiUtils;
import org.tsugi.util.TsugiLTIUtils;

import java.util.Properties;
import java.util.Enumeration;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;
import net.oauth.signature.OAuthSignatureMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

import org.apache.commons.lang3.StringUtils;

import org.imsglobal.basiclti.BasicLTIUtil;

/*
 ** Implement the code that is common across implementations here. 
 */
public abstract class BaseTsugi implements Tsugi 
{

    private Log log = LogFactory.getLog(BaseTsugi.class);

    public String base_string = null;
    public String error_message = null;

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
        if ( email == null ) email = i.getProperty("custom_person_email_primary");
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

        // Lets trim whitespace from all of these properties
        Enumeration keys = o.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = o.getProperty(key);
            if ( value == null ) continue;
            String newValue = StringUtils.strip(value);
            if ( value.equals(newValue) ) continue;
            o.setProperty(key,newValue);
        }

        return o;
    }


    /**
     * Check the OAuth Signature
     */
    public boolean checkOAuthSignature(HttpServletRequest request, String oauth_secret, String oauth_consumer_key)
    {
        String URL = TsugiUtils.getOurServletPath(request);
        OAuthMessage oam = OAuthServlet.getMessage(request, URL);
        OAuthValidator oav = new SimpleOAuthValidator();
        OAuthConsumer cons = new OAuthConsumer("about:blank#OAuth+CallBack+NotUsed", oauth_consumer_key,oauth_secret, null);

        OAuthAccessor acc = new OAuthAccessor(cons);

        base_string = null;
        error_message = null;
        try {
            base_string = OAuthSignatureMethod.getBaseString(oam);
        } catch (Exception e) {
            base_string = null;
        }

        try {
            oav.validateMessage(oam, acc);
        } catch (Exception e) {
            error_message = "Provider failed to validate message";
            return false;
        }
        return true;
    }

}

