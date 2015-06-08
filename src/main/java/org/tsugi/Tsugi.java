
package org.tsugi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This in effect the Tsugi "Launch Factory" that allows an application to 
 * setup the launch for each request/response cycle.
 */
public interface Tsugi {
   /**
     * Return the version of this application.
     */
    public String getVersion();

   /**
     * Get the launch information for the current session
     */
    public Launch getLaunch(HttpServletRequest req, HttpServletResponse res);
}
