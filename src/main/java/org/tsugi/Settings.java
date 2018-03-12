
package org.tsugi;

import java.util.Properties;

/**
 * This is a class to manage settings associated with the link and context.
 */
public interface Settings {

    /**
     * Get the launch associated with this object
     * @return The launch object
     */
    public Launch getLaunch();

   /**
     * Retrieve an JSON string of all of the settings
     * @return The JSON string of all of the settings
     */
    public String getSettingsJson();

    /**
     * Set all of the settings from a JSON string
     *
     * @param json Properties that are serialized in JSON and stored.
     * Replaces existing settings.  Persists the new settings if
     * they can be persisted.
     * @return True if all of the settings from the JSON string are set
     * @throws java.io.IOException if an IO Error is encountered
     */
    public boolean setSettingsJson(String json) throws java.io.IOException;

   /**
     * Retrieve an array of all of the settings
     *
     * If there are no settings, return an empty array.  
     * @return An array of all of the settings
     */
    public Properties getSettings();

    /**
     * Set all of the settings.
     *
     * @param props Properties that are serialized in JSON and stored.
     * Replaces existing settings.  If this is an empty array, this effectively
     * empties out all the settings.Persists the new settings if
     * they can be persisted.
     * @return True if all of the settings are set
     */
    public boolean setSettings(Properties props);

    /**
     * Set or update a number of keys to new values in link settings.
     *
     * @param props An array of key value pairs that are to be updated / added
     * in the settings.
     * @return True if keys are set or updated to new values in the link settings
     */
    public boolean updateSettings(Properties props);

    /**
     * Retrieve a particular key from the link settings.
     *
     * Returns the value found in settings or false if the key was not found.
     * Persists the new settings if they can be persisted.
     *
     * @param key - The key to get from the settings.
     * @param def - What to return if the key is not present
     * @return a particular key from the link settings
     */
    public String getSetting(String key, String def);

    /**
     * Set or update a key to a new value in link settings.
     *
     * Persists the new settings if they can be persisted.
     *
     * @param key The key to set in settings.
     * @param value The value to set for that key
     * @return True if a key is set or updated to a new value in link settings
     */
    public boolean setSetting(String key, String value);

}
