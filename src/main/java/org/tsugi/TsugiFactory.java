
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
     * Return the Appropriate Tsugi implementation.
     *
     * Can be overridden by setting the "tsugi.factory.tsugiClassName"
     * in the system-wide properties.
     */
    public static Tsugi getTsugi() {
        String className = System.getProperty("tsugi.factory.tsugiClassName");
        Tsugi tsugi = null;
        if ( className != null ) {
            try {
                tsugi = (Tsugi) Class.forName(className).newInstance();
            } catch (Exception ex) {
                log.error("Cannot create instance of "+className,ex);
                throw new RuntimeException("Cannot create instance of "+className,ex);
            }
        } else {
            tsugi = new org.tsugi.impl.jdbc.Tsugi_JDBC();
        }
        log.trace("Returning a Tsugi implementation="+tsugi);
        return tsugi;
    }
}
