
package org.tsugi;

import org.tsugi.Tsugi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This factory returns the appropriate Tsugi implementation.
 */
public class TsugiFactory {

    private static Log log = LogFactory.getLog(TsugiFactory.class);

   /**
     * Return the implementation.
     */
    public static Tsugi getTsugi() {
        Tsugi tsugi = new org.tsugi.impl.jdbc.Tsugi_JDBC();
        log.trace("Returning a Tsugi implementation="+tsugi);
        return tsugi;
    }
}
