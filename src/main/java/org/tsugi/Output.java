
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
     * @return A properties list of the software installed and versions.
     */
    public Properties header(PrintWriter out);

    /**
     * Finsh the header and start the body - emit the CSS includes
     *
     * This includes all of the CSS but does not include the
     * JavaScript.  JavaScript is included in footerStart()
     * 
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
     */
    public void footerStart(PrintWriter out);

    /**
     * Finish the footer.
     */
    public void footerEnd(PrintWriter out);

    /** 
     * Start the side navigation if the tool wants it
     **/
    public void navStart(PrintWriter out);

    /** 
     * Complete the side navigation if it was started
     **/
    public void navEnd(PrintWriter out);

    /** 
     * Get a GET URL to the current servlet
     *
     * We abstract this in case the framework needs to 
     * point to a URL other than the URL in the request
     * object.  This URL should be used for AJAX calls
     * to dynamic data in JavaScript.
     **/
    public String getGetUrl(String path);

    /** 
     * Get a POST URL to the current servlet
     *
     * We abstract this in case the framework needs to 
     * point to a URL other than the URL in the request
     * object.
     **/
    public String getPostUrl(String path);

    /** 
     * Redirect to a path - can be bull
     **/
    public void postRedirect(String path);

    /** 
     * Get any needed hidden form fields
     *
     * This will be properly formatted HTML - most likely one
     * or more input type="hidden" fields - the framework
     * may use this to help it maintain context across
     * request / response cycles.
     *
     * @return String Text to include in a form.  May be the 
     * empty string if nothing is needed by the framework.
     **/
    public String getHidden();

    /** 
     * Get a URL to the 'static' folder within this servlet
     *
     * We abstract this because this might be stored in a
     * CDN for this application. 
     * TODO: Define the property for this
     **/
    public String getStaticUrl();

    /** 
     * Get a URL to a system-wide spinner image
     **/
    public String getSpinnerUrl();

}
