
package org.tsugi.base;

import org.tsugi.*;
import java.util.Properties;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseOutput implements Output {

    public Launch launch = null;
    public HttpServletRequest request = null;
    public HttpServletResponse response = null;
    public HttpSession session = null;

    public BaseOutput(Launch launch)
    {
        this.launch = launch;
        this.request = launch.getRequest();
        this.response = launch.getResponse();
        if ( request != null ) {  // During unit tests
            this.session = request.getSession();
        }
    }

    public Launch getLaunch()
    {
        return launch;
    }

    public void flashSuccess(String message)
    {
        if ( session == null ) return;
        session.setAttribute(Output.SESSION_SUCCESS, message);
    }

    public void flashError(String message)
    {
        if ( session == null ) return;
        session.setAttribute(Output.SESSION_ERROR, message);
    }

    public void flashMessages(PrintWriter out)
    {
        if ( session == null ) return;
        String error = (String) session.getAttribute(Output.SESSION_ERROR);
        String success = (String) session.getAttribute(Output.SESSION_SUCCESS);
        session.removeAttribute(Output.SESSION_ERROR);
        session.removeAttribute(Output.SESSION_SUCCESS);

        if ( error != null ) {
            out.print("<div class=\"alert alert-danger\"><a href=\"#\" class=\"close\" ");
            out.print("data-dismiss=\"alert\">&times;</a>");
            out.print(error);
            out.println("</div>");
        }

        if ( success != null ) {
            out.print("<div class=\"alert alert-success\"><a href=\"#\" class=\"close\" ");
            out.print("data-dismiss=\"alert\">&times;</a>");
            out.print(success);
            out.println("</div>");
        }
    }

    public Properties header(PrintWriter out)
    {
        if ( response != null ) response.setContentType("text/html; charset=utf-8");

        Properties props = new Properties();
        props.setProperty(Output.HANDLEBARS,"3.0.3"); // JavaScript
        props.setProperty(Output.JQUERY,"1.10.2"); // JavaScript

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" >");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");

        // Path to static assets
        String statpath = request.getContextPath();

        out.print("<link href=\"");
        out.print(statpath);
        out.println("/static/css/custom-theme/jquery-ui-1.10.0.custom.css\" rel=\"stylesheet\">");
        props.setProperty(Output.JQUERY_UI,"1.10.0");

        out.print("<link href=\"");
        out.print(statpath);
        out.println("/static/bootstrap-3.1.1/css/bootstrap.min.css\" rel=\"stylesheet\">");
        props.setProperty(Output.BOOTSTRAP,"3.1.1");

        out.print("<link href=\"");
        out.print(statpath);
        out.println("/static/bootstrap-3.1.1/css/bootstrap-theme.min.css\" rel=\"stylesheet\">");

        out.println("<style> <!-- from navbar.css -->");
        out.println(" body { padding-top: 20px; padding-bottom: 20px; }");
        out.println(" .navbar { margin-bottom: 20px; }");
        out.println("</style>");
        out.println("<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->");
        out.println("<!--[if lt IE 9]>");
        out.print("<script src=\"");
        out.print(statpath);
        out.println("/static/html5shiv/html5shiv.js\"></script>");
        out.print("<script src=\"");
        out.print(statpath);
        out.println("/static/respond/respond.min.js\"></script>");
        out.println("<![endif]-->");
        boolean done = false;
        if ( session != null ) {
            String csrf = (String) session.getAttribute(Output.SESSION_CSRF);
            if ( csrf != null ) {
                done = true;
                out.print("<script type=\"text/javascript\">CSRF_TOKEN = '");
                out.print(csrf);
                out.println("\";</script>");
            }
        }
        if ( ! done ) {
            out.println("<script type=\"text/javascript\">CSRF_TOKEN = \"TODORemoveThis\";</script>");
        }
        return props;
    }

    public void bodyStart(PrintWriter out)
    {
        out.println("");
        out.println("</head>");
        out.println("<body style=\"padding: 15px 15px 15px 15px;\">");
        // TODO: Complain about post data?
    }

    public void navStart(PrintWriter out)
    {
        // Placeholder for Nico
    }
    
    public void navEnd(PrintWriter out)
    {
        // Placeholder for Nico
    }

    public void footerStart(PrintWriter out)
    {
        String statpath = request.getContextPath();

        out.print("<script src=\"");
        out.print(statpath);
        out.println("/static/js/jquery-1.10.2.min.js\"></script>");

        out.print("<script src=\"");
        out.print(statpath);
        out.println("/static/bootstrap-3.1.1/js/bootstrap.min.js\"></script>");

        out.print("<script src=\"");
        out.print(statpath);
        out.println("/static/js/handlebars-v3.0.3.js\"></script>");

        // Serve this locally during early development - Move to CDN when stable
        out.print("<script src=\"");
        out.print(statpath);
        out.println("/static/js/tsugiscripts.js\"></script>");

        // TODO: Handle heartbeat
        /*
        if ( isset($CFG->sessionlifetime) ) {
            $heartbeat = ( $CFG->sessionlifetime * 1000) / 2;
            // $heartbeat = 10000;
    ?>
    <script type="text/javascript">
    HEARTBEAT_URL = "<?php echo(addSession($CFG->wwwroot."/core/util/heartbeat.php")); ?>";
    HEARTBEAT_INTERVAL = setInterval(doHeartBeat, <?php echo($heartbeat); ?>);
    </script>
    <?php
        }
        */

        // TODO: Analytics
        // $this->doAnalytics();
    }

    public void footerEnd(PrintWriter out)
    {   
        out.println("");
        out.println("</body>");
        out.println("</html>");
    }

    public String getGetUrl(String path)
    {
        String retval = request.getContextPath();
        String sp = request.getServletPath();
        if ( sp != null ) retval = retval + sp;
        if ( path != null ) retval = retval + "/" + path;
        return retval;
    }

    public String getPostUrl(String path)
    {
        return getGetUrl(path); 
    }

    public String getHidden()
    {
        return "<!-- hidden -->\n";
    }

    public String getStaticUrl()
    {
        String retval = request.getContextPath() + "/static";
        return retval;
    }

    public String getSpinnerUrl() 
    {
        String statpath = getStaticUrl() + "/img/spinner.gif";
        return statpath;
    }

}
