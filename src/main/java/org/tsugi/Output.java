
package org.tsugi;

import org.tsugi.*;
import java.util.Properties;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
  * A Tsugi application makes use of this class as follows:
  *
  *     PrintWriter out = res.getWriter();
  *
  *     Launch launch = tsugi.getLaunch(req, res);
  *     if ( launch.isComplete() ) return;
  *
  *     Output o = launch.getOutput();
  *
  *     Properties p = o.header(out);
  *     out.println("<title>Sample</title>");
  *     o.bodyStart(out);
  *     o.navStart(out); // If you want it..
  *     o.flashMessages(out);
  *
  *     out.println("<pre>");
  *     out.println("Welcome to hello world!");
  *     out.println("</pre>");
  *
  *     o.navEnd(out); // If you started it
  *     o.footerStart(out);
  *     // Some of my own JavaScript goodness
  *     o.footerEnd(out);
  *
  * The implementation of this class is likely to evolve quite a bit
  * as we add features to LMS systems like Sakai and OAE to share 
  * their navigation bits with Tsugi tools.  So don't dig too deep into
  * the implementations or hack the statics files too much.
  */

public interface Output {

    public final String SESSION_SUCCESS = "tsugi::session::success";
    public final String SESSION_ERROR = "tsugi::session::error";
    public final String SESSION_CSRF = "tsugi::session::csrf";

    // Canonnical names for software
    public final String HANDLEBARS = "handlebars";
    public final String JQUERY_UI = "jquery-ui";
    public final String JQUERY = "jquery";
    public final String BOOTSTRAP = "bootstrap";

    // TODO: Add documentation to the methods
    public void flashSuccess(String message);

    public void flashError(String message);

    public void flashMessages(PrintWriter out);

    /**
     * Emits the header and expected scripts.  
     *
     * @return A properties list of the software installed and versions.
     */
    public Properties header(PrintWriter out);

    public void bodyStart(PrintWriter out);

    public void footerStart(PrintWriter out);

    public void footerEnd(PrintWriter out);

    public void navStart(PrintWriter out);

    public void navEnd(PrintWriter out);

    public String getSpinnerUrl();

}
