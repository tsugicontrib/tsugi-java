
package org.tsugi.base;

import org.tsugi.*;
import java.util.Properties;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseOutput implements Output {

    public HttpServletRequest request = null;
    public HttpServletResponse response = null;
    public HttpSession session = null;

    public BaseOutput(HttpServletRequest request, HttpServletResponse response)
    {
        this.request = request;
        this.response = response;
        if ( request != null ) {  // During unit tests
            this.session = request.getSession();
        }
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

/*

    function header($headCSS=false) {
        global $HEAD_CONTENT_SENT, $CFG, $RUNNING_IN_TOOL;
        global $CFG;
        if ( $HEAD_CONTENT_SENT === true ) return;
        header("Content-Type: text/html; charset=utf-8");
    ?><!DOCTYPE html>
    <html>
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><?php echo($CFG->servicename); ?></title>
        <!-- Le styles -->
        <link href="<?php echo($CFG->staticroot); ?>/static/css/custom-theme/jquery-ui-1.10.0.custom.css" rel="stylesheet">
        <link href="<?php echo($CFG->staticroot); ?>/static/bootstrap-3.1.1/css/bootstrap.min.css" rel="stylesheet">
        <link href="<?php echo($CFG->staticroot); ?>/static/bootstrap-3.1.1/css/bootstrap-theme.min.css" rel="stylesheet">

    <style> <!-- from navbar.css -->
    body {
      padding-top: 20px;
      padding-bottom: 20px;
    }

    .navbar {
      margin-bottom: 20px;
    }
    </style>

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn"t work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="<?php echo($CFG->wwwroot); ?>/static/html5shiv/html5shiv.js"></script>
          <script src="<?php echo($CFG->wwwroot); ?>/static/respond/respond.min.js"></script>
        <![endif]-->

    <?php
        if ( isset($_SESSION["CSRF_TOKEN"]) ) {
            echo("<script type="text/javascript">CSRF_TOKEN = "".$_SESSION["CSRF_TOKEN"]."";</script>"."\n");
        } else {
            echo("<script type="text/javascript">CSRF_TOKEN = "TODORemoveThis";</script>"."\n");
        }
        $HEAD_CONTENT_SENT = true;
    }

    function bodyStart($checkpost=true) {
        echo("\n</head>\n<body style=\"padding: 15px 15px 15px 15px;\">\n");
        if ( $checkpost && count($_POST) > 0 ) {
            $dump = safe_var_dump($_POST);
            echo("<p style="color:red">Error - Unhandled POST request</p>");
            echo("\n<pre>\n");
            echo($dump);
            echo("\n</pre>\n");
            error_log($dump);
            die_with_error_log("Unhandled POST request");
        }
    }

    function footerStart() {
        global $CFG;
        echo("<script src="".$CFG->staticroot."/static/js/jquery-1.10.2.min.js"></script>"."\n");
        echo("<script src="".$CFG->staticroot."/static/bootstrap-3.1.1/js/bootstrap.min.js"></script>"."\n");

        // Serve this locally during early development - Move to CDN when stable
        echo("<script src="".$CFG->wwwroot."/static/js/tsugiscripts.js"></script>"."\n");

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

        $this->doAnalytics();
    }

    function footerEnd() {
        echo("\n</body>\n</html>\n");
    }

    function footer($onload=false) {
        global $CFG;
        $this->footerStart();
        if ( $onload !== false ) {
            echo("\n".$onload."\n");
        }
        $this->footerEnd();
    }

    function getSpinnerUrl() {
        global $CFG;
        return $CFG->staticroot . "/static/img/spinner.gif";
    }
*/

}
