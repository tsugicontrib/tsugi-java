
package org.tsugi;

import org.tsugi.*;
import java.util.Properties;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// TODO: Add documentation to the methods

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
  *     o.header(out);
  *     o.bodyStart(out);
  *     o.flashMessages(out);
  *
  *     out.println("<pre>");
  *     out.println("Welcome to hello world!");
  */

public interface Output {

    public final String SESSION_SUCCESS = "tsugi::session::success";
    public final String SESSION_ERROR = "tsugi::session::error";
    public final String SESSION_CSRF = "tsugi::session::csrf";

    public void flashSuccess(String message);

    public void flashError(String message);

    public void flashMessages(PrintWriter out);

    public void header(PrintWriter out);

    public void bodyStart(PrintWriter out);

    public void footerStart(PrintWriter out);

    public void footerEnd(PrintWriter out);

    public String getSpinnerUrl();


}
