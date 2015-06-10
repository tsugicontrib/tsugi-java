
package org.tsugi;

import java.util.Properties;

/**
 */
public interface Settings {
   /**
     * Retrieve an array of all of the settings
     *
     * If there are no settings, return an empty array.  
     */
    // public Properties getAllSettings();

    /**
     * Set all of the settings.
     *
     * @param props Properties that are serialized in JSON and stored.
     * Replaces existing settings.  If this is an empty array, this effectively
     * empties out all the settings.
     */
    // public boolean setAllSettings(Properties props);

    /**
     * Set or update a number of keys to new values in link settings.
     *
     * @param props An array of key value pairs that are to be updated / added
     * in the settings.
     */
    // public boolean updateAllSettings(Properties props);

    /**
     * Retrieve a particular key from the link settings.
     *
     * Returns the value found in settings or false if the key was not found.
     *
     * @param key - The key to get from the settings.
     * @param def - What to return if the key is not present
     */
    // public String getSetting(String key, String def);

    /**
     * Set or update a key to a new value in link settings.
     *
     * @params key The key to set in settings.
     * @params value The value to set for that key
     */
    // public boolean setSetting(String key, String value);

}
