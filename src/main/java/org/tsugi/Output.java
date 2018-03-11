
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
  * <pre><code>
  *     PrintWriter out = res.getWriter();
  *
  *     Launch launch = tsugi.getLaunch(req, res);
  *     if ( launch.isComplete() ) return;
  *
  *     Output o = launch.getOutput();
  *
  *     Properties p = o.header(out);
  *     out.println("&lt;title&gt;Sample&lt;/title&gt;");
  *     o.bodyStart(out);
  *     o.navStart(out); // If you want it..
  *     o.flashMessages(out);
  *
  *     out.println("&lt;pre&gt;");
  *     out.println("Welcome to hello world!");
  *     out.println("&lt;/pre&gt;");
  *
  *     o.navEnd(out); // If you started it
  *     o.footerStart(out);
  *     // Some of my own JavaScript goodness
  *     $(document).ready( ... );
  *     o.footerEnd(out);
  * </code></pre>
  *
  * The header() only includes the CSS for the libraries.  The 
  * the JavaScript librarys are included by footerStart().  This
  * is where the tool can add JQuery plugins or include JavaScript of its
  * own.
  *
  * The implementation of this class is likely to evolve quite a bit
  * as we someday add features to LMS systems like Sakai and OAE to share 
  * their navigation bits with Tsugi tools.  So don't dig too deep into
  * the implementations or hack the static files too much.
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

    /**
     * Get the launch associated with this object
     * @return The launch object
     */
    public Launch getLaunch();

    // TODO: Add documentation to the methods
    public void flashSuccess(String message);

    public void flashError(String message);

    public void flashMessages(PrintWriter out);

    /**
     * Emits the header and expected scripts.  
     *
     * At this point (there may be more in the future) Tsugi provisions
     * Handlebars, Bootstrap, jQjuery, and jQuery-ui.
     *
     * @param out Represents the PrintWriter object
     * @return A properties list of the software installed and versions.
     */
    public Properties header(PrintWriter out);

    /**
     * Finsh the header and start the body - emit the CSS includes
     *
     * This includes all of the CSS but does not include the
     * JavaScript.  JavaScript is included in footerStart()
     * 
     * @param out Represents the PrintWriter object
     */
    public void bodyStart(PrintWriter out);

    /**
     * Start the footer emit the JavaScript includes.
     *
     * This includes all of the JavaScript libraries any "document
     * ready" work neede by the Tsugi navigation that surrounds the
     * tool.  The tool can include their own libraries here and add
     * their own "document ready" processing.
     *
     * If a tool chooses to include their own version of jQuery or
     * handlebars - they must so a noConflict() and leave the 
     * global variables "Handlebars" and "$" pointing at Tsugi's
     * version of these libraries.
     *
     * @param out Represents the PrintWriter object
     */
    public void footerStart(PrintWriter out);

    /**
     * Finish the footer.
     * @param out Represents the PrintWriter object
     */
    public void footerEnd(PrintWriter out);

    /** 
     * Start the side navigation if the tool wants it
     * @param out Represents the PrintWriter object
     **/
    public void navStart(PrintWriter out);

    /** 
     * Complete the side navigation if it was started
     * @param out Represents the PrintWriter object
     **/
    public void navEnd(PrintWriter out);

}
