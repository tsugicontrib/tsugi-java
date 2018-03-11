
package org.tsugi;

import org.tsugi.Tsugi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This factory returns the appropriate Tsugi implementation.
 *
 * You can configure this Factory using a system-wide property
 *
 * <pre><code>
 * tsugi.factory.tsugiClassName=org.tsugi.impl.jdbc.Tsugi_JDBC
 * </code></pre>
 * 
 * This should only be called once during servlet initialization
 * and then the Tsugi instance should be used during each 
 * request / response cycle.  A sample for using this is as follows:
 *
 * <pre><code>
 * public class TsugiServlet extends HttpServlet {
 * 
 *     Tsugi tsugi = null;
 * 
 *     // Allow overriding from something like Spring
 *     public void setTsugi(Tsugi tsugi)
 *     {
 *         this.tsugi = tsugi;
 *     }
 * 
 *     public void init(ServletConfig config) throws ServletException {
 *         super.init(config);
 *         if ( tsugi == null ) tsugi = TsugiFactory.getTsugi();
 *         System.out.println("Tsugi init="+tsugi);
 *     }
 * </code></pre>
 *
 */
public class TsugiFactory {

    private static Log log = LogFactory.getLog(TsugiFactory.class);

   /**
     * Return the Appropriate Tsugi implementation.
     *
     * Can be overridden by setting the "tsugi.factory.tsugiClassName"
     * in the system-wide properties.
     * @return The appropriate Tsugi implementation
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
