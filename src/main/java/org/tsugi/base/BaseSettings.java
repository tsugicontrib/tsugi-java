
package org.tsugi.base;

import java.util.Properties;
import java.util.Enumeration;

import org.tsugi.Settings;

import org.apache.commons.lang3.StringUtils;

/**
 */
public class BaseSettings implements Settings {

    // These must be set in the implementing class
    public Properties settings = null;

    // Constructor is only in the implementing class so as 
    // to allow flexibility in method signature

   /**
     * Retrieve an JSON string of all of the settings
     */
    public String getSettingsJson()
    {
        return null;
    }

    /**
     * Set all of the settings from a JSON string
     *
     * @param json Properties that are serialized in JSON and stored.
     * Replaces existing settings.  
     */
    public boolean setSettingsJson(String json)
    {
        return false;
    }

   /**
     * Retrieve an array of all of the settings
     *
     * If there are no settings, return an empty array.  
     */
    public Properties getSettings()
    {
        return settings;
    }

    /**
     * Set all of the settings.
     *
     * @param props Properties that are serialized in JSON and stored.
     * Replaces existing settings.  If this is an empty array, this effectively
     * empties out all the settings.
     */
    public boolean setSettings(Properties props)
    {
        settings = props;
        return true;
    }

    /**
     * Set or update a number of keys to new values in link settings.
     *
     * @param props An array of key value pairs that are to be updated / added
     * in the settings.
     */
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
        return true;
    }

    /**
     * Retrieve a particular key from the link settings.
     *
     * Returns the value found in settings or false if the key was not found.
     *
     * @param key - The key to get from the settings.
     * @param def - What to return if the key is not present
     */
    public String getSetting(String key, String def)
    {
        if ( settings == null ) return def;
        String retval = settings.getProperty(key);
        if ( retval == null ) retval = def;
        return retval;
    }

    /**
     * Set or update a key to a new value in link settings.
     *
     * @params key The key to set in settings.
     * @params value The value to set for that key
     */
    public boolean setSetting(String key, String value)
    {
        if ( settings == null ) return false;
        if ( key == null ) return false;
        settings.setProperty(key, value);
        return true;
    }

}
