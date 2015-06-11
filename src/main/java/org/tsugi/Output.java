
package org.tsugi;

import org.tsugi.*;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a class that captures the output conventions of Tusgi.
 *
 * In order to be consistent across Tsugi tools we capture the kinds of
 * HTML conventions we want to use.   This allows us to change our UI
 * in one place.
 *
 * A typical Tsugi Tool can get a lot done with the rough outline:
 *
 *     use \Tsugi\Core\LTIX;
 *
 *     // Require CONTEXT, USER, and LINK
 *     $LTI = LTIX::requireData();
 *
 *     // Handle incoming POST data and redirect as necessary...
 *     if ( ... ) {
 *         header( "Location: ".addSession("index.php") ) ;
 *     }
 *
 *     // Done with POST
 *     $OUTPUT->header();
 *     $OUTPUT->bodyStart();
 *     $OUTPUT->flashMessages();
 *
 *     // Output some HTML
 *
 *     $OUTPUT->footerStart();
 *     ?>
 *        // Stick some JavaScript here...
 *     <?php
 *     $OUTPUT->footerEnd();
 *
 * This class is likely to grow a bit to capture new needs as they arise.
 * You can look at the various bits of sample code in the mod and other
 * tool folders to see patterns of the use of this class.
 */
public interface Output {

/*
    function flashMessages() {
        if ( isset($_SESSION["error"]) ) {
            echo "<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert">&times;</a>".
            $_SESSION["error"]."</div>\n";
            unset($_SESSION["error"]);
        }
        if ( isset($_SESSION["success"]) ) {
            echo "<div class="alert alert-success"><a href="#" class="close" data-dismiss="alert">&times;</a>".
            $_SESSION["success"]."</div>\n";
            unset($_SESSION["success"]);
        }
    }

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
