
package org.tsugi;

import org.tsugi.Tsugi;
import org.tsugi.impl.Tsugi_JDBC;

/**
 * This factory returns the appropriate Tsugi implementation.
 */
public class TsugiFactory {
   /**
     * Return the implementation.
     */
    public static Tsugi getTsugi() {
        return new Tsugi_JDBC();
    }
}
