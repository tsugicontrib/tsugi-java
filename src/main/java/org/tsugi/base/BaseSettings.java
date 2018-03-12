
package org.tsugi.base;

import java.util.Properties;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import java.io.IOException;

import org.tsugi.Launch;
import org.tsugi.Settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * The base implementation for the Settings interface
 */
public class BaseSettings implements Settings {

    public Launch launch = null;

    // These must be set in the implementing class
    public Properties settings = new Properties();

    // Constructor is only in the implementing class so as 
    // to allow flexibility in method signature

    public Launch getLaunch()
    {
        return launch;
    }

    /**
     * Persist the settings wherever they need to go.
     *
     * We expect the extending class to override this.  If this is not overridden,
     * settings will be in-memory only.
     * @return True if the settings are persistent
     */
    public boolean persistSettings()
    {
        return true;
    }

    public String getSettingsJson()
    {
        if ( settings == null ) return "{}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.createObjectNode();
        Enumeration keys = settings.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = (String)settings.get(key);
            ((ObjectNode) root).put(key,value);
        }
        return root.toString();
    }

    public boolean setSettingsJson(String json)
        throws IOException
    {
        ObjectMapper m = new ObjectMapper();
        JsonNode doc = m.readTree(json);
        settings = new Properties();
        if ( doc.isObject() ) {
            Iterator<Map.Entry<String,JsonNode>> ite = doc.fields();
            while (ite.hasNext()) {
                Map.Entry<String,JsonNode> temp = (Map.Entry<String,JsonNode>) ite.next();
                String key = temp.getKey();
                JsonNode node = temp.getValue();
                if ( node.isValueNode() ) {
                    String value = node.asText();
                    settings.setProperty(key, value);
                }
 
            }
        }
        return persistSettings();
    }

    public Properties getSettings()
    {
        return settings;
    }

    public boolean setSettings(Properties props)
    {
        settings = props;
        return persistSettings();
    }

    public boolean updateSettings(Properties props)
    {
        if ( settings == null ) return false;
        if ( props == null ) return false;

        Enumeration keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = (String)props.get(key);
            props.setProperty(key, value);
        }

        return persistSettings();
    }

    public String getSetting(String key, String def)
    {
        if ( settings == null ) return def;
        String retval = settings.getProperty(key);
        if ( retval == null ) retval = def;
        return retval;
    }

    public boolean setSetting(String key, String value)
    {
        if ( settings == null ) return false;
        if ( key == null ) return false;
        String oldValue = settings.getProperty(key);
        if ( value.equals(oldValue) ) return true;
        settings.setProperty(key, value);
        persistSettings();
        return true;
    }

}
